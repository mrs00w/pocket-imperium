package pimperium;

import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

public class ExploreCard implements CommandCard {
	private final Player player;

	public ExploreCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
		int priority = 2;
		return priority;
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
		return false;
	}

	public void execute(int currentRound) {
		System.out.println(STR."\{player.getName()} explore d'autres systèmes avec ses vaisseaux.");
		Game game = Game.getInstance();
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

		Scanner reader = new Scanner(System.in); // Scanner pour l'entrée utilisateur
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
//			for (Ship ship : sourceHex.getShips()) {
//				if (ship.getPlayer() == player && !ship.isUsed()) { //pourquoi vérifier que le vaisseau appartient au jouer alors qu'on a déjà vérifié que l'hex appartenait au joueur ?
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
			if (targetHex == null) {
				System.out.println("Hex non valide. Essayez encore.");
				i--; // Refaire ce tour
				continue;
			}
			//On peut avancer de 1 ou 2 hexagones. On doit donc vérifier que l'hexagone cible est bien voisin à 2 hex près
			//On a targetHexId l'hex de destination et sourceHexId l'hex de départ
			int origineDizaine = sourceHexId/10;
			int origineUnite = sourceHexId%10;
			int targetDizaine = targetHexId/10;
			int targetUnite = targetHexId%10;
			if (!(targetDizaine >= origineDizaine - 2 && targetDizaine <= origineDizaine + 2 && targetUnite >= origineUnite - 2 && targetUnite <= origineUnite + 2)) {
				System.out.println("Le hex cible n'est pas adjacent. Essayez encore.");
				i--; // Refaire ce tour
				continue;
			}
			if (targetHex.getCurrentOccupant() != player && targetHex.getCurrentOccupant() != null) {
				System.out.println("Vous ne pouvez pas déplacer vos vaisseaux dans un hex occupé par un autre joueur.");
				i--; // Refaire ce tour
				continue;
			}
			if (!sourceHex.getNeighbors().contains(targetHex) && !hasValidCommonNeighbor(sourceHex,targetHex,player)){
				System.out.println("Vous ne pouvez pas passé à travers un hex occupé par un autre joueur.");
				i--; // Refaire ce tour
				continue;
			}
			//IMPORTANT faire ce test avant de vérifier que targetHex appartient au TriPrime
			//On vérifie que personne ne contrôle le TriPrime
			if (targetHex.getLevelSystem() == 3 && Hex.getTriPrimeOccupant()!=null || Hex.getTriPrimeOccupant()!=player) {// && !shipsToMove.isEmpty()) { Ça sert a rien on l'a testé au dessus
				System.out.println("Le Tri Prime est déjà controlé par un autre joueur.");
				i--; //Refaire ce tour
				continue;
			}
			if (!sourceHex.getNeighbors().contains(targetHex) && uniqueCommonNeighborTriPrime(sourceHex,targetHex)){
				if(targetHex.getCurrentOccupant()!=null && targetHex.getCurrentOccupant()!=player){
					System.out.println("Vous ne pouvez pas passé à travers du TriPrime alors qu'il est occupé");
					i--; // Refaire ce tour
					continue;
				} else{
					//S'arrêter sur le TriPrime
					targetHex = (sourceHex.getNeighbors().stream()
							.filter(targetHex.getNeighbors()::contains).toList()).getFirst();
				}
			}
			// Déplacer les vaisseaux
			for (Ship s : shipsToMove) {
				s.updatePosition(targetHex);
				s.setUsed(true); // Empêcher d'utiliser ce vaisseau à nouveau ce tour
				targetHex.getShips().add(s); //On ajoute le vaisseau dans la liste des vaisseaux de l'Hex cible
			}
			sourceHex.getShips().subList(0,shipsToMove.size()).clear(); //On supprime les ships déplacés de la liste de ships du Hex de départ

			// Mettre à jour le contrôle du hex cible
			if (targetHex.getCurrentOccupant() != player) {
				targetHex.setCurrentOccupant(player);
				System.out.println(STR."Vous contrôlez désormais l'hex \{targetHexId}.");
			}

			// Retirer le contrôle du hex source s'il est vidé
			if (sourceHex.getShips().isEmpty()) {
				sourceHex.setCurrentOccupant(null);
				System.out.println(STR."Le hex \{sourceHexId} n'est plus contrôlé.");
			}
		}
		System.out.println("Exploration terminée.");
		for (Ship ship : player.getShipsSurPlateau()){
			ship.setUsed(false);
		}
	}
}