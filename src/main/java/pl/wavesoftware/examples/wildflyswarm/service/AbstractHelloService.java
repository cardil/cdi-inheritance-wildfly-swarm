package pl.wavesoftware.examples.wildflyswarm.service;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.examples.wildflyswarm.domain.User;
import pl.wavesoftware.examples.wildflyswarm.service.api.HelloService;
import pl.wavesoftware.examples.wildflyswarm.service.api.UserService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public abstract class AbstractHelloService implements HelloService {

    private final UserService userService;

    protected abstract String helloMessage();

    @Override
    public String makeHello() {
        Collection<User> users = userService.fetchActiveUser();
        StringBuilder sb = new StringBuilder();
        sb.append(helloMessage()).append(" ");
        Optional<String> result = users.stream()
                .map(AbstractHelloService::presentUser)
                .reduce(AbstractHelloService::join);
        if (result.isPresent()) {
            sb.append(result.get());
        } else {
            sb.append("to no one");
        }
        sb.append("!");
        return sb.toString();
    }

    private static String presentUser(User user) {
        return user.getName() + " " + user.getSurname();
    }

    private static String join(String userRepr1, String userRepr2) {
        return userRepr1 + ", " + userRepr2;
    }
}
