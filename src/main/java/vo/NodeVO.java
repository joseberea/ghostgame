package vo;

import java.util.Map;

public class NodeVO {
	private boolean loser;
	private boolean winner;
	private boolean leaf;
	private int maxLength;
	Map<Character, NodeVO> children;

	public NodeVO(boolean loser, boolean winner, boolean leaf, int maxLength, Map<Character, NodeVO> children) {
		super();
		this.loser = loser;
		this.winner = winner;
		this.leaf = leaf;
		this.maxLength = maxLength;
		this.children = children;
	}

	public boolean isLoser() {
		return loser;
	}

	public void setLoser(boolean loser) {
		this.loser = loser;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
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
