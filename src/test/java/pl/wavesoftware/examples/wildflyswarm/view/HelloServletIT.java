package pl.wavesoftware.examples.wildflyswarm.view;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.ClassRule;
import org.junit.Test;
import pl.wavesoftware.wildflyswarm.testing.JavaMinusJarRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-04
 */
public class HelloServletIT {

    @ClassRule
    public static JavaMinusJarRunner runner = JavaMinusJarRunner.builder()
        .withPackaging("jar")
        .withClassifier("swarm")
        .withPortJavaOption(JavaMinusJarRunner.WILDFLY_SWARM)
        .build();

    @Test
    public void testGetRoot() throws UnirestException {
        // given
        String address = String.format("http://localhost:%d/", runner.getPort());
        String expectedMessage = "Funny hello Dolph Lundgren, Arnold Schwarzenegger!";

        // when
        HttpResponse<String> response = Unirest.get(address).asString();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedMessage);
    }
}
