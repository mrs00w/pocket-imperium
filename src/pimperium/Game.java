package pimperium;

import gui.Controller;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Game {
	private static Game game = null;
	private final List<Player> players;
	private Ground ground;
	private static int round = 0;
	private SectorCard centralCard;
	private Controller controller;

	private Game() {
		this.ground = new Ground();
		this.players = new ArrayList<>();
		this.centralCard = ground.findCentralCard(ground.getSectors());
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}

	public static Game getInstance() {
		if (game == null) {
			System.out.println("Création d'une partie");
			game = new Game();
		}
		return game;
	}

	public void calculScore() {
		System.out.println("Calcul des scores de chaque joueur pour le round");
		//Demande à chaque joueur de choisir une sectorCard
		//Les joueurs n'ont pas droit de choisir une sectorCard qui a déjà été choisie
		//Le joueur qui contrôle le TriPrime a le droit de sélectionner une sectorCard de plus
		//Personne ne peut sélectionner le TriPrime
		//Pour chaque sectorCard, si des systemes sont controlés, le joueur qui le controle gagne autant de point
		// que le niveau du systeme (peut importe qui a choisi la carte)
		Set<SectorCard> chosenSectors = new HashSet<>();
		Map<Player, SectorCard> playerChoices = new HashMap<>();
		Player triPrimeController = getTriPrimeController();

		for (Player player : players) {
			SectorCard chosenSector = chooseSector(player, chosenSectors);
			if (chosenSector != null) {
				chosenSectors.add(chosenSector);
				playerChoices.put(player, chosenSector);
			}
		}

		if (triPrimeController != null) {
			SectorCard additionalSector = chooseSector(triPrimeController, chosenSectors);
			if (additionalSector != null) {
				chosenSectors.add(additionalSector);
			}
		}

		// Calculer les scores
		for (Map.Entry<Player, SectorCard> entry : playerChoices.entrySet()) {
			Player player = entry.getKey();
			SectorCard sectorCard = entry.getValue();
			int points = calculateSectorPoints(player, sectorCard);
			player.addPoints(points);
		}

		if (triPrimeController != null) {
			for (SectorCard extraSector : chosenSectors) {
				if (triPrimeController.controlsSector(extraSector)) {
					int points = calculateSectorPoints(triPrimeController, extraSector);
					triPrimeController.addPoints(points);
				}
			}
		}
	}

	private Player getTriPrimeController() {
		for (Player player : players) {
			if (player.controlsSector(centralCard)) {
				return player;
			}
		}
		return null;
	}

	private SectorCard chooseSector(Player player, Set<SectorCard> chosenSectors) {
		System.out.println("Quel secteur voulez-vous choisir ? (Entrez l'ID de position sur la carte)");
		Scanner reader = new Scanner(System.in);
		int idPositionOnMap = reader.nextInt();

		SectorCard sector = ground.getSectorByPositionOnMap(idPositionOnMap);
			// Vérifie si le secteur est valide
			if (sector != null && sector.getPositionOnMap() == idPositionOnMap) { // Vérifie que l'ID correspond
				if (!chosenSectors.contains(sector) && sector != centralCard && player.controlsSector(sector)) {
					return sector; // Retourne le secteur si toutes les conditions sont respectées
				} else {
					System.out.println("Ce secteur ne peut pas être choisi (déjà choisi, central, ou non contrôlé par le joueur).");
					return null; // Optionnel : Si le secteur n'est pas valide
				}
			}
		System.out.println("Aucun secteur correspondant à cet ID n'a été trouvé.");
		return null; // Si aucun secteur valide n'est trouvé
	}

	private int calculateSectorPoints(Player player, SectorCard sectorCard) {
		int points = 0;
		for (Hex hex : sectorCard.getHexes()) {
			if (player.controlsHex(hex)) {
				points += hex.getLevelSystem();
			}
		}
		return points;
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

		//ça ça n'arrivera jamais vu qu'on l'utilise qu'au moment de l'initialisation (pour le moment)
//		if (availableHexes.isEmpty()) {
//			System.out.println("Aucun hex disponible pour le placement des vaisseaux.");
//			return;
//		}

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

		for (int i=0; i<2; i++) {//On prend 2 vaisseaux hors du plateau et on les place sur l'Hex choisi
			player.getShipsHorsPlateau().peek().setPosition(targetHex); //positionne un vaisseau au niveau de l'hexagone cible
			player.getShipsSurPlateau().add(player.getShipsHorsPlateau().peek()); //On ajoute le nouveau vaisseau à la liste des vaisseaux situés sur le plateau
			targetHex.getShips().add(player.getShipsHorsPlateau().peek());
			int shipCount = targetHex.getShips().size();
			controller.updateHexLabel(targetHex.getIdHex(), shipCount, player);
			player.getShipsHorsPlateau().pop();
			// Définir le joueur comme occupant du système
			targetHex.setCurrentOccupant(player);
			player.getHexesOccupes().add(targetHex);
		}
//		for (Ship s : targetHex.getShips()){
//			System.out.println(s.toString());
//		}

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
			System.out.println("Au tour "+ (iCard+1)+ " l'ordre des joueurs est : ");
			for (Player p : copie) {
				System.out.print(p.getName() +" ");
				//Romain : Il faut prendre en compte qu'on ne peut déplacer de vaisseaux si on en a pas. Normalement tout le monde devrait jouer
				//Laora : Si on n'en a pas sur la carte on meurt, donc le cas où il n'y a pas de vaisseau n'existe pas
				//p.perform(iCard);
			}
			System.out.println("");
			for (Player p : copie) {
//				CommandCard carteJouee = p.getCard(iCard);
//				int indiceCarte = iCard;
//				int capaciteCarte = (int) players.stream()
//						.filter(player -> p.getCard(indiceCarte).equals(carteJouee))
//						.count(); //On compte le nombre de joueurs ayant choisi la même carte que le joueur en train de jouer
				p.perform(iCard);
			}
		}
		sustainShips();
		calculScore();
		updatePriority(); //Le marqueur "Premier Joueur" passe au joueur suivant
	}

	private void updatePriority() {
		for (Player p: players){
			p.updatePrio();
		}
	}

//	public static void main(String[] args) {
	public void setupGame() {
		Game game = getInstance();
		System.out.println("Création de la partie");
		//Initialisation du terrain
		game.ground = new Ground();
		System.out.println("Création du terrain");
		//game.ground.setupGround(); // En vrai on peut tout mettre dans le constructeur direct //du coup je l'ai mis dans le constructeur
//		String player;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Combien de joueurs humains ?");
		int humanPlayers = scanner.nextInt();

		System.out.println("Combien de bots ?");
		int botPlayers = scanner.nextInt();

		// Ajouter les joueurs humains
		for (int i = 0; i < humanPlayers; i++) {
			Player player = new Player();
			player.setPlayerName(); // Demander un nom
			players.add(player);
		}

		// Ajouter les bots
		for (int i = 0; i < botPlayers; i++) {
			players.add(new Bot());
		}

		players.get(0).setColor(Color.RED);
		players.get(1).setColor(Color.GREEN);
		players.get(2).setColor(Color.BLUE);

		//Maintenant que la map et les joueurs sont créés on peut initialiser le terrain
		System.out.println("Initialisation du terrain");
		game.initialShipDeployment();
		//J'ai mis la limite à 2 juste le temps des tests
		while ((getRound() < 2) && (game.players.size() != 1)) {
			game.nextRound();
		}

// Pour calculer les scores de victoire à la fin
		Map<String, Integer> tableauScores = new HashMap<>();
		for (Player p : game.players){
			tableauScores.put(p.getName(), Integer.valueOf(p.getScore()));
		}

		String gagnant = null;
		int scoreMax = 0;

		for (Map.Entry<String, Integer> entry : tableauScores.entrySet()) {
			String joueur = entry.getKey();
			int score = entry.getValue();

			if (score > scoreMax) {
				scoreMax = score;
				gagnant = joueur;
			}
		}
		System.out.println("Félicitations, à" + gagnant + "pour avoir gagner la partie avec" + scoreMax +" points !");
	}
}
