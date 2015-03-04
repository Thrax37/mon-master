package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import players.Felyne;
import players.Koneko;
import players.Lancer;
import players.Melynx;
import players.Palico;
import players.RedViper;
import players.RoranStronghammer;
import players.TheKingsJester;
import players.WeaselWill;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

public class Tournament {

	public static final int GAMES = 10;
	
	public static Player[] players = {
		new RoranStronghammer(),
		new RedViper(),
		new WeaselWill(),
		new TheKingsJester(),
		new Lancer(),
		new Helper(),
		new Helper(),
		new Helper()
	};
	
	private static List<Player> helpers = new ArrayList<>(Arrays.asList(
			new Felyne(),
			new Melynx(),
			new Palico(),
			new Koneko()
	));
	
	public static void main(String[] args) {
		
		// Adding random helpers to make teams
		Random r = new Random();
		for (int i = 0; i < players.length; i++) { 
			if (players[i].getClass().equals(Helper.class)) {
				int p = r.nextInt(helpers.size());
				players[i] = helpers.get(p);
				helpers.remove(p);
			}
		}
		
		List<List<Score>> scores = new ArrayList<List<Score>>();
		
		System.out.println("****************************");
		System.out.println("*     TOURNAMENT START     *");
		System.out.println("****************************");
		System.out.println();
		
		for (int i = 1; i <= GAMES; i++) {
			Game game = new Game(players);
			scores.add(game.run());
		}
		
		System.out.println();
		System.out.println("****************************");
		System.out.println("*     TOURNAMENT  END      *");
		System.out.println("****************************");
	
		Map<Player, Double> scoreByPlayer = scores.stream().flatMap(List::stream).collect(groupingBy(Score::getPlayer, averagingInt(Score::getScore)));
		List<Score> finalScores = new ArrayList<Score>(); 
		scoreByPlayer.forEach((p, s) -> finalScores.add(new Score(p, s.intValue())));
		
		Collections.sort(finalScores, Collections.reverseOrder());
		
		for (int i = 0; i < finalScores.size(); i++) {
			Score score = finalScores.get(i);
			System.out.println(i+1 + ". " + score.print());
		}
	}
}
