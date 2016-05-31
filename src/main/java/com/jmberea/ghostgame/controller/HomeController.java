package com.jmberea.ghostgame.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value = "home.htm")
	public ModelAndView goHome(HttpServletRequest request) throws IOException {
		request.getSession().getServletContext().setAttribute("string_", "");
		request.getSession().getServletContext().setAttribute("branch_",
				request.getSession().getServletContext().getAttribute("dictionary_"));
		return new ModelAndView("game");
	}
}
