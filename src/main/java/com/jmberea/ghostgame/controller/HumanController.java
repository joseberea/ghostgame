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

/**
 * HumanController
 * Handles the human movements.
 * 
 * When the method processHumanLetter is called, it retrieves the branch
 * 	and the current string stored in the servletContext.
 * 
 * If the branch does not contains the character given by the human player
 * 	returns the STATUS_NOT_EXISTS status because the resultant string is not
 * 	in the dictionary.
 * 
 * If the branch contains the character there are have three possibilities:
 * 
 * 	1.- The current string is a word shorter than three and the node has no 
 * 		children. Then we have the STATUS_DRAW status, because there are no
 * 		more movements.
 * 
 *	2.- The current string is a word bigger than three and the node is a 
 *		leaf. Then we have the STATUS_IS_A_WORD status and the player loses
 *		because the string matches a word.
 *
 *	3.- The current string does not matches the first or second point. Then 
 *		the game continues with the STATUS_CONTINUE status.
 */

@Controller
public class HumanController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/humanLetter.htm", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Integer processHumanLetter(HttpServletRequest request, @RequestParam("nextChar") Character nextChar) {
		logger.debug("Human plays character " + nextChar);
		String string_ = request.getSession().getServletContext().getAttribute(Const.STRING_CTX_NAME).toString();
		logger.debug("New string: " + string_ + nextChar);
		Map<Character, NodeVO> branch_ = (Map<Character, NodeVO>) request.getSession().getServletContext().getAttribute(Const.BRANCH_CTX_NAME);
		Object status = null;
		if(branch_.containsKey(nextChar)) {
			status = branch_.get(nextChar);
			if(status instanceof NodeVO) {
				if(((NodeVO) status).getChildren().isEmpty() && string_.length() < 3) {
					logger.debug("DRAW STATUS");
					return Const.STATUS_DRAW;
				} if(((NodeVO) status).isLeaf() && string_.length() >= 3) {
					logger.debug("WORD STATUS");
					return Const.STATUS_IS_A_WORD;
				} else {
					setRequestAttribute(request, Const.BRANCH_CTX_NAME, ((NodeVO) status).getChildren());
				}
			} else {
				setRequestAttribute(request, Const.BRANCH_CTX_NAME, branch_.get(nextChar));
			}
		} else {
			logger.debug("NOT EXISTS STATUS");
			return Const.STATUS_NOT_EXISTS;
		}
		setRequestAttribute(request, Const.STRING_CTX_NAME, string_ + nextChar);
		logger.debug("CONTINUE STATUS");
		return Const.STATUS_CONTINUE;
	}
	
	private void setRequestAttribute(HttpServletRequest request, String name, Object value) {
		request.getSession().getServletContext().setAttribute(name, value);
	}
}
