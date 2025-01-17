package pimperium;

import gui.Controller;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.*;

/**
 * Représente une partie
 */

public class Game {
	/**
	 * Instance statique du jeu.
	 * Ce champ est utilisé pour accéder à l'instance unique du jeu en cours.
	 *
	 * @since 1.0
	 */
	private static Game game = null;

	/**
	 * Liste des joueurs participant à la partie.
	 * Cette liste contient tous les joueurs actuellement actifs dans le jeu.
	 *
	 * @since 1.0
	 */
	private final List<Player> players;

	/**
	 * Terrain sur lequel les joueurs évoluent.
	 * Cette variable contient les informations relatives au terrain du jeu,
	 * telles que la disposition des secteurs et des ressources.
	 *
	 * @since 1.0
	 */
	private Ground ground;

	/**
	 * Le numéro actuel du tour de jeu.
	 * Ce champ est utilisé pour suivre le nombre de tours qui se sont écoulés
	 * depuis le début de la partie.
	 *
	 * @since 1.0
	 */
	private static int round = 0;

	/**
	 * Carte centrale du jeu.
	 * Cette carte contient des informations stratégiques qui influencent le déroulement
	 * du jeu, et elle est placée au centre du terrain.
	 *
	 * @since 1.0
	 */
	private SectorCard centralCard;

	/**
	 * Contrôleur du jeu.
	 * Ce contrôleur gère l'interaction entre les différentes parties du jeu, les joueurs
	 * et les événements du jeu.
	 *
	 * @since 1.0
	 */
	private Controller controller;

	/**
	 * Constructeur privé pour le singleton de la partie.
	 */

	private Game() {
		this.ground = new Ground();
		this.players = new ArrayList<>();
		this.centralCard = ground.getSectorById(4);
	}

	/**
	 * Définit le contrôleur de l'interface.
	 *
	 * @param controller Le contrôleur à associer.
	 */

	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Obtient le contrôleur de l'interface.
	 *
	 * @return Le contrôleur actuel.
	 */

	public Controller getController() {
		return controller;
	}

	/**
	 * Récupère l'instance unique de la classe Game.
	 *
	 * @return L'instance unique.
	 */

	public static Game getInstance() {
		if (game == null) {
			System.out.println("Création d'une partie");
			game = new Game();
		}
		return game;
	}

	/**
	 * Met à jour les secteurs contrôlés par chaque joueur.
	 */

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

	/**
	 * Réinitialise les secteurs contrôlés par chaque joueur.
	 */

	public void clearControlledSectors() {
		for (Player p : Game.getInstance().getPlayers()) {
			p.getControlledSectors().clear();
		}
	}

	/**
	 * Calcule les scores des joueurs pour un tour donné.
	 */

	public void calculScore() {
		System.out.println("Calcul des scores de chaque joueur pour le round");
		Set<SectorCard> chosenSectors = new HashSet<>();
		Player triPrimeController = ground.getTriPrimeOccupant();

		for (Player player : players) {

            SectorCard chosenSector;
			chosenSector = chooseSector(player, chosenSectors);
			chosenSectors.add(chosenSector);
			for (Hex hex : chosenSector.getHexes()) {
				if (hex.getLevelSystem() > 0 && hex.getCurrentOccupant() != null) {
					hex.getCurrentOccupant().addPoints(hex.getLevelSystem());
					System.out.println(hex.getCurrentOccupant().getName()+" a gagné "+hex.getLevelSystem()+" points.");
				}
			}
		}

		if (triPrimeController != null) {
			SectorCard additionalSector = chooseSector(ground.getTriPrimeOccupant(), chosenSectors);
			for (Hex hex : additionalSector.getHexes()){
				if (hex.getLevelSystem()>0 && hex.getCurrentOccupant()!=null){
					hex.getCurrentOccupant().addPoints(hex.getLevelSystem());
					System.out.println(hex.getCurrentOccupant().getName()+" a gagné "+hex.getLevelSystem()+" points.");
				}
			}
        }
	}

	/**
	 * Détermine le joueur qui contrôle le secteur central TriPrime.
	 *
	 * @return Le joueur contrôlant le TriPrime ou null si aucun joueur ne le contrôle.
	 */

	public Player getTriPrimeController() {
		for (Player player : players) {
			if (player.controlsSector(centralCard)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Permet à un joueur de choisir un secteur.
	 *
	 * @param player         Le joueur effectuant le choix.
	 * @param chosenSectors  Les secteurs déjà choisis.
	 * @return Le secteur choisi.
	 */

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
		}
	}

	/**
	 * Choix de secteur pour un bot.
	 *
	 * @param bot            Le bot effectuant le choix.
	 * @param chosenSectors  Les secteurs déjà choisis.
	 * @return Le secteur choisi par le bot.
	 */

	private SectorCard chooseSectorBot(Bot bot, Set<SectorCard> chosenSectors) {
		System.out.println(bot.getName() + "choisi un secteur");
		Random random = new Random();

		List<SectorCard> sectorControlledbyBotList = new ArrayList<SectorCard>(bot.getControlledSectors());

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

	/**
	 * Calcule les points obtenus par les joueurs dans un secteur donné.
	 *
	 * @param sectorCard Le secteur à partir duquel les points sont calculés.
	 */

	private void calculateSectorPoints(SectorCard sectorCard) {
		for (Hex hex : sectorCard.getHexes()) {
			Player hexOwner = hex.getCurrentOccupant();
			if (hex.getLevelSystem() > 0 && hexOwner != null) {
				int points = hex.getLevelSystem();
				System.out.println("Le joueur " + hexOwner.getName() + " gagne " + points + " dans le secteur n°" + sectorCard.getId());
				hexOwner.addPoints(points);
			}
		}
	}

	/**
	 * Retourne la liste des joueurs participant à la partie.
	 *
	 * @return Une liste des joueurs {@link Player}.
	 */

	public List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * Retourne le plateau de jeu.
	 *
	 * @return Le plateau de jeu {@link Ground}.
	 */
	public Ground getGround() {
		return ground;
	}

	/**
	 * Retourne le numéro du tour actuel.
	 *
	 * @return Le numéro de tour actuel.
	 */
	public static int getRound() {
		return round;
	}

	/**
	 * Compare l'ordre des joueurs en fonction de la priorité d'une carte donnée.
	 *
	 * @param indiceCard L'indice de la carte à comparer.
	 * @return Une liste des joueurs triée par ordre de priorité.
	 */

	public List<Player> compareOrder(int indiceCard) {
		List<Player> copie = new ArrayList<>(players);
		copie.sort(Comparator.comparingInt((Player player) -> player.getCard(indiceCard).getPriority())
				.thenComparingInt(Player::getPriority)); // Comparer la priorité des joueurs en cas d'égalité);
		return copie;
	}

	/**
	 * Gère le maintien des vaisseaux sur le plateau en retirant les excédents.
	 */

	public void sustainShips() {
		System.out.println("On retire les vaisseaux en trop");
		Controller controller = getController();
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
					Ship shipToRemove = shipsOnHex.getFirst();  // Retirer les vaisseaux du fond de la liste
					hex.getShips().remove(shipToRemove);  // Retirer le vaisseau de l'hex
					// Retourner le vaisseau à l'approvisionnement (ou le stockage)
					hex.getCurrentOccupant().getShipsSurPlateau().remove(shipToRemove);
					hex.getCurrentOccupant().getShipsHorsPlateau().add(shipToRemove);  // Assurez-vous que game.getSupply() existe et fonctionne
				}

				System.out.println(STR."\{excessShips} vaisseaux excédentaires ont été retirés de l'hex \{hex.getIdHex()}");
				controller.updateHexLabel(hex.getIdHex(), shipsOnHex.size(), hex.getCurrentOccupant());
			}
		}
	}

	/**
	 * Initialise le déploiement des vaisseaux au début de la partie.
	 */

	public void initialShipDeployment() {
		System.out.println("");
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

	/**
	 * Place les vaisseaux d'un joueur sur le plateau.
	 *
	 * @param player Le joueur plaçant les vaisseaux.
	 * @param ground Le plateau de jeu.
	 */

	private void placeShips(Player player, Ground ground) {
		// Obtenez tous les systèmes de niveau 1 non occupés dans des secteurs non occupés
		List<Hex> availableHexes = ground.getHexes().stream()
				.filter(hex -> hex.getLevelSystem() == 1 && hex.getCurrentOccupant() == null && !ground.getSectorById(hex.getSectorId()).getHasShips())
				.toList();

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

		System.out.println("\nHex sélectionné : " + targetHex.getIdHex());

		for (int i=0; i<2; i++) {//On prend 2 vaisseaux hors du plateau et on les place sur l'Hex choisi
			player.getShipsHorsPlateau().peek().setPosition(targetHex);
			// positionne un vaisseau au niveau de l'hexagone cible
			player.getShipsSurPlateau().add(player.getShipsHorsPlateau().peek());
			// On ajoute le nouveau vaisseau à la liste des vaisseaux situés sur le plateau
			targetHex.getShips().add(player.getShipsHorsPlateau().peek());
			player.addControlledSector(targetHex.getSector());
			int shipCount = targetHex.getShips().size();
			controller.updateHexLabel(targetHex.getIdHex(), shipCount, player);
			player.getShipsHorsPlateau().pop();
			// Définir le joueur comme occupant du système
			targetHex.setCurrentOccupant(player);
			player.getHexesOccupes().add(targetHex);
		}

		ground.getSectorById(targetHex.getSectorId()).setHasShips(true);

		System.out.println(STR."\{player.getName()} a placé 2 vaisseaux sur le système \{targetHex.getIdHex()}");
	}

	/**
	 * Exécute un nouveau tour
	 */

	public void nextRound() {
		round++;
		System.out.println("\nNouvelle manche");
		for (Player p : players) {
			System.out.println(STR."\nLe joueur \{p.getName()} planifie son tour");
			p.plan();

		}
		for (int iCard = 0; iCard < 3; iCard++) {
			List<Player> copie = compareOrder(iCard);
			System.out.println("\nAu tour "+ (iCard+1)+ " l'ordre des joueurs est : ");
			for (Player p : copie) {
				System.out.print(p.getName() +" ");
			}
			System.out.println("");
			for (Player p : copie) {
				p.perform(iCard);
				p.getShipsSurPlateau().forEach(ship -> ship.setUsed(false));
			}
		}
		sustainShips();
		calculScore();
		for (Player p : players){
			System.out.println(p.getName()+" a "+p.getScore()+" points.");
		}
		updatePriority(); //Le marqueur "Premier Joueur" passe au joueur suivant
	}

	/**
	 * Modifie la priorité des joueurs. Représente la rotation des joueurs.
	 */

	private void updatePriority() {
		for (Player p: players){
			p.updatePrio();
		}
	}

	/**
	 * Méthode principale pour configurer et lancer une partie.
	 */

	public void setupGame() {
		Game game = getInstance();
		System.out.println("Création de la partie");
		//Initialisation du terrain
		game.ground = new Ground();
		System.out.println("Création du terrain");
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

		for (int i = 0; i < humanPlayers; i++) {
			Player player = new Player();
			player.setPlayerName(); // Demander un nom
			players.add(player);
		}

		// Ajouter les bots
		// On complète avec des bots jusqu'à avoir 3 joueurs dans la partie
		for (int i = 0; i < 3-humanPlayers; i++) {
			players.add(new Bot(i));
		}

		players.get(0).setColor(Color.RED);
		players.get(1).setColor(Color.GREEN);
		players.get(2).setColor(Color.BLUE);

		//Maintenant que la map et les joueurs sont créés on peut initialiser le terrain
		System.out.println("\nInitialisation du terrain");
		game.initialShipDeployment();
		while ((getRound() < 9) && (game.players.size() != 2)) {
			game.nextRound();
		}

		if(getRound()==9) { // C'est à dire qu'on a atteint la fin de la partie
			// reparcourir l'ensemble des systèmes pour accorder les points x2
			for (Hex hex : game.getGround().getHexes()) {
				if (hex.getLevelSystem() > 0 && hex.getLevelSystem() < 3 && hex.getCurrentOccupant() != null) {
					hex.getCurrentOccupant().addPoints(hex.getLevelSystem() * 2);
					System.out.println(hex.getCurrentOccupant().getName() + " a gagné " + hex.getLevelSystem() + " points.");
				}
				if (hex.getIdHex() == 34 && hex.getCurrentOccupant() != null) {
					hex.getCurrentOccupant().addPoints(6);
					System.out.println(hex.getCurrentOccupant().getName() + " a gagné 6 points.");
				}
			}
		}

		String gagnant = null;
		int scoreMax = 0;

		for (Player player : players) {

			if (player.getScore() > scoreMax) {
				scoreMax = player.getScore();
				gagnant = player.getName();
			}
		}
		System.out.println("La partie est terminée");
		System.out.println("Félicitations, à " + gagnant + " pour avoir gagner la partie avec " + scoreMax +" points !");
	}
}
