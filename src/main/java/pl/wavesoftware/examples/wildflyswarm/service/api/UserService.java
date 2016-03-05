package pl.wavesoftware.examples.wildflyswarm.service.api;

import pl.wavesoftware.examples.wildflyswarm.domain.User;

import java.util.Collection;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
public interface UserService {
    /**
     * Retrieves a collection of active users
     * @return a collection with only active users
     */
    Collection<User> fetchActiveUsers();
}
