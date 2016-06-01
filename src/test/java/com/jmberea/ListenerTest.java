package com.jmberea;

import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;

import com.jmberea.ghostgame.config.MvcConfiguration;
import com.jmberea.ghostgame.listener.LoadDictionaryContextListener;
import com.jmberea.ghostgame.util.Const;

import junit.framework.Assert;

@ContextConfiguration(classes = MvcConfiguration.class)
@RunWith(JUnit4.class)
public class ListenerTest {

	MockServletContext servletContext;
	ServletContextEvent event;

	@Before
	public void setUp() {
		servletContext = new MockServletContext();
		event = new ServletContextEvent(servletContext);
	}

	@Test
	public void testGetIntegerParameter() {
		LoadDictionaryContextListener listener = new LoadDictionaryContextListener();
		listener.contextInitialized(event);

		Assert.assertNotNull(event.getServletContext());
		Assert.assertNotNull(event.getServletContext().getAttribute(Const.DICTIONARY_CTX_NAME));
		
		listener.contextDestroyed(event);
		Assert.assertNull(event.getServletContext().getAttribute(Const.DICTIONARY_CTX_NAME));
	}

}
