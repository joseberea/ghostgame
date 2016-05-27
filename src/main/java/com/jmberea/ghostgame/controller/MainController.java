package com.jmberea.ghostgame.controller;

import java.util.Map;

import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vo.NodeVO;

@Controller
public class MainController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/putLetter.htm", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String putLetter(HttpServletRequest request, @RequestParam("nextChar") Character nextChar) {
		logger.info("CHARACTER: " + nextChar);
		String string_ = request.getSession().getServletContext().getAttribute("string_").toString();
		Map<Character, NodeVO> branch_ = (Map<Character, NodeVO>) request.getSession().getServletContext().getAttribute("branch_");
		if(branch_.containsKey(nextChar)) {
			Object element = branch_.get(nextChar);
			if(element instanceof NodeVO) {
				if(((NodeVO) element).getChildren().isEmpty()) {
					string_ += nextChar + ": LEAF";					
				} else {
					string_ += nextChar;
					request.getSession().getServletContext().setAttribute("branch_", ((NodeVO) element).getChildren());
				}
			} else {
				request.getSession().getServletContext().setAttribute("branch_", branch_.get(nextChar));
				string_ += nextChar;
			}
		} else {
			string_ += nextChar + ": NO EXIST";
		}
		request.getSession().getServletContext().setAttribute("string_", string_);
		return string_;
	}
}
