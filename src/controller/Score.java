package controller;


public class Score implements Comparable<Score> {
	private Player player;
	private int score;
	
	public Score(Player player, int score) {
		super();
		this.player = player;
		this.score = score;
	}
	
	public String print() {
		return player.getDisplayName() + ": " + score;
	}
	
	@Override
	public int compareTo(Score other) {
		if (score > other.score) {
			return 1;
		} else if (score < other.score){
			return -1;
		}

		return 0;
	}	
}
