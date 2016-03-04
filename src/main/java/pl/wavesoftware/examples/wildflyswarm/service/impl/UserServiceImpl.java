package pl.wavesoftware.examples.wildflyswarm.service.impl;

import com.google.common.collect.ImmutableSet;
import pl.wavesoftware.examples.wildflyswarm.domain.User;
import pl.wavesoftware.examples.wildflyswarm.service.UserService;

import java.util.Collection;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
public class UserServiceImpl implements UserService {

    private final static User DOLPH = new User("Dolph", "Lundgren");
    private final static User ARNOLD = new User("Arnold", "Schwarzenegger");

    @Override
    public Collection<User> fetchActiveUser() {
        return ImmutableSet.of(
                DOLPH,
                ARNOLD
        );
    }
}
