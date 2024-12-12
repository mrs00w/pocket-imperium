package pimperium;

import java.util.Scanner;
import java.util.*;

public class Game {
	private static Game game = null;
	private List<Player> players;
	private Ground ground;
	private static int round = 0;
	private List<Hex> hexes;
	
	private Game(Game game) {
		this.game = game;
		this.ground = new Ground();
        List<Player> players = new ArrayList<>();
		this.players= players;
	}

	public static Game getInstance() {
		if (game==null) {
			System.out.println("Création d'une partie");
			game = new Game(game);
		}
		return game;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public Ground getGround() { return ground; }

	public static int getRound() {
		return round;
	}

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

//	public Hex getHexById(int id) {
//		if (id >= 0 && id < ground.getHexes().size()) {
//			hexes = ground.getHexes();
//			return hexes.get(id);  // Retourner l'Hex à l'index correspondant à l'ID
//		}
//		return null;  // Retourner null si l'ID est invalide
//	}

	public void nextRound() {
		round++;
		for (Player p : players){
			System.out.println("Le joueur " + p.getName() + " planifie son tour");
			p.plan();
		}
		for (int iCard = 0; iCard<3; iCard++) {
			List<Player> copie = compareOrder(iCard);
			System.out.println("Au tour "+ (iCard+1)+ " l'ordre des joueurs est :");
			for (Player p : copie) {
				System.out.println(p.getName());
				// Il faut prendre en compte qu'on ne peut déplacer de vaisseaux si on en a pas. Normalement tout le monde devrait jouer
				// Expand en premier
				// Il faut aussi prendre en compte l'ordre des cartes.
				//Pour le moment on vérifie que l'ordre est correctement défini avant de perform
				//p.perform(iCard);
			}
		}
		sustainShips();
		calculScore();

	}
	
	public static void main(String[] args) {
		Game game = getInstance();
		System.out.println("Création de la partie");
		//Initialisation du terrain
		game.ground = new Ground();
		System.out.println("Création du terrain");
//		String player;
		//Instanciation des joueurs
		for (int i=0;i<3;i++) {
			Player p = new Player();
			p.setPlayerName();
			game.players.add(p);
		}
		//J'ai mis la limite à 2 juste le temps des tests
		while ((getRound() < 2) && (game.players.size() != 1)) {
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
