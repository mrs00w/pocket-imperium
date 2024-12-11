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

	public void execute(int currentRound) {
		System.out.println(STR."\{player.getName()} explore d'autres systèmes avec ses vaisseaux.");
		Game game = Game.getInstance();
		List<Player> players = new ArrayList<>(game.getPlayers());

		// Filtre des joueurs ayant choisi Explore
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

		// Effectuer les mouvements autorisés
		for (int i = 0; i < fleetMovementsAllowed; i++) {
			System.out.println("Sélectionnez un hex à partir duquel déplacer une flotte (ID) :");
			int sourceHexId = reader.nextInt();

			// Récupérer les vaisseaux dans l'hex source
			Hex sourceHex = game.getGround().getHexById(sourceHexId);
			if (!sourceHex.getCurrentOccupant().equals(player)) {
				System.out.println("Cet hex n'est pas contrôlé par vous. Veuillez choisir un autre hex.");
				i--; // Refaire ce tour
				continue;
			}

			List<Ship> shipsInHex = new ArrayList<>();
			for (Ship ship : sourceHex.getShips()) {
				if (ship.getPlayer() == player && !ship.isUsed()) {
					shipsInHex.add(ship);

					// Sélectionner les vaisseaux à déplacer
					List<Ship> shipsToMove = player.chooseShipsToMove(shipsInHex);
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
					if (!sourceHex.getNeighbors().contains(targetHex)) {
						System.out.println("Le hex cible n'est pas adjacent. Essayez encore.");
						i--; // Refaire ce tour
						continue;
					}
					if (targetHex.getCurrentOccupant() != player) {
						System.out.println("Vous ne pouvez pas déplacer vos vaisseaux dans un hex occupé par un autre joueur.");
						i--; // Refaire ce tour
						continue;
					}
					if (targetHex.getLevelSystem() == 3 && !shipsToMove.isEmpty()) {
						System.out.println("Vous devez vous arrêter au hex Tri-Prime si vous y entrez.");
					}

					// Déplacer les vaisseaux
					for (Ship s : shipsToMove) {
						s.updatePosition(targetHex);
						s.markAsUsed(true); // Empêcher d'utiliser ce vaisseau à nouveau ce tour
					}

					// Mettre à jour le contrôle du hex cible
					if (targetHex.getCurrentOccupant() != player) {
						targetHex.setCurrentOccupant(player);
						System.out.println(STR."Vous contrôlez désormais le hex \{targetHexId}.");
					}

					// Retirer le contrôle du hex source s'il est vidé
					if (sourceHex.getShips().isEmpty()) {
						sourceHex.setCurrentOccupant(null);
						System.out.println(STR."Le hex \{sourceHexId} n'est plus contrôlé.");
					}
				}

				System.out.println("Exploration terminée.");
			}
		}
	}
}