package pimperium;

import java.util.*;

public class Game {
	private static Game game = null;
	private Set<Player> players;
	private Ground ground;
	private static int round = 0;
	
	private Game(Game game) {
		this.game = game;
		Set<Player> setPlayers = new HashSet<Player>();
		this.players=setPlayers;
	}
	
	public static Game getInstance(Game game) {
		if (game==null) {
			game = new Game(game);
		}
		return game;
	}
	
	public void compareOrder(int i) {
		
	}
	public void sustainShips() {
		
	}
	public void calculScore() {
		
	}
	public void nextRound() {
		
	}
	
	public void main(String[] args) {
		getInstance(game);
		//Initialisation du terrain
		game.ground = new Ground();
		//Instanciation des joueurs
		for (int i=0;i<3;i++) {
			Player p = new Player();
			game.players.add(p);
		}
		while (game.round<9 || game.players.size()!=0) {
			game.round++;
			game.nextRound();
		}
		//fin de partie, annoncer vainqueur
	}
}
