package pl.wavesoftware.examples.wildflyswarm.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.wavesoftware.examples.wildflyswarm.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@RunWith(MockitoJUnitRunner.class)
public class FunnyHelloServiceTest {

    @Mock private UserService userService;
    @InjectMocks private FunnyHelloService helloService;

    @Test
    public void testHelloMessage() throws Exception {
        // when
        String message = helloService.helloMessage();

        // then
        assertThat(message).isEqualTo("Funny hello");
    }


}