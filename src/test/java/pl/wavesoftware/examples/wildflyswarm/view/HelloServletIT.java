package pl.wavesoftware.examples.wildflyswarm.view;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 * @since 2016-03-04
 */
public class HelloServletIT {
    @Test
    public void testGetRoot() throws UnirestException {

        // given
        String address = "http://localhost:8080/";
        String expectedMessage = "Funny hello Dolph Lundgren, Arnold Schwarzenegger!";

        // when
        HttpResponse<String> response = Unirest.get(address).asString();

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(expectedMessage);
    }
}
