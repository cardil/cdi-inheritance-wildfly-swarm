package pl.wavesoftware.examples.wildflyswarm.service;

import org.junit.Before;
import org.junit.Test;
import pl.wavesoftware.examples.wildflyswarm.domain.User;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
public class DefaultUserServiceTest {

    private DefaultUserService userService;

    @Before
    public void before() {
        userService = new DefaultUserService();
    }

    @Test
    public void testFetchActiveUser() throws Exception {
        // when
        Collection<User> users = userService.fetchActiveUsers();

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("name").containsExactly("Dolph", "Arnold");
        assertThat(users).extracting("surname").containsExactly("Lundgren", "Schwarzenegger");
    }
}
