package pimperium;

import gui.Controller;

import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

//Ce que j'ai pas fait pour explore :
//Individual ships may be added to a fleet as it passes
//through a hex, or left behind in a hex while the fleet
//moves away to an adjacent hex.

public class ExploreCard implements CommandCard {
	private final Player player;

	public ExploreCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
		return 2;
	}

	private List<Hex> getCommonNeighbors(Hex hex1, Hex hex2) {
		List<Hex> neighborsHex1 = hex1.getNeighbors();
		List<Hex> neighborsHex2 = hex2.getNeighbors();

		// Trouver les voisins communs
		return neighborsHex1.stream()
				.filter(neighborsHex2::contains)
				.toList();
	}

	private boolean hasValidCommonNeighbor(Hex sourceHex, Hex targetHex, Player currentPlayer) {
		// Obtenez les voisins communs
		List<Hex> commonNeighbors = getCommonNeighbors(sourceHex, targetHex);

		// Vérifiez si au moins un voisin répond aux critères
		return commonNeighbors.stream().anyMatch(hex ->
				hex.getCurrentOccupant() == null || hex.getCurrentOccupant() == currentPlayer);
	}

	public boolean uniqueCommonNeighborTriPrime(Hex hex1, Hex hex2) {
		List<Hex> neighborsHex1 = hex1.getNeighbors();
		List<Hex> neighborsHex2 = hex2.getNeighbors();

		List<Hex> commonNeighbors = neighborsHex1.stream()
				.filter(neighborsHex2::contains)
				.filter(hex -> hex.getLevelSystem() == 3) // Ajout du critère sur le LevelSystem
				.toList();

		if (commonNeighbors.size() == 1) {
			return true;
		}

//        private boolean isReachableInTwoSteps(Hex sourceHex, Hex targetHex) {
//            // Étape 1 : Obtenez les voisins directs de la source
//            List<Hex> firstStepNeighbors = sourceHex.getNeighbors();
//
        // Étape 2 : Vérifiez si la destination est un voisin direct
//            if (firstStepNeighbors.contains(targetHex)) {
//                return true;
//            }

        // Étape 3 : Obtenez les voisins des voisins et vérifiez s'ils contiennent la cible
//            for (Hex neighbor : firstStepNeighbors) {
//                if (neighbor.getNeighbors().contains(targetHex)) {
//                    return true;
//                }
//            }
//        }

		// Si aucune condition n'est remplie, la cible n'est pas atteignable
		return false;
	}

	public void execute(int currentRound) {
		System.out.println(STR."\{player.getName()} explore d'autres systèmes avec ses vaisseaux.");
		Game game = Game.getInstance();
		Controller controller = game.getController();
		List<Player> players = new ArrayList<>(game.getPlayers());


		//A partir de là on peut optimiser

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

		//Jusque là on peut optimiser

		System.out.println(player.getName() + ", voulez vous jouer cette carte ?");
		System.out.println("1. Passer la carte");
		System.out.println("2. Jouer la carte");

		Scanner reader = new Scanner(System.in);

		int choice;
		try {
			choice = reader.nextInt();
		} catch (Exception e) {
			System.out.println("Entrée invalide. Veuillez entrer un nombre.");
			return;
		}

		if (choice == 1) {
			System.out.println("Vous avez choisi de passer cette carte.");
			// Rien à faire ici : le joueur ne joue pas cette carte
			return;
		} // Scanner pour l'entrée utilisateur

		System.out.println("Vous pouvez déplacer " +fleetMovementsAllowed+ " flottes");

		// Effectuer les mouvements autorisés
		for (int i = 0; i < fleetMovementsAllowed; i++) {
			System.out.println("Sélectionnez un hex à partir duquel déplacer une flotte (ID) :");
			int sourceHexId = reader.nextInt();

			// Récupérer les vaisseaux dans l'hex source
			Hex sourceHex = game.getGround().getHexById(sourceHexId);
			List<Hex> hexes = game.getGround().getHexes();
			List<Hex> occupiedByPlayer = hexes.stream()
					.filter(hex -> hex.getCurrentOccupant() != null && hex.getCurrentOccupant().equals(player)).toList();
			if (!occupiedByPlayer.contains(sourceHex)){
				System.out.println("Cet hex n'est pas contrôlé par vous. Veuillez choisir un autre hex.");
				i--; // Refaire ce tour
				continue;
			}

			List<Ship> shipsNotUsedInHex = sourceHex.getShips().stream()
					.filter(ship -> !ship.isUsed())
					.toList();
			if (shipsNotUsedInHex.isEmpty()) {
				System.out.println("Aucun vaisseau disponible dans cet hex.");
				i--;
				continue;
			}

			int iBoucle=1;
			boolean boucle=true;
			while (boucle && iBoucle<=2) {
//			for (Ship ship : sourceHex.getShips()) {
//				if (ship.getPlayer() == player && !ship.isUsed()) { //pourquoi vérifier que le vaisseau appartient au joueur alors qu'on a déjà vérifié que l'hex appartenait au joueur ?
//					shipsNotUsedInHex.add(ship);

				// Sélectionner les vaisseaux à déplacer
				List<Ship> shipsToMove = player.chooseShipsToMove(shipsNotUsedInHex);
				if (shipsToMove.isEmpty()) {
					System.out.println("Aucun vaisseau choisi pour ce déplacement. Essayez encore.");
					i--; // Refaire ce tour
					continue;
				}

				System.out.println("Sélectionnez un hex de destination (ID) :");
				int targetHexId = reader.nextInt();
				Hex targetHex = game.getGround().getHexById(targetHexId);

				// Valider les règles de déplacement
                // Version de Romain
//                if (targetHex == null || !isReachableInTwoSteps(sourceHex, targetHex)) {
//                    System.out.println("Le hex cible n'est pas atteignable en un ou deux mouvements. Essayez encore.");
//                    i--; // Refaire ce tour
//                    continue;
//                }
                if (targetHex == null) {
					System.out.println("Hex non valide. Essayez encore.");
					i--; // Refaire ce tour
					continue;
				}


//			//On peut avancer de 1 ou 2 hexagones. On doit donc vérifier que l'hexagone cible est bien voisin à 2 hex près
//			//On a targetHexId l'hex de destination et sourceHexId l'hex de départ
//			//ça marche pas à tous les coups --> Faire les mvt 1 par 1
//			int origineDizaine = sourceHexId/10;
//			int origineUnite = sourceHexId%10;
//			int targetDizaine = targetHexId/10;
//			int targetUnite = targetHexId%10;
//			if (!((targetDizaine >= origineDizaine - 2) && (targetDizaine <= origineDizaine + 2) && (targetUnite >= origineUnite - 2) && (targetUnite <= origineUnite + 2))) {
//				System.out.println("Le hex cible n'est pas adjacent. Essayez encore.");
//				i--; // Refaire ce tour
//				continue;
//			}

				if (targetHex.getCurrentOccupant() != player && targetHex.getCurrentOccupant() != null) {
					System.out.println("Vous ne pouvez pas déplacer vos vaisseaux dans un hex occupé par un autre joueur.");
					i--; // Refaire ce tour
					continue;
				}

//				if (!sourceHex.getNeighbors().contains(targetHex) && !hasValidCommonNeighbor(sourceHex, targetHex, player)) {
//					System.out.println("Vous ne pouvez pas passé à travers un hex occupé par un autre joueur.");
//					i--; // Refaire ce tour
//					continue;
//				}

				//IMPORTANT faire ce test avant de vérifier que targetHex appartient au TriPrime
				//On vérifie que personne ne contrôle le TriPrime
				if (targetHex.getLevelSystem() == 3){
					if (Hex.getTriPrimeOccupant() == null || Hex.getTriPrimeOccupant() == player){
						System.out.println("Vous controllez le Tri Prime.");
						boucle=false;
					}else {
						System.out.println("Le Tri Prime est déjà controlé par un autre joueur.");
						i--; //Refaire ce tour
						continue;
					}
				}

				//Pour ne pas passer au travers du tri prime mais on en n'a plus besoin
//				if (!sourceHex.getNeighbors().contains(targetHex) && uniqueCommonNeighborTriPrime(sourceHex, targetHex)) {
//					if (targetHex.getCurrentOccupant() != null && targetHex.getCurrentOccupant() != player) {
//						System.out.println("Vous ne pouvez pas passé à travers du TriPrime alors qu'il est occupé");
//						i--; // Refaire ce tour
//						continue;
//					} else {
//						//S'arrêter sur le TriPrime
//						targetHex = (sourceHex.getNeighbors().stream()
//								.filter(targetHex.getNeighbors()::contains).toList()).getFirst();
//					}
//				}

				// Mettre à jour le contrôle du hex cible
				if (targetHex.getCurrentOccupant() != player) {
					targetHex.setCurrentOccupant(player);
					System.out.println(STR."Vous contrôlez désormais l'hex \{targetHexId}.");
				}

				// Déplacer les vaisseaux
				for (Ship s : shipsToMove) {
					s.updatePosition(targetHex);
					s.setUsed(true); // Empêcher d'utiliser ce vaisseau à nouveau ce tour
					targetHex.getShips().add(s); //On ajoute le vaisseau dans la liste des vaisseaux de l'Hex cible
				}
				sourceHex.getShips().subList(0, shipsToMove.size()).clear(); //On supprime les ships déplacés de la liste de ships du Hex de départ

                //Version Romain
//                List<Ship> shipsInSourceHex = sourceHex.getShips();
//                int maxShipsToRemove = Math.min(shipsToMove.size(), shipsInSourceHex.size());
//                // Supprimer uniquement les vaisseaux valides
//                shipsInSourceHex.subList(0, maxShipsToRemove).clear();
//
//                controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), player);
//                controller.updateHexLabel(targetHex.getIdHex(), targetHex.getShips().size(), player);

                // Retirer le contrôle du hex source s'il est vidé
				if (sourceHex.getShips().isEmpty()) {
					sourceHex.setCurrentOccupant(null);
					System.out.println(STR."Le hex \{sourceHexId} n'est plus contrôlé.");
				}
                controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), player);
                controller.updateHexLabel(targetHex.getIdHex(), targetHex.getShips().size(), player);


                if (iBoucle==1){
					System.out.println("Voulez vous déplacer votre flotte d'une case de plus ? (0 pour non, 1 pour oui) : ");
					if(reader.nextInt()==1){
						System.out.println("");
						sourceHex=targetHex;
						sourceHexId=sourceHex.getIdHex();
						shipsNotUsedInHex=shipsToMove; //ajouter à la liste les vaisseaux du joueur qui étaient déjà sur l'hex d'arrivé si il y en a
					} else{
						boucle=false;
					}
				}
				iBoucle++;
			}
		}

		player.getShipsSurPlateau().forEach(ship -> ship.setUsed(false));
		System.out.println("Exploration terminée.");
	}
}