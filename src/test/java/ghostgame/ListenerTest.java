

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent; 

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.jmberea.ghostgame.config.MvcConfiguration;
import com.jmberea.ghostgame.listener.LoadDictionaryContextListener;

@ContextConfiguration(classes = MvcConfiguration.class)
@RunWith(MockitoJUnitRunner.class)
public class ListenerTest {

	public ListenerTest() {
	}

	@Mock
	ServletContextEvent mockEvent;

	@Mock
	ServletContext mockServletContext;
	@Mock
	Configuration mockConfig;

	@Mock
	WebApplicationContext mockWebContext;

	@Mock
	MockServletContext ctx;

	@Test
	public void testContextInitialized() {

		when(mockEvent.getServletContext()).thenReturn(mockServletContext);

		when(mockServletContext.getAttribute(Matchers.anyString())).thenReturn(mockWebContext);

		LoadDictionaryContextListener instance = new LoadDictionaryContextListener();
		instance.contextInitialized(mockEvent);

		verify(mockEvent, times(1)).getServletContext();

	}
	
    @Test
    public void onGet() throws ServletException, IOException{
        SomeServlet someServlet = PowerMock.createPartialMock(SomeServlet.class, "getServletContext");   
        ServletContext servletContext = PowerMock.createNiceMock(ServletContext.class);
        HttpServletRequest httpServletRequest = PowerMock.createNiceMock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = PowerMock.createNiceMock(HttpServletResponse.class);

        someServlet.getServletContext();
        PowerMock.expectLastCall().andReturn(servletContext);

        servletContext.getAttribute("test");
        PowerMock.expectLastCall().andReturn("hello");

        PowerMock.replay(someServlet, servletContext, httpServletRequest, httpServletResponse);

        someServlet.doGet(httpServletRequest, httpServletResponse);
    }

}