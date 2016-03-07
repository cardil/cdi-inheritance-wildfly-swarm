package pl.wavesoftware.examples.wildflyswarm.service;

import pl.wavesoftware.examples.wildflyswarm.service.api.UserService;

import javax.inject.Inject;

import static pl.wavesoftware.examples.wildflyswarm.service.Hello.Hellos.FUNNIER;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 07.03.16
 */
@Hello(FUNNIER)
public class EvenFunnierHelloService extends FunnyHelloService {
    @Inject
    public EvenFunnierHelloService(UserService userService) {
        super(userService);
    }

    @Override
    protected String helloTemplate() {
        return "LOL Hello %s !!!";
    }
}
