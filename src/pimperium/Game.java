package pimperium;

import java.util.Scanner;
import java.util.*;

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
		copie.sort(Comparator.comparingInt((Player player) -> player.getCard(indiceCard).getPriority())
				.thenComparingInt(Player::getPriority)); // Comparer la priorité des joueurs en cas d'égalité);
		return copie;
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
		//Demande à chaque joueur de choisir une sectorCard
		//Les joueurs n'ont pas droit de choisir une sectorCard qui a déjà été choisie
		//Le joueur qui contrôle le TriPrime a le droit de sélectionner une sectorCard de plus
		//Personne ne peut sélectionner le TriPrime
		//Pour chaque sectorCard, si des systemes sont controlés, le joueur qui le controle gagne autant de point que le niveau du systeme (peut importe qui a choisi la carte)
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
		//Comment vérifier que le secteur est libre ?
		List<Hex> availableHexes = ground.getHexes().stream()
				.filter(hex -> hex.getLevelSystem() == 1 && hex.getCurrentOccupant() == null)
				.toList();

		if (availableHexes.isEmpty()) {
			System.out.println("Aucun hex disponible pour le placement des vaisseaux.");
			return;
		}

		// Sélectionner un hex disponible pour le placement

		Scanner reader = new Scanner(System.in);
		Hex targetHex = null;
		// Il faut mettre des do partout pour permettre aux joueurs de refaire leurs actions
		//j'ai inversé le do et le while
		while (targetHex == null) {
			System.out.println(STR."\{player.getName()}, où voulez-vous placer vos vaisseaux ? (Entrez l'ID d'un Hex disponible)");

			if (!reader.hasNextInt()) {//On vérifie que c'est bien un int qui a été entré
				System.out.println("Entrée invalide, veuillez entrer un entier.");
				reader.next(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
				continue; // Recommence la boucle pour demander un autre ID
			}

			int idHex = reader.nextInt();

			targetHex = availableHexes.stream()
					.filter(hex -> hex.getIdHex() == idHex)
					.findFirst()
					.orElse(null);

			if (targetHex == null) {
				System.out.println("Erreur : Aucun Hex disponible avec cet ID. Réessayez.");
			}
		}

// Une fois sorti, targetHex est valide

		System.out.println("Hex sélectionné : " + targetHex);
//
//		// Place 2 vaisseaux du joueur
		Stack<Ship> shipsToDeploy = player.getShipsHorsPlateau();
//				.stream()
//				.limit(2)
//				.toList();

		if (shipsToDeploy.size() < 2) {
			System.out.println(STR."Le joueur \{player.getName()} n'a pas assez de vaisseaux pour le placement.");
			return;
		}

		shipsToDeploy.peek().setPosition(targetHex);
		//On ajoute le nouveau vaisseau à la liste des vaisseaux situés sur le plateau
		player.getShipsSurPlateau().add(shipsToDeploy.peek());
		shipsToDeploy.pop();

//		for (Ship ship : shipsToDeploy) {
//			targetHex.addShip(ship);
//			player.getShipsHorsPlateau().remove(ship);
//			player.getShipsSurPlateau().add(ship);
//		}

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
			System.out.println("Au tour "+ (iCard+1)+ " l'ordre des joueurs est :");
			for (Player p : copie) {
				System.out.println(p.getName());
				// Il faut prendre en compte qu'on ne peut déplacer de vaisseaux si on en a pas. Normalement tout le monde devrait jouer
				//Si on n'en a pas sur la carte on meurt nan ?
				//p.perform(iCard);
			}
		}
		//sustainShips();
		//calculScore();
		updatePriority();

	}

	private void updatePriority() {
		for (Player p: players){
			p.updatePrio();
		}
	}

	public static void main(String[] args) {
		Game game = getInstance();
		System.out.println("Création de la partie");
		//Initialisation du terrain
		game.ground = new Ground();
		System.out.println("Création du terrain");
		//game.ground.setupGround(); // En vrai on peut tout mettre dans le constructeur direct //du coup je l'ai mis dans le constructeur
//		String player;
		//Instanciation des joueurs
		for (int i = 0; i < 3; i++) {
			Player p = new Player();
			p.setPlayerName();
			game.players.add(p);
		}
		//Maintenant que la map et les joueurs sont créés on peut initialiser le terrain
		System.out.println("Initialisation du terrain");
		game.initialShipDeployment();
		//J'ai mis la limite à 2 juste le temps des tests
		while ((getRound() < 2) && (game.players.size() != 1)) {
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
