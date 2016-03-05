package pl.wavesoftware.gasper.internal;

import com.google.common.collect.ImmutableList;
import pl.wavesoftware.eid.exceptions.EidIllegalStateException;
import pl.wavesoftware.gasper.Gasper;
import pl.wavesoftware.maven.MavenResolver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-05
 */
public class GasperBuilder extends Gasper.RunnerCreator {

    private String packaging = MavenResolver.DEFAULT_PACKAGING;
    private String classifier = "";
    private List<Map.Entry<String, String>> javaOpts = new ArrayList<>();
    private String portJavaOption;
    private Integer port;
    private boolean inheritIO = false;
    private String context = Gasper.DEFAULT_CONTEXT;
    private int portAvailableMaxTime = Gasper.DEFAULT_PORT_AVAILABLE_MAX_SECONDS;
    private int deploymentMaxTime = Gasper.DEFAULT_DEPLOYMENT_MAX_SECONDS;
    private Function<HttpEndpoint, Boolean> contextChecker = Executor.DEFAULT_CONTEXT_CHECKER;

    public GasperBuilder withPackaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    public GasperBuilder withClassifier(String classifier) {
        this.classifier = classifier;
        return this;
    }

    public GasperBuilder withJavaOption(String key, String value) {
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>(key, value);
        javaOpts.add(entry);
        return this;
    }

    public GasperBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public GasperBuilder usePortJavaOptionFor(String portJavaOption) {
        this.portJavaOption = portJavaOption;
        return this;
    }

    public GasperBuilder inheritIO() {
        return inheritIO(true);
    }

    public GasperBuilder inheritIO(boolean inheritIO) {
        this.inheritIO = inheritIO;
        return this;
    }

    public Gasper create() {
        if (port == null) {
            port = findNotBindedPort();
        }
        if (portJavaOption != null) {
            withJavaOption(portJavaOption, port.toString());
        }
        Settings settings = new Settings(
            packaging, classifier, port, ImmutableList.copyOf(javaOpts),
            inheritIO, context, contextChecker,
            portAvailableMaxTime, deploymentMaxTime
        );
        return create(settings);
    }

    public GasperBuilder maxStartupTime(int seconds) {
        this.portAvailableMaxTime = seconds;
        return this;
    }

    public GasperBuilder maxDeploymentTime(int seconds) {
        this.deploymentMaxTime = seconds;
        return this;
    }

    public GasperBuilder waitForContext(String context) {
        this.context = context;
        return this;
    }

    public GasperBuilder useContextChecker(Function<HttpEndpoint, Boolean> contextChecker) {
        this.contextChecker = contextChecker;
        return this;
    }

    private static int findNotBindedPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException ex) {
            throw new EidIllegalStateException("20160305:000138", ex);
        }
    }
}
