package pl.wavesoftware.examples.wildflyswarm.view;

import lombok.extern.slf4j.Slf4j;
import pl.wavesoftware.examples.wildflyswarm.service.api.DefaultImpl;
import pl.wavesoftware.examples.wildflyswarm.service.api.HelloService;
import pl.wavesoftware.examples.wildflyswarm.service.Hello;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.wavesoftware.examples.wildflyswarm.service.Hello.Hellos.FUNNIER;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@WebServlet("/")
@Slf4j
public class HelloServlet extends HttpServlet {

    private final HelloService defaultImpl;
    private final HelloService funnierImpl;

    @Inject
    public HelloServlet(@Hello(FUNNIER) HelloService funnierImpl, @DefaultImpl HelloService defaultImpl) {
        this.defaultImpl = defaultImpl;
        this.funnierImpl = funnierImpl;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Staring HelloServlet...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("A request for: " + request.getRequestURI());
        String hello = defaultImpl.makeHello();
        log.info(String.format("Hello message: \"%s\"", hello));
        response.getWriter().append(hello);
        log.info(String.format("Funnier version: \"%s\"", funnierImpl.makeHello()));
    }
}
