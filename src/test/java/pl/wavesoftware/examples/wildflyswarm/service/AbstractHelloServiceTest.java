package pl.wavesoftware.examples.wildflyswarm.service;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.wavesoftware.examples.wildflyswarm.domain.User;
import pl.wavesoftware.examples.wildflyswarm.service.api.UserService;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractHelloServiceTest {
    private static final Collection<User> USERS = ImmutableSet.of(
            new User("John", "Rambo"),
            new User("Mr.", "Anderson")
    );

    @Mock
    private UserService userService;
    @InjectMocks
    private TestHelloService helloService;

    @Test
    public void testMakeHello() throws Exception {
        // given
        when(userService.fetchActiveUsers()).thenReturn(USERS);

        // when
        String hello = helloService.makeHello();

        // then
        assertThat(hello).isNotEmpty();
        assertThat(hello).isEqualTo("Hello John Rambo, Mr. Anderson!");
    }

    @Test
    public void testMakeHello_toEmpty() throws Exception {
        // given
        when(userService.fetchActiveUsers()).thenReturn(ImmutableSet.<User>of());

        // when
        String hello = helloService.makeHello();

        // then
        assertThat(hello).isNotEmpty();
        assertThat(hello).isEqualTo("Hello to no one!");
    }

    private static class TestHelloService extends AbstractHelloService {

        public TestHelloService(UserService userService) {
            super(userService);
        }

        @Override
        protected String helloTemplate() {
            return "Hello %s!";
        }
    }
}
