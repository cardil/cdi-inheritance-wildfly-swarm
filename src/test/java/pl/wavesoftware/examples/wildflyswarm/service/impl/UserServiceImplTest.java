package pl.wavesoftware.examples.wildflyswarm.service.impl;

import org.junit.Before;
import org.junit.Test;
import pl.wavesoftware.examples.wildflyswarm.domain.User;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
public class UserServiceImplTest {

    private UserServiceImpl userService;

    @Before
    public void before() {
        userService = new UserServiceImpl();
    }

    @Test
    public void testFetchActiveUser() throws Exception {
        // when
        Collection<User> users = userService.fetchActiveUser();

        // then
        assertThat(users).extracting("name").containsExactly("Dolph", "Arnold");
        assertThat(users).extracting("surname").containsExactly("Lundgren", "Schwarzenegger");
    }
}