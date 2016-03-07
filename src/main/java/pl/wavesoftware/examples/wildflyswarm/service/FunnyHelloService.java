package pl.wavesoftware.examples.wildflyswarm.service;

import pl.wavesoftware.examples.wildflyswarm.service.api.DefaultImpl;
import pl.wavesoftware.examples.wildflyswarm.service.api.UserService;

import javax.inject.Inject;

import static pl.wavesoftware.examples.wildflyswarm.service.Hello.Hellos.FUNNY;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@DefaultImpl
@Hello(FUNNY)
public class FunnyHelloService extends AbstractHelloService {

    @Inject
    public FunnyHelloService(UserService userService) {
        super(userService);
    }

    @Override
    protected String helloTemplate() {
        return "Hello %s :P";
    }
}
