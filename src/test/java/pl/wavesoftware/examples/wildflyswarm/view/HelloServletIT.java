package pl.wavesoftware.examples.wildflyswarm.view;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.ClassRule;
import org.junit.Test;
import pl.wavesoftware.gasper.Gasper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-04
 */
public class HelloServletIT {

    @ClassRule
    public static Gasper gasper = Gasper.defaultForWildflySwarm().create();

    @Test
    public void testGetRoot() throws UnirestException {
        // given
        String address = String.format("http://localhost:%d/", gasper.getPort());
        String expectedMessage = "Hello Dolph Lundgren, Arnold Schwarzenegger :P";

        // when
        HttpResponse<String> response = Unirest.get(address).asString();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedMessage);
    }
}
