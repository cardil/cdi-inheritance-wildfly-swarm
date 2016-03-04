package pl.wavesoftware.examples.wildflyswarm.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.wavesoftware.examples.wildflyswarm.service.HelloService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Krzysztof Suszynski <krzysztof.suszynski@coi.gov.pl>
 * @since 04.03.16
 */
@RunWith(MockitoJUnitRunner.class)
public class HelloServletTest {

    @InjectMocks
    private HelloServlet servlet;

    @Mock
    private HelloService service;

    @Mock
    private HttpServletRequest servletRequest;

    @Mock
    private HttpServletResponse servletResponse;

    @Test
    public void testDoGet() throws Exception {
        // given
        String hello = "Hello to nobody!";
        when(service.makeHello()).thenReturn(hello);
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        when(servletResponse.getWriter()).thenReturn(pw);

        // when
        servlet.doGet(servletRequest, servletResponse);

        // then
        assertThat(writer.toString()).isEqualTo(hello);
    }
}