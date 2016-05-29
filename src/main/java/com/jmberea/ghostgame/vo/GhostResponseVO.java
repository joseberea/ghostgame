package com.jmberea.ghostgame.vo;

public class GhostResponseVO {
	private Character letter;
	private Integer status;
	
	public GhostResponseVO() {
		super();
	}
	public Character getLetter() {
		return letter;
	}
	public void setLetter(Character letter) {
		this.letter = letter;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
