package pl.wavesoftware.gasper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import pl.wavesoftware.gasper.internal.GasperBuilder;
import pl.wavesoftware.gasper.internal.Executor;
import pl.wavesoftware.gasper.internal.Settings;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.maven.MavenResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-04
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Gasper implements TestRule {

    public static final String WILDFLY_SWARM = "swarm.http.port";
    public static final String SPRING_BOOT = "server.port";
    public static final int DEFAULT_PORT_AVAILABLE_MAX_SECONDS = 60;
    public static final int DEFAULT_DEPLOYMENT_MAX_SECONDS = 30;
    public static final String DEFAULT_CONTEXT = "/";

    private final Settings settings;
    private Path artifact;
    private Executor executor;

    public static GasperBuilder builder() {
        return new GasperBuilder();
    }

    public Integer getPort() {
        return settings.getPort();
    }

    public static GasperBuilder defaultForWildflySwarm() {
        return builder()
            .withPackaging("jar")
            .withClassifier("swarm")
            .usePortJavaOptionFor(Gasper.WILDFLY_SWARM);
    }

    public static GasperBuilder defaultForSpringBoot() {
        return builder()
            .withPackaging("jar")
            .usePortJavaOptionFor(Gasper.SPRING_BOOT);
    }

    public abstract static class RunnerCreator {
        public Gasper create(Settings settings) {
            return new Gasper(settings);
        }
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
        log.info(String.format("%s simple integration test gasper starting!", this.getClass().getSimpleName()));
        MavenResolver resolver = new MavenResolver();
        artifact = resolver.getBuildArtifact(settings.getPackaging(), settings.getClassifier());
        File workingDirectory = resolver.getBuildDirectory();
        List<String> command = buildCommand();
        log.info(String.format(
            "Command to be executed: \"%s\"", command.stream().collect(Collectors.joining(" "))
        ));
        executor = new Executor(command, workingDirectory, settings);
    }

    private void before() throws IOException {
        executor.start();
        log.info("All looks ready, running tests...");
    }

    private List<String> buildCommand() {
        List<String> command = new ArrayList<>();
        command.add("java");
        buildJavaOptions(command);
        command.add("-jar");
        command.add(artifact.toAbsolutePath().toString());
        return command;
    }

    private void buildJavaOptions(List<String> command) {
        command.addAll(settings.getJavaOptions().stream()
            .map(entry -> String.format("-D%s=%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.toList())
        );
    }

    private void after() {
        log.info("Testing on server completed.");
        executor.stop();
    }
}
