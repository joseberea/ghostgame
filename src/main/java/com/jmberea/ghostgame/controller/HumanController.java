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

import com.jmberea.ghostgame.util.Const;
import com.jmberea.ghostgame.vo.NodeVO;

@Controller
public class HumanController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/humanLetter.htm", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer processHumanLetter(HttpServletRequest request, @RequestParam("nextChar") Character nextChar) {
		Map<Character, NodeVO> branch_ = (Map<Character, NodeVO>) request.getSession().getServletContext().getAttribute("branch_");
		String string_ = request.getSession().getServletContext().getAttribute("string_").toString();
		Object status = null;
		if(branch_.containsKey(nextChar)) {
			status = branch_.get(nextChar);
			if(status instanceof NodeVO) {
				if(((NodeVO) status).getChildren().isEmpty() && string_.length() < 3) {
					return Const.STATUS_DRAW;
				} if(((NodeVO) status).isLeaf() && string_.length() >= 3) {
					// End
					return Const.STATUS_IS_A_WORD;
				} else {
					request.getSession().getServletContext().setAttribute("branch_", ((NodeVO) status).getChildren());
				}
			} else {
				// First move
				request.getSession().getServletContext().setAttribute("branch_", branch_.get(nextChar));
			}
		} else {
			return Const.STATUS_NOT_EXISTS;
		}
		request.getSession().getServletContext().setAttribute("string_", string_ + nextChar);
		return Const.STATUS_CONTINUE;
	}
}
