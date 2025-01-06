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
		this.centralCard = ground.getSectorById(4);
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

	public void updateControlledSectors() {
		// Parcours de tous les hexagones de la carte
		for (Hex hex : ground.getHexes()) {
			// Récupération du joueur occupant ce hex
			Player occupant = hex.getCurrentOccupant();

			// Vérification : si un occupant existe, on met à jour ses secteurs contrôlés
			if (occupant != null) {
				SectorCard sector = hex.getSector(); // Récupération du secteur du hex
				if (sector != null) {
					occupant.addControlledSector(sector); // Ajoute le secteur au joueur
				}
			}
		}
	}

	public void clearControlledSectors() {
		for (Player p : Game.getInstance().getPlayers()) {
			p.getControlledSectors().clear();
		}
	}

	public void calculScore() {
		System.out.println("Calcul des scores de chaque joueur pour le round");

		//on va bazarder c'te partie :

//		clearControlledSectors();
//		updateControlledSectors();
		//Demande à chaque joueur de choisir une sectorCard
		//Les joueurs n'ont pas droit de choisir une sectorCard qui a déjà été choisie
		//Le joueur qui contrôle le TriPrime a le droit de sélectionner une sectorCard de plus
		//Personne ne peut sélectionner le TriPrime
		//Pour chaque sectorCard, si des systemes sont controlés, le joueur qui le controle gagne autant de point
		// que le niveau du systeme (peut importe qui a choisi la carte)
		Set<SectorCard> chosenSectors = new HashSet<>();
//		Map<Player, SectorCard> playerChoices = new HashMap<>();
		Player triPrimeController = ground.getTriPrimeOccupant();

		for (Player player : players) {

            //Ma partie
//            SectorCard chosenSector=null;
  //          while(chosenSector==null) {
    //            chosenSector = chooseSector(player, chosenSectors);
    //        }
    //        chosenSectors.add(chosenSector);
    //        playerChoices.put(player, chosenSector);
//          }

            SectorCard chosenSector;
//			if (player instanceof Bot) {
//				chosenSector = chooseSectorBot((Bot) player, chosenSectors);
//                chosenSectors.add(chosenSector);
//                playerChoices.put(player, chosenSector);
//            } else {
			chosenSector = chooseSector(player, chosenSectors);
			chosenSectors.add(chosenSector);
//				playerChoices.put(player, chosenSector);
			for (Hex hex : chosenSector.getHexes()) {
				if (hex.getLevelSystem() > 0 && hex.getCurrentOccupant() != null) {
					hex.getCurrentOccupant().addPoints(hex.getLevelSystem());
					System.out.println(hex.getCurrentOccupant().getName()+" a gagné "+hex.getLevelSystem()+" points.");
				}
			}
//			}
		}

		if (triPrimeController != null) {
			SectorCard additionalSector;
            if (triPrimeController instanceof Bot) {
                additionalSector = chooseSectorBot((Bot) ground.getTriPrimeOccupant(), chosenSectors);
//                chosenSectors.add(additionalSector);
            } else {
				additionalSector = chooseSector(ground.getTriPrimeOccupant(), chosenSectors);
//				chosenSectors.add(additionalSector);
			}
			for (Hex hex : additionalSector.getHexes()){
				if (hex.getLevelSystem()>0 && hex.getCurrentOccupant()!=null){
					hex.getCurrentOccupant().addPoints(hex.getLevelSystem());
					System.out.println(hex.getCurrentOccupant().getName()+" a gagné "+hex.getLevelSystem()+" points.");
				}
			}
        }

		// Calculer les scores
//		for (Map.Entry<Player, SectorCard> entry : playerChoices.entrySet()) {
//			Player player = entry.getKey();
//			SectorCard sectorCard = entry.getValue();
//			calculateSectorPoints(sectorCard);
//		}

//		if (triPrimeController != null) {
//			for (SectorCard extraSector : chosenSectors) {
//				if (triPrimeController.controlsSector(extraSector)) {
//					calculateSectorPoints(extraSector);
//				}
//			}
//		}
	}

	public Player getTriPrimeController() {
		for (Player player : players) {
			if (player.controlsSector(centralCard)) {
				return player;
			}
		}
		return null;
	}

	public SectorCard chooseSector(Player player, Set<SectorCard> chosenSectors) {
		if(player instanceof Bot) {
			while (true) {
				Random random = new Random();
				int randomId = random.nextInt(10);

				List<SectorCard> filteredSectors = ground.getSectors().stream()
						.filter(sector -> sector.equals(ground.getSectorById(randomId)) && !chosenSectors.contains(sector))
						.toList();
				if (filteredSectors.isEmpty() || randomId == 4) {
					continue;
				}
				System.out.println(player.getName() + " a choisi le secteur "+randomId);
				return ground.getSectorById(randomId);
			}
		}
//			List<Integer> sectorIdControlledByBot = new ArrayList<>();
//
//			int idSectorById = random.nextInt(9);
//
//			for (SectorCard sector: player.getControlledSectors()) {
//				sectorIdControlledByBot.add(sector.getId());
//			}
//
//			int sectorIdChoosen = sectorIdControlledByBot.get(random.nextInt(sectorIdControlledByBot.size()));
//
//			for (SectorCard sector: player.getControlledSectors()) {
//				if (sector.getId() == sectorIdChoosen && sector != centralCard) {
//					sectorChoosen = sector;
//				} else {
//					chooseSector(player, chosenSectors);
//				}
//			}
//		}
		while (true) {
			System.out.println("Quel secteur voulez-vous choisir " + player.getName() + "? (Entrez l'ID de position sur la carte)");
			Scanner reader = new Scanner(System.in);
			int idSectorById = reader.nextInt();

			List<SectorCard> filteredSectors = ground.getSectors().stream()
					.filter(sector -> sector.equals(ground.getSectorById(idSectorById)) && !chosenSectors.contains(sector))
					.toList();
			if (filteredSectors.isEmpty() || idSectorById==5) {
				System.out.println("Le secteur choisi n'est pas valide");
				continue;
			}

			return ground.getSectorById(idSectorById);

//		if (player.getControlledSectors().contains(sector)) {
//			System.out.println("Le joueur est bien présent dans ce secteur " + idSectorById);
//		}

			// Vérifie si le secteur est valide
//		if (sector != null && sector.getId() == idSectorById) { // Vérifie que l'ID correspond
//			if (!chosenSectors.contains(sector) && sector != centralCard && player.controlsSector(sector)) {
//				return sector; // Retourne le secteur si toutes les conditions sont respectées
//			} else {
//				System.out.println("Ce secteur ne peut pas être choisi (déjà choisi, central, ou non contrôlé par le joueur).");
//				chooseSector(player, chosenSectors); // Optionnel : Si le secteur n'est pas valide
//			}
//		}
//		System.out.println("Aucun secteur correspondant à cet ID n'a été trouvé.");
//		return null; // Si aucun secteur valide n'est trouvé
		}
	}

	private SectorCard chooseSectorBot(Bot bot, Set<SectorCard> chosenSectors) {
		System.out.println(bot.getName() + "choisi un secteur");
		Random random = new Random();

		List<SectorCard> sectorControlledbyBotList = new ArrayList<SectorCard>(bot.getControlledSectors());

//        sectorControlledbyBotList.remove(centralCard);

		for (SectorCard s: chosenSectors) {
            sectorControlledbyBotList.remove(s);
		}

		int SectorByPositionInList = random.nextInt(sectorControlledbyBotList.size());
		SectorCard sectorChoosen = sectorControlledbyBotList.get(SectorByPositionInList);

		if (sectorChoosen == null) {
			List<SectorCard> secteursRestants = ground.getSectors();
			secteursRestants.removeAll(sectorControlledbyBotList);
			int SectorByPosition = random.nextInt(secteursRestants.size());
			sectorChoosen = secteursRestants.get(SectorByPosition);
		}

        System.out.println(bot.getName() + "a choisi le secteur n°" + sectorChoosen.getId());
		return sectorChoosen;
	}

	private void calculateSectorPoints(SectorCard sectorCard) {
		for (Hex hex : sectorCard.getHexes()) {
//			if (player.controlsHex(hex)) {
			Player hexOwner = hex.getCurrentOccupant();
			if (hex.getLevelSystem() > 0 && hexOwner != null) {
				int points = hex.getLevelSystem();
				System.out.println("Le joueur " + hexOwner.getName() + " gagne " + points + " dans le secteur n°" + sectorCard.getId());
				hexOwner.addPoints(points);
			}
		}
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
		System.out.println("On retire les vaisseaux en trop");
		Controller controller = getController();
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
				controller.updateHexLabel(hex.getIdHex(), excessShips, hex.getCurrentOccupant());
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

		updateControlledSectors();
	}

	private void placeShips(Player player, Ground ground) {
		// Obtenez tous les systèmes de niveau 1 non occupés dans des secteurs non occupés
		List<Hex> availableHexes = ground.getHexes().stream()
				.filter(hex -> hex.getLevelSystem() == 1 && hex.getCurrentOccupant() == null && !ground.getSectorById(hex.getSectorId()).getHasShips())
				.toList();

		//ça ça n'arrivera jamais vu qu'on l'utilise qu'au moment de l'initialisation (pour le moment)
//		if (availableHexes.isEmpty()) {
//			System.out.println("Aucun hex disponible pour le placement des vaisseaux.");
//			return;
//		}

		// Sélectionner un hex disponible pour le placement

		Scanner reader = new Scanner(System.in);
		Hex targetHex = null;

		//Lorsque c'est un bot qui doit choisir le systeme sur lequel placer ses 2 vaisseaux
		if (player instanceof Bot){
			Random random = new Random();
			targetHex = availableHexes.get(random.nextInt(availableHexes.size()));
		}

		//Lorsque c'est un joueur humain qui doit choisir le systeme sur lequel placer ses 2 vaisseaux
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

// Une fois sorti, on a bien validé targetHex

		System.out.println("Hex sélectionné : " + targetHex.getIdHex());

		for (int i=0; i<2; i++) {//On prend 2 vaisseaux hors du plateau et on les place sur l'Hex choisi
			player.getShipsHorsPlateau().peek().setPosition(targetHex); //positionne un vaisseau au niveau de l'hexagone cible
			player.getShipsSurPlateau().add(player.getShipsHorsPlateau().peek()); //On ajoute le nouveau vaisseau à la liste des vaisseaux situés sur le plateau
			targetHex.getShips().add(player.getShipsHorsPlateau().peek());
			player.addControlledSector(targetHex.getSector());
			System.out.println("Le joueur " + targetHex.getCurrentOccupant() + " controle le secteur n°" + targetHex.getSector().getId());
			System.out.println(player.getControlledSectors());
			int shipCount = targetHex.getShips().size();
			controller.updateHexLabel(targetHex.getIdHex(), shipCount, player);
			player.getShipsHorsPlateau().pop();
			// Définir le joueur comme occupant du système
			targetHex.setCurrentOccupant(player);
			player.getHexesOccupes().add(targetHex);
		}
//
//		System.out.println("Le joueur " + player.getName() + " est présent dans le secteur n°" + targetHex.getSector().getId());
		ground.getSectorById(targetHex.getSectorId()).setHasShips(true);
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
				p.getShipsSurPlateau().forEach(ship -> ship.setUsed(false));
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

		int humanPlayers=0;
		// Boucle jusqu'à ce qu'une entrée valide soit fournie
		while (humanPlayers<1 || humanPlayers>3) {
			System.out.println("Combien de joueurs humains ? (entre 1 et 3) ");
			try {
				humanPlayers = scanner.nextInt();
				if (humanPlayers < 0) {
					System.out.println("Le nombre de joueurs doit être positif. Veuillez réessayer.");
				}
			} catch (Exception e) {
				System.out.println("Entrée invalide. Veuillez entrer un nombre.");
				scanner.next();  // Vide le scanner pour éviter une boucle infinie
			}
		}

//		System.out.println("Combien de bots ?");
//		int botPlayers = scanner.nextInt();

		// Ajouter les joueurs humains

		for (int i = 0; i < humanPlayers; i++) {
			Player player = new Player();
			player.setPlayerName(); // Demander un nom
			players.add(player);
		}

		// Ajouter les bots
		//On complète avec des bots jusqu'à avoir 3 joueurs dans la partie
		for (int i = 0; i < 3-humanPlayers; i++) {
			players.add(new Bot(i));
		}

		players.get(0).setColor(Color.RED);
		players.get(1).setColor(Color.GREEN);
		players.get(2).setColor(Color.BLUE);

		//Maintenant que la map et les joueurs sont créés on peut initialiser le terrain
		System.out.println("Initialisation du terrain");
		game.initialShipDeployment();
		//J'ai mis la limite à 2 juste le temps des tests
		while ((getRound() < 2) && (game.players.size() != 2)) {
			game.nextRound();
		}

// Pour calculer les scores de victoire à la fin
		Map<String, Integer> tableauScores = new HashMap<>();

		//reparcourir l'ensemble des systèmes pour accorder les points x2
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
		System.out.println("La partie est terminée");
		System.out.println("Félicitations, à " + gagnant + " pour avoir gagner la partie avec " + scoreMax +" points !");
	}
}
