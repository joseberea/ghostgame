package com.jmberea.ghostgame.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jmberea.ghostgame.util.Const;

@Controller
public class HomeController {
	@RequestMapping(value = "home.htm")
	public ModelAndView goHome(HttpServletRequest request) throws IOException {
		request.getSession().getServletContext().setAttribute(Const.STRING_CTX_NAME, "");
		return new ModelAndView("game");
	}
}
