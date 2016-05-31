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

@Controller
public class GhostController {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ghostLetter.htm", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody GhostResponseVO getGhostLetter(HttpServletRequest request) {
		GhostResponseVO ghostResponse;
		List<Character> winnerBranches = new ArrayList<Character>();
		List<Character> neutralBranches = new ArrayList<Character>();
		List<Character> loserBranches = new ArrayList<Character>();
		List<Character> drawBranches = new ArrayList<Character>();
		Map<Character, NodeVO> branch_ = (Map<Character, NodeVO>) request.getSession().getServletContext().getAttribute("branch_");
		String string_ = request.getSession().getServletContext().getAttribute("string_").toString();
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
		
		if(!winnerBranches.isEmpty()) {
			ghostResponse = setStatus(branch_, winnerBranches, Const.STATUS_CONTINUE, string_, request);
		} else if(!neutralBranches.isEmpty()) {
			ghostResponse = setStatus(branch_, neutralBranches, Const.STATUS_CONTINUE, string_, request);
		} else if(!loserBranches.isEmpty()) {
			ghostResponse = setLoserStatus(branch_, loserBranches, string_, request);
		} else {
			ghostResponse = setStatus(branch_, drawBranches, Const.STATUS_DRAW, string_, request);
		}
		
		return ghostResponse;
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
		request.getSession().getServletContext().setAttribute("branch_", branch_);
		request.getSession().getServletContext().setAttribute("string_", string_ + nextChar);
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
		ghostResponse.setLetter(nextChar);
		if(branch_.get(branchList.get(position)).isLeaf() && string_.length() >= 3) {
			ghostResponse.setStatus(Const.STATUS_IS_A_WORD);
		} else {
			branch_ = branch_.get(branchList.get(position)).getChildren();
			ghostResponse.setStatus(Const.STATUS_CONTINUE);		
			request.getSession().getServletContext().setAttribute("branch_", branch_);
			
		}
		request.getSession().getServletContext().setAttribute("string_", string_ + nextChar);
		return ghostResponse;
	}
	
	
}
