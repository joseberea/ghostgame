package com.jmberea.ghostgame.controller;

import java.io.IOException;

import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);

	@RequestMapping(value = "home.htm")
	public ModelAndView goHome(HttpServletRequest request) throws IOException {
		logger.info("ESTOY EN EL CONTROLLER");
		request.getSession().getServletContext().setAttribute("string_", "");
		request.getSession().getServletContext().setAttribute("branch_",
				request.getSession().getServletContext().getAttribute("dictionary_"));
		return new ModelAndView("game");
	}
}
