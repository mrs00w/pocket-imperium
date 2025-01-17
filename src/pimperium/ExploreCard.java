package pimperium;

import gui.Controller;

import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

/**
 * Représente la carte Expand
 */

public class ExploreCard implements CommandCard {
	/**
	 * Le joueur qui possède cette carte Explore
	 */
	private final Player player;

	/**
	 * Crée une carte Explore
	 * @param player le joueur qui possède la carte
	 */

	public ExploreCard(Player player) {
		this.player = player;
	}

	/**
	 * Renvoie la priorité de la carte
	 * @return la priorité de la carte
	 */

	public int getPriority() {
		return 2;
	}

	/**
	 * Renvoie le nom de la carte
	 * @return le nom de la carte
	 */

	public String getName(){return "explore";}

	/**
	 * Récupère les Hex voisins en commun de deux Hex sur le plateau
	 * @param hex1 le premier Hex à comparer
	 * @param hex2 le deuxième Hex à comparer
	 * @return liste contenant les voisins en commun de ces deux Hex
	 */

	private List<Hex> getCommonNeighbors(Hex hex1, Hex hex2) {
		List<Hex> neighborsHex1 = hex1.getNeighbors();
		List<Hex> neighborsHex2 = hex2.getNeighbors();

		// Trouver les voisins communs
		return neighborsHex1.stream()
				.filter(neighborsHex2::contains)
				.toList();
	}

	/**
	 * Vérifie si deux Hex ont des voisins en commun
	 * @param sourceHex le Hex de départ
	 * @param targetHex le Hex dans lequel on souhaite se déplacer
	 * @param currentPlayer le joueur qui possède l'Hex de départ
	 * @return
	 */

	private boolean hasValidCommonNeighbor(Hex sourceHex, Hex targetHex, Player currentPlayer) {
		// Obtenez les voisins communs
		List<Hex> commonNeighbors = getCommonNeighbors(sourceHex, targetHex);

		// Vérifiez si au moins un voisin répond aux critères
		return commonNeighbors.stream().anyMatch(hex ->
				hex.getCurrentOccupant() == null || hex.getCurrentOccupant() == currentPlayer);
	}

	/**
	 * Exécute les instructions de la carte Explore. Cette partie permet à un joueur de déplacer ses vaisseaux.
	 * Les vaisseaux peuvent être déplacés de maximum deux cases.
	 * @param currentRound La position de la carte à exécuter
	 */

	public void execute(int currentRound) {
		System.out.println(STR."\n\{player.getName()} explore d'autres systèmes avec ses vaisseaux.");
		Game game = Game.getInstance();
		Controller controller = game.getController();
		List<Player> players = new ArrayList<>(game.getPlayers());

		// Filtre des joueurs ayant choisi Explore pour compter le nbr de vaisseaux a déplacer
		List<Player> explorePlayers = players.stream()
				.filter(p -> p.getCard(currentRound) instanceof ExploreCard)
				.toList();

		// Déterminer le nombre de mouvements autorisés
		int exploreCount = explorePlayers.size();
		int fleetMovementsAllowed = switch (exploreCount) {
			case 1 -> 3;  // 3 mouvements si seul joueur à explorer
			case 2 -> 2;  // 2 mouvements si 2 joueurs explorent
			default -> 1; // 1 mouvement sinon
		};

		Scanner reader = new Scanner(System.in);
		if (player instanceof Bot){
			// Décision aléatoire : 1 chance sur 6 de passer la carte
			int decision = new Random().nextInt(6) + 1;  // Génère un nombre entre 1 et 6
			if (decision == 1) {
				System.out.println("Le bot a décidé de passer la carte.");
				return;
			}
			System.out.println("Le bot a décidé de jouer la carte Explore.");
		}

		System.out.println(player.getName()+" peut déplacer jusqu'à " +fleetMovementsAllowed+ " flottes");

		// Effectuer les mouvements autorisés
		int i=0;
		while(i < fleetMovementsAllowed){
			List<Hex> hexes = game.getGround().getHexes();
			List<Hex> occupiedByPlayer = hexes.stream()
					.filter(hex -> hex.getCurrentOccupant() != null && hex.getCurrentOccupant().equals(player))
					.filter(hex -> hex.getShips().stream().anyMatch(ship -> !ship.isUsed()))
					.toList();

			if (occupiedByPlayer.isEmpty()) {
				System.out.println("Vous ne pouvez pas déplacer de vaisseaux.");
				return;
			}

			int sourceHexId;
			Hex sourceHex;

			if(player instanceof Bot){
				sourceHex = occupiedByPlayer.get(new Random().nextInt(occupiedByPlayer.size()));
				sourceHexId = sourceHex.getIdHex();
			} else {
				System.out.println("Sélectionnez un hex à partir duquel déplacer une flotte (ID) ou tapez 0 pour passer votre tour :");

				try {
					sourceHexId = reader.nextInt();
				} catch (Exception e) {
					System.out.println("Entrée invalide. Veuillez entrer un nombre.");
					return;
				}

				if (sourceHexId == 0) {
					System.out.println("Vous choisissez de passer votre tour");
					return;
				}

				// Récupérer les vaisseaux dans l'hex source
				sourceHex = game.getGround().getHexById(sourceHexId);
				if(game.getGround().getTriPrime().contains(sourceHex)){
					if (game.getGround().getTriPrimeOccupant()!=player) {
						System.out.println("Vous n'occupez pas ce système.");
						continue;
					}
				}else{
					if (!occupiedByPlayer.contains(sourceHex)) {
						System.out.println("Cet hex n'est pas contrôlé par vous. Veuillez choisir un autre hex.");
						continue; // Refaire ce tour
					}
				}

			}
			List<Ship> shipsNotUsedInHex;
			if(game.getGround().getTriPrime().contains(sourceHex)){
				shipsNotUsedInHex = new ArrayList<Ship>(game.getGround().getHexById(34).getShips().stream()
						.filter(ship -> !ship.isUsed())
						.toList());
			}else {
				shipsNotUsedInHex = new ArrayList<Ship>(sourceHex.getShips().stream()
						.filter(ship -> !ship.isUsed())
						.toList());
			}

			if (shipsNotUsedInHex.isEmpty()) {
				System.out.println("Aucun vaisseau disponible dans cet hex.");
				continue;
			}

			int iBoucle=1;
			boolean boucle=true;
			while (boucle && iBoucle<=2) {
				Hex targetHex;
				int targetHexId;
				List<Ship> shipsToMove;

				if(player instanceof Bot){
					//Choix des vaisseaux à déplacer
					shipsToMove = shipsNotUsedInHex.stream()
							.limit(new Random().nextInt(shipsNotUsedInHex.size()) + 1)
							.toList();

					//Sélection hex de destination
					targetHex = sourceHex.getNeighbors().get(new Random().nextInt(sourceHex.getNeighbors().size()));
					targetHexId = targetHex.getIdHex();
					System.out.println(player.getName()+" déplace "+shipsToMove.size()+" vaisseau(x) de "+sourceHex.getIdHex()+" vers "+targetHex.getIdHex());

				} else {
					// Sélectionner les vaisseaux à déplacer
					shipsToMove = player.chooseShipsToMove(shipsNotUsedInHex);
					if (shipsToMove.isEmpty()) {
						System.out.println("Aucun vaisseau choisi pour ce déplacement. Essayez encore.");
						continue;
					}

					System.out.println("Sélectionnez un hex de destination (ID) :");

					try {
						targetHexId = reader.nextInt();
					} catch (Exception e) {
						System.out.println("Entrée invalide. Veuillez entrer un nombre.");
						return;
					}

					targetHex = game.getGround().getHexById(targetHexId);

					if (targetHex == null) {
						System.out.println("Hex non valide. Essayez encore.");
						continue;
					}

					if (!sourceHex.getNeighbors().contains(targetHex)) {
						System.out.println("Cet Hex n'est un pas un voisin de l'Hex de départ");
						continue;
					}

					if (targetHex.getCurrentOccupant() != player && targetHex.getCurrentOccupant() != null) {
						System.out.println("Vous ne pouvez pas déplacer vos vaisseaux dans un hex occupé par un autre joueur.");
						continue;
					}
				}

				if (!sourceHex.getNeighbors().contains(targetHex) && !hasValidCommonNeighbor(sourceHex, targetHex, player)) {
					System.out.println("Vous ne pouvez pas passé à travers un hex occupé par un autre joueur.");
					i--; // Refaire ce tour
					continue;
				}

				//On vérifie que personne ne contrôle le TriPrime
				if (targetHex.getLevelSystem() == 3){
					if (game.getGround().getTriPrimeOccupant() == null || game.getGround().getTriPrimeOccupant() == player){
						System.out.println(player.getName()+" contrôle le Tri Prime.");
						player.addControlledSector(targetHex.getSector());
						player.addHexesOccupes(game.getGround().getTriPrime());
						targetHex=game.getGround().getHexById(34);
						boucle=false;
						iBoucle++;
					}else {
						System.out.println("Le Tri Prime est déjà controlé par un autre joueur.");
						continue;
					}
				}

				// Mettre à jour le contrôle du hex cible
				if (targetHex.getCurrentOccupant() != player) { // && targetHex.getLevelSystem()!=3 ?
					targetHex.setCurrentOccupant(player);
					player.getHexesOccupes().add(targetHex);
					player.getControlledSectors().add(targetHex.getSector());
					System.out.println(STR."\{player.getName()} contrôle désormais l'hex \{targetHexId}.");
				}

				// Déplacer les vaisseaux
				for (Ship s : shipsToMove) {
					s.updatePosition(targetHex);
					s.setUsed(true); // Empêcher d'utiliser ce vaisseau à nouveau ce tour
					targetHex.getShips().add(s); // On ajoute le vaisseau dans la liste des vaisseaux de l'Hex cible
				}
				if(game.getGround().getTriPrime().contains(sourceHex)){
					sourceHex=game.getGround().getHexById(34);
				}
				sourceHex.getShips().subList(0, shipsToMove.size()).clear();
				//On supprime les ships déplacés de la liste de ships du Hex de départ

                // Retirer le contrôle du hex source s'il est vidé
				if (sourceHex.getShips().isEmpty()) {
					sourceHex.setCurrentOccupant(null);
					player.getHexesOccupes().remove(sourceHex);
					if(game.getGround().getTriPrime().contains(sourceHex)){
						Hex.setTriPrimeOccupant(null);
						System.out.println("Vous ne contrôlez plus le TriPrime");
					}else{
						System.out.println(STR."Le hex \{sourceHexId} n'est plus contrôlé.");
					}
				}

                controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), player);
                controller.updateHexLabel(targetHex.getIdHex(), targetHex.getShips().size(), player);


                if (iBoucle==1){
					int choice;
					if(player instanceof Bot){
						choice = new Random().nextInt(5) + 1;
					}else {
						System.out.println("Voulez vous déplacer votre flotte d'une case de plus ? (0 pour non, 1 pour oui) : ");
						try {
							choice = reader.nextInt();
						} catch (Exception e) {
							System.out.println("Entrée invalide. Veuillez entrer un nombre.");
							return;
						}
					}
					if(choice==1){
						System.out.println("");
						sourceHex=targetHex;
						sourceHexId=sourceHex.getIdHex();
							shipsNotUsedInHex=sourceHex.getShips();
					} else{
						boucle=false;
					}
				}
				iBoucle++;
			}
			i++;
		}

		System.out.println("\nExploration terminée.");
	}
}