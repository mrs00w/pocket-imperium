package pimperium;

import java.util.*;

public class Game {
	private static Game game = null;
	private List<Player> players;
	private Ground ground;
	private static int round = 0;
	public CommandCard[] cardGameOrder;
	private List<Hex> hexes;
	
	private Game(Game game) {
		this.game = game;
		this.ground = new Ground();
        List<Player> players = new ArrayList<>();
		this.players= players;
	}

	public static Game getInstance() {
		if (game==null) {
			System.out.println();
			game = new Game(game);
		}
		return game;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public Ground getGround() { return ground; }

	public List<Player> compareOrder(int indiceCard) {
		List<Player> copie = new ArrayList<>(players);
		copie.sort(Comparator.comparingInt(player -> player.getCard(indiceCard).getPriority()));
		return copie;
		//Est-ce que c'est player ? Player ? copie ?

		// La liste est trié pour le tour
		// Il faut prendre en compte le cas où deux joueurs ont choisi la même carte
	}

	public void sustainShips() {
		
	}
	public void calculScore() {
		
	}

	public void nextRound() {
		round++;
		for (Player p : players){
			System.out.println("Le joueur " + p.name + " planifie son tour");
			p.plan();
		}
		for (int iCard = 0; iCard<3; iCard++) {
			List<Player> copie = compareOrder(iCard);
			for (Player p : copie) {
				// Il faut prendre en compte qu'on ne peut déplacer de vaisseaux si on en a pas. Normalement tout le monde devrait jouer
				// Expand en premier
				// Il faut aussi prendre en compte l'ordre des cartes.
				p.perform(iCard);
			}
		}
		sustainShips();
		calculScore();

	}
	
	public static void main(String[] args) {
		Game game = Game.getInstance();
		//Initialisation du terrain
		game.ground = new Ground();
		String player;
		//Instanciation des joueurs
		for (int i=0;i<3;i++) {
			Player p = new Player();
//			p.setPlayerName(); // Débuggage
			game.players.add(p);
		}
		while (game.round<9 || game.players.size()!=1) {
			game.nextRound();
		}
//		Map<String, Integer> tableauScores = new HashMap<>();
//		for (Player p : players){
//			tableauScores.put(p.name, Integer.valueOf(p.score));
//		}
//
//		String gagnant = null;
//		int scoreMax = 0;
//
//		for (Map.Entry<String, Integer> entry : tableauScores.entrySet()) {
//			String joueur = entry.getKey();
//			int score = entry.getValue();
//
//			if (score > scoreMax) {
//				scoreMax = score;
//				gagnant = joueur;
//			}
//		}
//		System.out.println("Félicitations, à" + gagnant + "pour avoir gagner la partie avec" + scoreMax +" points !");
	}
}
