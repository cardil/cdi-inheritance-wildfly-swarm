package pl.wavesoftware.examples.wildflyswarm.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.wavesoftware.examples.wildflyswarm.service.HelloService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@WebServlet("/")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HelloServlet extends HttpServlet {

    private final HelloService service;

    @PostConstruct
    public void postConstruct() {
        log.info("Staring HelloServlet...");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("A request for: " + request.getRequestURI());
        String hello = service.makeHello();
        log.info(String.format("Hello message: \"%s\"", hello));
        response.getWriter().append(hello);
    }
}
