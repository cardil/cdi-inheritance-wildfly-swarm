package pl.wavesoftware.examples.wildflyswarm.service;

import pl.wavesoftware.examples.wildflyswarm.service.api.UserService;

import javax.inject.Inject;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
public class FunnyHelloService extends AbstractHelloService {

    @Inject
    public FunnyHelloService(UserService userService) {
        super(userService);
    }

    @Override
    protected String helloMessage() {
        return "Hello :P";
    }
}
