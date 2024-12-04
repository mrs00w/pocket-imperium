package pimperium;

import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;

public class ExploreCard implements CommandCard{
	private final Player player;

	public ExploreCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
		int priority = 2;
		return priority;
	}

	public void execute(int currentRound) {
		Game game = Game.getInstance(null);
		List<Player> players = new ArrayList<>(game.getPlayers());
		List<Player> explorePlayers = players.stream()
				.filter(player -> player.getCard(currentRound) instanceof ExpandCard)
				.toList();

		int exploreCount = explorePlayers.size();

		int shipsToMoveInt = switch (exploreCount) {
			case 1 -> 3;  // Si exploreCount est 1, on retourne 3
			case 2 -> 2;  // Si exploreCount est 2, on retourne 2
			default -> 1; // Si exploreCount est autre chose, on retourne 1
		};

		Stack<Ship> currentShips = player.getShipsSurPlateau();
		for (int i=0; i < shipsToMoveInt; i++) {
			Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println("Quels vaisseaux voulez-vous déplacer ? (Entrez l'Hex dans lequel ils sont situés) :");
			int hex = reader.nextInt();
			List<Ship> shipsInHex = player.findShipsByHexId(hex);
			List<Ship> shipsToMove = player.chooseShipsToMove(shipsInHex);
			System.out.println("Où voulez vous le déplacer ? :");
			int newHexInt = reader.nextInt();
			Hex newHex = game.getGround().getHexById(newHexInt);
			for (Ship s: shipsToMove) {
				s.updatePosition(newHex);
			}
			reader.close();
		}
	}
}
