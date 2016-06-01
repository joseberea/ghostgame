package com.jmberea;

import javax.servlet.ServletContextEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;

import com.jmberea.ghostgame.config.MvcConfiguration;
import com.jmberea.ghostgame.controller.GhostController;
import com.jmberea.ghostgame.controller.HumanController;
import com.jmberea.ghostgame.listener.LoadDictionaryContextListener;
import com.jmberea.ghostgame.util.Const;

import junit.framework.Assert;

@ContextConfiguration(classes = MvcConfiguration.class)
@RunWith(JUnit4.class)
public class GhostTest {

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockServletContext servletContext;
	ServletContextEvent event;

	@InjectMocks
	HumanController humanController;
	
	@InjectMocks
	GhostController ghostController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		servletContext = new MockServletContext();
		event = new ServletContextEvent(servletContext);

		LoadDictionaryContextListener listener = new LoadDictionaryContextListener();
		listener.contextInitialized(event);

		request.getSession().getServletContext().setAttribute(Const.BRANCH_CTX_NAME,
				event.getServletContext().getAttribute(Const.DICTIONARY_CTX_NAME));
		request.getSession().getServletContext().setAttribute(Const.STRING_CTX_NAME, "");
	}

	@Test
	public void controllerTest() {
		humanController.processHumanLetter(request, 'a');
		Assert.assertNotNull(ghostController.getGhostLetter(request));
	}

}
