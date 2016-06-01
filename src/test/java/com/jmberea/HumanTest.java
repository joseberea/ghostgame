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
import com.jmberea.ghostgame.controller.HumanController;
import com.jmberea.ghostgame.listener.LoadDictionaryContextListener;
import com.jmberea.ghostgame.util.Const;

import junit.framework.Assert;

@ContextConfiguration(classes = MvcConfiguration.class)
@RunWith(JUnit4.class)
public class HumanTest {

	MockHttpServletRequest request;
	MockHttpServletResponse response;
	MockServletContext servletContext;
	ServletContextEvent event;

	@InjectMocks
	HumanController controller;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		servletContext = new MockServletContext();
		event = new ServletContextEvent(servletContext);

		LoadDictionaryContextListener listener = new LoadDictionaryContextListener();
		listener.contextInitialized(event);

		request.getSession().getServletContext().setAttribute(Const.DICTIONARY_CTX_NAME,
				event.getServletContext().getAttribute(Const.DICTIONARY_CTX_NAME));
		request.getSession().getServletContext().setAttribute(Const.STRING_CTX_NAME, "");
	}

	@Test
	public void wordBranchTest() {
		// Test 'abas' string --> is a word
		Assert.assertEquals(controller.processHumanLetter(request, 'a').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'b').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'a').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 's').intValue(), Const.STATUS_IS_A_WORD);
	}

	@Test
	public void notExistsBranchTest() {
		// Test 'abaa' string --> not exists
		Assert.assertEquals(controller.processHumanLetter(request, 'a').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'b').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'a').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'a').intValue(), Const.STATUS_NOT_EXISTS);
	}

	@Test
	public void drawBranchTest() {
		// Test 'yep' string --> is a draw result (word with length < 4
		Assert.assertEquals(controller.processHumanLetter(request, 'y').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'e').intValue(), Const.STATUS_CONTINUE);
		Assert.assertEquals(controller.processHumanLetter(request, 'p').intValue(), Const.STATUS_DRAW);
	}

}
