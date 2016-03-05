package pl.wavesoftware.gasper.internal;

import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-05
 */
@Data
public class Settings {
    private final String packaging;
    private final String classifier;
    private final int port;
    private final List<Map.Entry<String, String>> javaOptions;
    private final boolean inheritIO;
    private final String context;
    private final Function<HttpEndpoint, Boolean> contextChecker;
    private final int portAvailableMaxTime;
    private final int deploymentMaxTime;

    public List<Map.Entry<String, String>> getJavaOptions() {
        return ImmutableList.copyOf(javaOptions);
    }

}
