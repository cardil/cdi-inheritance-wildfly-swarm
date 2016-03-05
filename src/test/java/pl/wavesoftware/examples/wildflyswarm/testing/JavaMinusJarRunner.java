package pl.wavesoftware.examples.wildflyswarm.testing;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import pl.wavesoftware.eid.exceptions.Eid;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.maven.MavenResolver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-04
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class JavaMinusJarRunner implements TestRule {

    public static final String WILDFLY_SWARM = "swarm.http.port";
    public static final String SPRING_BOOT = "server.port";
    private static final int DEFAULT_MAX_SECONDS = 60;

    private final String packaging;
    private final String classifier;
    private final Integer port;
    private final List<Map.Entry<String, String>> javaOpts;
    private Path artifact;
    private Process process;
    private File workingDirectory;

    public static JavaMinusJarBuilder builder() {
        return new JavaMinusJarBuilder();
    }

    public static JavaMinusJarRunner buildDefault() {
        return builder().build();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        try {
            setup();
            before();
            return new Statement() {

                @Override
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    } finally {
                        after();
                    }
                }
            };
        } catch (IOException e) {
            throw new EidIllegalStateException("20160305:004035", e);
        }
    }

    private void setup() {
        MavenResolver resolver = new MavenResolver();
        artifact = resolver.getBuildArtifact(packaging, classifier);
        workingDirectory = resolver.getBuildDirectory();
    }

    private void before() throws IOException {
        String[] command = buildCommand();
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDirectory);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        process = pb.start();
        log.info(String.format("Starting \"java -jar\" server, waiting for port: %d to became active...", port));
        boolean ok = waitForPortToBecomeAvialable(process, port, DEFAULT_MAX_SECONDS);
        if (!ok) {
            throw new EidIllegalStateException(new Eid("20160305:003452"),
                "Process %s probably didn't started well after maximum wait time is reached: %s",
                command.toString(), DEFAULT_MAX_SECONDS
            );
        }
        try {
            log.info("Waiting additional 4s for deployment to happen...");
            process.waitFor(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("Waited 4s " + e.getLocalizedMessage(), e);
        }
        log.info("All looks deployed and ready for tests!");
    }

    private static boolean waitForPortToBecomeAvialable(Process process, int port, int maxSeconds) {
        for (int i = 0; i < maxSeconds * 4; i++) {
            try {
                process.waitFor(250, TimeUnit.MILLISECONDS);
                if (isPortTaken(port)) {
                    return true;
                }
            } catch (InterruptedException e) {
                log.debug("Waited 250ms " + e.getLocalizedMessage(), e);
            }
        }
        return false;
    }

    private String[] buildCommand() {
        List<String> command = new ArrayList<>();
        command.add("java");
        for (Map.Entry<String, String> entry : javaOpts) {
            command.add(String.format("-D%s=%s", entry.getKey(), entry.getValue()));
        }
        command.add("-jar");
        command.add(artifact.toAbsolutePath().toString());
        log.info(String.format("Command to be executed: %s", command.stream().collect(Collectors.joining(" "))));
        return command.toArray(new String[command.size()]);
    }

    private void after() {
        log.info("Stopping \"java -jar\" server.");
        process.destroy();
    }

    public Integer getPort() {
        return port;
    }

    public static class JavaMinusJarBuilder {

        private String packaging = MavenResolver.DEFAULT_PACKAGING;
        private String classifier = "";
        private List<Map.Entry<String, String>> javaOpts = new ArrayList<>();
        private String portJavaOption;
        private Integer port;

        public JavaMinusJarBuilder withPackaging(String packaging) {
            this.packaging = packaging;
            return this;
        }

        public JavaMinusJarBuilder withClassifier(String classifier) {
            this.classifier = classifier;
            return this;
        }

        public JavaMinusJarBuilder withJavaOption(String key, String value) {
            Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(key, value);
            javaOpts.add(entry);
            return this;
        }

        public JavaMinusJarBuilder withPort(int port) {
            this.port = port;
            return this;
        }

        public JavaMinusJarBuilder withPortJavaOption(String portJavaOption) {
            this.portJavaOption = portJavaOption;
            return this;
        }

        public JavaMinusJarRunner build() {
            if (port == null) {
                port = findNotBindedPort();
            }
            if (portJavaOption != null) {
                withJavaOption(portJavaOption, port.toString());
            }
            return new JavaMinusJarRunner(
                packaging, classifier, port, ImmutableList.copyOf(javaOpts)
            );
        }
    }

    private static int findNotBindedPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException ex) {
            throw new EidIllegalStateException("20160305:000138", ex);
        }
    }

    private static boolean isPortTaken(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return false;
        } catch (IOException ex) {
            return true;
        }
    }
}
