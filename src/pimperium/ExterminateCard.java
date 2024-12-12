package pimperium;

import java.util.ArrayList;
import java.util.*;

public class ExterminateCard implements CommandCard {

	private Game game;
	private Player player;

	public ExterminateCard(Player player) {
		this.game = Game.getInstance();
		this.player = player;
	}

	public int getPriority() {
		int priority = 3;
		return priority;
	}

	public void execute(int currentround) {

		System.out.println(STR."\{this.player.getName()} envahit un système");
		System.out.println("Quel système voulez vous envahir ? (ID)");
		Scanner reader = new Scanner(System.in);
		int idHex = reader.nextInt();
		Hex targetHex = game.getGround().getHexById(idHex);

		// Vérifier que le joueur n'occupe pas déjà le système
		if (targetHex.getCurrentOccupant() == player) {
			System.out.println("Vous occupez déjà ce système.");
			return;
		}

		// Vérifier que le système est adjacent
		List<Hex> neighbors = targetHex.getNeighbors();
		List<Hex> validSources = neighbors.stream()
				.filter(h -> h.getCurrentOccupant() == player && !h.getShips().isEmpty())
				.toList();

		if (validSources.isEmpty()) {
			System.out.println("Aucun hex adjacent ne contient vos vaisseaux pour l'invasion.");
			return;
		}

		// Demander au joueur combien de vaisseaux il souhaite utiliser
		System.out.println("Combien de vaisseaux voulez-vous utiliser pour l'invasion ?");
		int numberOfShips = reader.nextInt();

		if (numberOfShips <= 0) {
			System.out.println("Vous devez utiliser au moins un vaisseau.");
			return;
		}

		List<Ship> invasionFleet = new ArrayList<>();

		// Collecter les vaisseaux à partir des hexes adjacents
		while (invasionFleet.size() < numberOfShips) {
			System.out.println("Sélectionnez un hex adjacent (ID) pour fournir des vaisseaux :");
			validSources.forEach(h -> System.out.println(STR."Hex ID: \{h.getIdHex()} - Vaisseaux disponibles: \{h.getShips().size()}"));

			int sourceHexId = reader.nextInt();
			Hex sourceHex = validSources.stream()
					.filter(h -> h.getIdHex() == sourceHexId)
					.findFirst()
					.orElse(null);

			if (sourceHex == null) {
				System.out.println("Hex invalide. Veuillez réessayer.");
				continue;
			}

			System.out.println("Combien de vaisseaux voulez-vous prendre de cet hex ?");
			int shipsToTake = reader.nextInt();

			if (shipsToTake <= 0 || shipsToTake > sourceHex.getShips().size()) {
				System.out.println("Nombre de vaisseaux invalide. Veuillez réessayer.");
				continue;
			}

			for (int i=0; i < shipsToTake; i++) {
				Ship movingShip = sourceHex.getShips().removeFirst();
				invasionFleet.add(movingShip);
			}
		}

		// Vérifier l'occupant actuel de la cible
		Player currentOccupant = targetHex.getCurrentOccupant();
		List<Ship> defendingFleet = targetHex.getShips();

		// Résolution de l'invasion
		if (!defendingFleet.isEmpty()) {
			int smallestFleetSize = Math.min(invasionFleet.size(), defendingFleet.size());

			// Retirer les vaisseaux de chaque côté

			player.getShipsSurPlateau().forEach(invasionFleet.subList(0, smallestFleetSize)::remove);
			targetHex.getCurrentOccupant().getShipsSurPlateau().forEach(invasionFleet.subList(0, smallestFleetSize)::remove);

			invasionFleet.subList(0, smallestFleetSize).clear();
			defendingFleet.subList(0, smallestFleetSize).clear();

			// IL faut aussi retirer les vaisseaux en question du plateau

			System.out.println(STR."Combat terminé : \{smallestFleetSize} vaisseaux détruits de chaque côté.");
		}

		// Résultat de l'invasion
		if (invasionFleet.isEmpty()) {
			System.out.println("Invasion échouée. Le système est désormais inoccupé.");
			targetHex.setCurrentOccupant(null);
		} else {
			System.out.println("Invasion réussie ! Vous contrôlez maintenant le système.");
			targetHex.setCurrentOccupant(player);
			targetHex.setShips(invasionFleet);
		}

		// Chaque vaisseau ne peut être utilisé qu'une fois par round, donc marquer les vaisseaux comme "utilisés"
		invasionFleet.forEach(ship -> ship.markAsUsed(true));
	}
}
