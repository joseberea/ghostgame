package com.jmberea.ghostgame.vo;

import java.util.Map;

public class NodeVO {
	private boolean canLose = false;
	private boolean canWin = false;
	private boolean leaf;
	private int maxLength;
	Map<Character, NodeVO> children;
	
	public NodeVO() {
		super();
	}

	public NodeVO(boolean loser, boolean winner, boolean leaf, int maxLength, Map<Character, NodeVO> children) {
		super();
		this.canLose = loser;
		this.canWin = winner;
		this.leaf = leaf;
		this.maxLength = maxLength;
		this.children = children;
	}

	public boolean isCanLose() {
		return canLose;
	}

	public void setCanLose(boolean canLose) {
		this.canLose = canLose;
	}

	public boolean isCanWin() {
		return canWin;
	}

	public void setCanWin(boolean canWin) {
		this.canWin = canWin;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public Map<Character, NodeVO> getChildren() {
		return children;
	}

	public void setChildren(Map<Character, NodeVO> children) {
		this.children = children;
	}

}
