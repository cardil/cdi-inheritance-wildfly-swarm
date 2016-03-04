package pl.wavesoftware.examples.wildflyswarm.view;

import lombok.RequiredArgsConstructor;
import pl.wavesoftware.examples.wildflyswarm.service.HelloService;

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
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class HelloServlet extends HttpServlet {

    private final HelloService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append(service.makeHello());
    }
}
