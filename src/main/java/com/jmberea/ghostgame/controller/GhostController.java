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
 * 		the current string length is shorter than three, we have a draw
 * 		node.
 * 
 * 	2.- winnerBranches: Stores the winner nodes. A node is winner when it
 * 		is not a draw one and is a canWin node and not a canLose one.
 * 
 * 	3.- neutralBranches: Stores the neutral nodes. Neutral node is canWin
 * 		and canLose node not stored in the winner o draw list.
 * 
 *  4.- loserBranches: Stores the loser nodes. A not winner, draw or neutral node.
 *  
 *  The ghost select a node of one of the list winnerBranches(random), neutralBranches (random),
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

	private void fillBranchPossibilities(String string_, Map<Character, NodeVO> branch_, 
			HttpServletRequest request) {
		Integer maxLength = 0;
		
		for(Entry<Character, NodeVO> entry : branch_.entrySet()) {
			NodeVO childNode = entry.getValue();
			if(childNode.isLeaf() && string_.length() < 3) {
				drawBranches.add(entry.getKey());
			} else if(childNode.isCanWin() && !childNode.isCanLose() && !(childNode.isLeaf() && string_.length() >= 3)) { // Winners
				winnerBranches.add(entry.getKey());
			} else if(childNode.isCanWin() && childNode.isCanLose() && !(childNode.isLeaf() && string_.length() >= 3)) { // Neutral
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
		if(branch_.get(branchList.get(position)).isLeaf() && string_.length() >= 3) {
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
