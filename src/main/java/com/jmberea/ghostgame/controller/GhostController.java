package com.jmberea.ghostgame.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jmberea.ghostgame.util.Const;
import com.jmberea.ghostgame.vo.GhostResponseVO;
import com.jmberea.ghostgame.vo.NodeVO;

/**
 * HumanController
 * Handles the ghost movements.
 * 
 * When the method getGhostLetter is called, it retrieves the branch
 * 	and the current string stored in the servletContext.
 * 
 * The ghost evaluates every child possibility of the branch according
 * 	to the current string and stores them into a four different list
 * 
 * 	1.- drawBranches: Stores the draw nodes. If the node is a leaf and
 * 		the current string length is shorter than WORD_LIMIT-1, we have a draw
 * 		node.
 * 
 * 	2.- winnerBranches: Stores the winner nodes. A node is a winner when it
 * 		is not a draw one and is a canWin node and not a canLose one.
 * 
 * 	3.- neutralBranches: Stores the neutral nodes. Neutral node is canWin
 * 		and canLose node and not stored in the winner or draw list.
 * 
 *  4.- loserBranches: Stores the loser nodes. A non winner, draw or neutral node.
 *  
 *  The ghost selects a node of one of the list winnerBranches(random), neutralBranches (random),
 *   drawBranches (random) or loserBranches (selecting the larger string) using this order.
 */

@Controller
public class GhostController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);
	
	private List<Character> winnerBranches;
	private List<Character> neutralBranches;
	private List<Character> loserBranches;
	private List<Character> drawBranches;
	
	@RequestMapping(value = "/ghostLetter.htm", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GhostResponseVO getGhostLetter(HttpServletRequest request) {
		return getGhostResponse(request);
	}
	
	/**
	 * Initialize the lists and returns the GhostResponseVO
	 * @param request HttpServletRequest object
	 * @return The GhostResponseVO object with the selected letter and the resultant status
	 */
	@SuppressWarnings("unchecked")
	private GhostResponseVO getGhostResponse(HttpServletRequest request) {
		String string_ = request.getSession().getServletContext().getAttribute(Const.STRING_CTX_NAME).toString();
		Map<Character, NodeVO> branch_ = (Map<Character, NodeVO>) request.getSession().getServletContext().getAttribute(Const.BRANCH_CTX_NAME);
		winnerBranches = new ArrayList<Character>();
		neutralBranches = new ArrayList<Character>();
		loserBranches = new ArrayList<Character>();
		drawBranches = new ArrayList<Character>();
		
		fillBranchPossibilities(string_, branch_, request);
		if(!winnerBranches.isEmpty()) {
			return setStatus(branch_, winnerBranches, Const.STATUS_CONTINUE, string_, request);
		} else if(!neutralBranches.isEmpty()) {
			return setStatus(branch_, neutralBranches, Const.STATUS_CONTINUE, string_, request);
		} else if(!drawBranches.isEmpty()){
			return setStatus(branch_, drawBranches, Const.STATUS_DRAW, string_, request);
		} else {
			return setLoserStatus(branch_, loserBranches, string_, request);
		} 
	}

	
	/**
	 * Fills the lists according with the strategy of the ghost
	 * @param string_ The current string
	 * @param branch_ The current branch map
	 * @param request HttpServletRequest object
	 */
	private void fillBranchPossibilities(String string_, Map<Character, NodeVO> branch_, 
			HttpServletRequest request) {
		Integer maxLength = 0;
		
		for(Entry<Character, NodeVO> entry : branch_.entrySet()) {
			NodeVO childNode = entry.getValue();
			if(childNode.isLeaf() && string_.length() < Const.WORD_LIMIT-1) {
				drawBranches.add(entry.getKey());
			} else if(childNode.isCanWin() && !childNode.isCanLose() && !(childNode.isLeaf() && string_.length() >= Const.WORD_LIMIT-1)) { // Winners
				winnerBranches.add(entry.getKey());
			} else if(childNode.isCanWin() && childNode.isCanLose() && !(childNode.isLeaf() && string_.length() >= Const.WORD_LIMIT-1)) { // Neutral
				neutralBranches.add(entry.getKey());
			} else if(winnerBranches.isEmpty()) { // Losers
				if(loserBranches.isEmpty() || childNode.getMaxLength() == maxLength || maxLength == 0) {
					loserBranches.add(entry.getKey());
				} else if(childNode.getMaxLength() > maxLength) {
					loserBranches.clear();
					loserBranches.add(entry.getKey());
				} // Discard shorter loser branches
			}
		}
	}

	
	/**
	 * Set the status and letter randomly
	 * @param branch_ The current branch
	 * @param branchList The selected list (4 possibilities)
	 * @param status The resultant status
	 * @param string_ The current string
	 * @param request HttpServletRequest object
	 * @return GhostResponseVO object with the selected letter and the resultant status
	 */
	private GhostResponseVO setStatus(Map<Character, NodeVO> branch_, List<Character> branchList, 
			Integer status, String string_, HttpServletRequest request) {
		GhostResponseVO ghostResponse = new GhostResponseVO();
		Random randomGenerator = new Random();
		Character nextChar;
		Integer position;
		
		position = randomGenerator.nextInt(branchList.size());
		branch_ = branch_.get(branchList.get(position)).getChildren();
		nextChar = branchList.get(position);
		
		ghostResponse.setLetter(nextChar);
		ghostResponse.setStatus(status);
		
		request.getSession().getServletContext().setAttribute(Const.BRANCH_CTX_NAME, branch_);
		request.getSession().getServletContext().setAttribute(Const.STRING_CTX_NAME, string_ + nextChar);
		logger.debug("Ghost plays character " + ghostResponse.getLetter());
		return ghostResponse;
	}
	
	
	/**
	 * Set the status and letter selecting the larger string
	 * @param branch_ The current branch
	 * @param branchList The selected list (4 possibilities)
	 * @param status The resultant status
	 * @param string_ The current string
	 * @param request HttpServletRequest object
	 * @return GhostResponseVO object with the selected letter and the resultant status
	 */
	private GhostResponseVO setLoserStatus(Map<Character, NodeVO> branch_, List<Character> branchList, 
			String string_, HttpServletRequest request) {
		GhostResponseVO ghostResponse = new GhostResponseVO();
		Random randomGenerator = new Random();
		Character nextChar;
		Integer position;
		position = randomGenerator.nextInt(branchList.size());
		nextChar = branchList.get(position);
		logger.debug("Ghost plays character " + ghostResponse.getLetter());
		ghostResponse.setLetter(nextChar);
		if(branch_.get(branchList.get(position)).isLeaf() && string_.length() >= Const.WORD_LIMIT-1) {
			logger.debug("WORD STATUS");
			ghostResponse.setStatus(Const.STATUS_IS_A_WORD);
		} else {
			branch_ = branch_.get(branchList.get(position)).getChildren();
			logger.debug("CONTINUE STATUS");
			ghostResponse.setStatus(Const.STATUS_CONTINUE);		
			request.getSession().getServletContext().setAttribute(Const.BRANCH_CTX_NAME, branch_);
		}
		request.getSession().getServletContext().setAttribute(Const.STRING_CTX_NAME, string_ + nextChar);
		return ghostResponse;
	}
	
	
}
