package pimperium;

import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
	private static Game game = null;
	private List<Player> players;
	private Ground ground;
	private static int round = 0;

	private Game() {
		this.ground = new Ground();
		List<Player> players = new ArrayList<>();
		this.players = players;
	}

	public static Game getInstance() {
		if (game == null) {
			System.out.println("Création d'une partie");
			game = new Game();
		} else {
			System.out.println("Instance existante de la partie récupérée");
		}
		return game;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public Ground getGround() {
		return ground;
	}

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
		// On suppose que "ground" est l'objet contenant tous les hexes
		for (Hex hex : ground.getHexes()) {
			// Calculer la capacité maximale de vaisseaux que ce hex peut soutenir
			int maxShips = 1 + hex.getLevelSystem();  // 1 + niveau du système

			// Obtenir les vaisseaux présents sur ce hex
			List<Ship> shipsOnHex = hex.getShips();

			// Si le nombre de vaisseaux dépasse la capacité maximale, on retire les excédentaires
			if (shipsOnHex.size() > maxShips) {
				// Calculer combien de vaisseaux doivent être retirés
				int excessShips = shipsOnHex.size() - maxShips;

				// On parcourt les vaisseaux excédentaires et on les retire
				for (int i = 0; i < excessShips; i++) {
					Ship shipToRemove = shipsOnHex.get(shipsOnHex.size() - 1 - i);  // Retirer les vaisseaux du fond de la liste
					hex.getShips().remove(shipToRemove);  // Retirer le vaisseau de l'hex
					// Retourner le vaisseau à l'approvisionnement (ou le stockage)
					hex.getCurrentOccupant().getShipsHorsPlateau().add(shipToRemove);  // Assurez-vous que game.getSupply() existe et fonctionne
				}

				System.out.println(STR."\{excessShips} vaisseaux excédentaires ont été retirés de l'hex \{hex.getIdHex()}");
			}
		}
	}

	public void calculScore() {
		System.out.println("Calcul des scores de chaque joueur pour le round");
		// Ne peux pas encore être fait car version sans carte
		// On peut faire une version avec les Hex seuls à la limite
	}

	public void initialShipDeployment() {
		List<Player> players = game.getPlayers();
		Ground ground = game.getGround();

		// Étape 1 : Chaque joueur place 2 vaisseaux dans un système de niveau 1 non occupé.
		for (int i = 0; i < players.size(); i++) {
			Player currentPlayer = players.get(i);
			placeShips(currentPlayer, ground);
		}

		// Étape 2 : Les joueurs placent à nouveau 2 vaisseaux dans l'ordre inverse.
		for (int i = players.size() - 1; i >= 0; i--) {
			Player currentPlayer = players.get(i);
			placeShips(currentPlayer, ground);
		}
	}

	private void placeShips(Player player, Ground ground) {
		// Obtenez tous les systèmes de niveau 1 non occupés dans des secteurs non occupés
		List<Hex> availableHexes = game.getGround().getHexes().stream()
				.filter(hex -> hex.getLevelSystem() == 1 && hex.getCurrentOccupant() == null)
				.toList();

		if (availableHexes.isEmpty()) {
			System.out.println("Aucun hex disponible pour le placement des vaisseaux.");
			return;
		}

		// Sélectionner un hex disponible pour le placement
		
		Scanner reader = new Scanner(System.in);
		Hex targetHex;
		// Il faut mettre des do partout pour permettre aux joueurs de refaire leurs actions
		do {
			System.out.println("Où voulez-vous placer vos vaisseaux ? (Entrez l'ID d'un Hex disponible)");
			int idHex = reader.nextInt();

			targetHex = availableHexes.stream()
					.filter(hex -> hex.getIdHex() == idHex)
					.findFirst()
					.orElse(null);

			if (targetHex == null) {
				System.out.println("Erreur : Aucun Hex disponible avec cet ID. Réessayez.");
			}
		} while (targetHex == null);

// Une fois sorti, targetHex est valide
		
		System.out.println("Hex sélectionné : " + targetHex);

		// Place 2 vaisseaux du joueur
		List<Ship> shipsToDeploy = player.getShipsHorsPlateau().stream()
				.limit(2)
				.toList();

		if (shipsToDeploy.size() < 2) {
			System.out.println(STR."Le joueur \{player.getName()} n'a pas assez de vaisseaux pour le placement.");
			return;
		}

		for (Ship ship : shipsToDeploy) {
			targetHex.addShip(ship);
			player.getShipsHorsPlateau().remove(ship);
			player.getShipsSurPlateau().add(ship);
		}

		// Définir le joueur comme occupant du système
		targetHex.setCurrentOccupant(player);

		System.out.println(STR."\{player.getName()} a placé 2 vaisseaux sur le système \{targetHex.getIdHex()}");
	}

	public void nextRound() {
		round++;
		for (Player p : players) {
			System.out.println(STR."Le joueur \{p.getName()} planifie son tour");
			p.plan();
		}
		for (int iCard = 0; iCard < 3; iCard++) {
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
		Game game = getInstance();
		System.out.println("Création de la partie");
		//Initialisation du terrain
		game.ground = new Ground();
		System.out.println("Création du terrain");
		game.getGround().setupGround(); // En vrai on peut tout mettre dans le constructeur direct
		System.out.println("Initialisation du terrain");
		game.initialShipDeployment();
		System.out.println("On place les vaisseaux pour commencer");
//		String player;
		//Instanciation des joueurs
		for (int i = 0; i < 3; i++) {
			Player p = new Player();
//			p.setPlayerName(); // Débuggage
			game.players.add(p);
		}
		//On lit l'ensemble des joueurs pour vérifier qu'ils ont bien été créés
		for (Player player : game.players) {
			System.out.println(player.toString());
		}

		while ((getRound() < 9) && (game.players.size() != 1)) {
			game.nextRound();
		}

// Pour calculer les scores de victoire à la fin
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
