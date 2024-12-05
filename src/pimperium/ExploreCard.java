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

		int shipsToMove = switch (exploreCount) {
			case 1 -> 3;  // Si exploreCount est 1, on retourne 3
			case 2 -> 2;  // Si exploreCount est 2, on retourne 2
			default -> 1; // Si exploreCount est autre chose, on retourne 1
		};

		//Stack<Ship> currentShips = player.getShipsSurPlateau();
		for (int i=0; i < shipsToMove; i++) {
			Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println("Quel vaisseau voulez-vous déplacer n°" + i + " ? :");
			int idVaisseau = reader.nextInt();
			reader.close();
		}
	}
}
