package pimperium;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExpandCard implements CommandCard{

	private final Player player;

	public ExpandCard(Player player) {
		this.player = player;
	}

    public int getPriority() {
        return 1;
	}

	public void execute(int currentRound) {
		System.out.println(player.getName() + " ajoute de nouveaux vaisseaux sur le plateau");
		Game game = Game.getInstance();
		List<Player> players = new ArrayList<>(game.getPlayers());
		List<Player> expandPlayers = players.stream()
				.filter(player -> player.getCard(currentRound) instanceof ExpandCard)
				.toList();

		int expandCount = expandPlayers.size();

		int shipsToMove = switch (expandCount) {
			case 1 -> 3;  // Si expandCount est 1, on retourne 3
			case 2 -> 2;  // Si expandCount est 2, on retourne 2
			default -> 1; // Si expandCount est autre chose, on retourne 1
		};

		for (int i=0; i < shipsToMove; i++) {
			Ship currentShip = player.getShipsHorsPlateau().pop();
			player.getShipsSurPlateau().add(currentShip);
			Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println("Où voulez-vous placer votre vaisseau n°" + i + " :");
			int hex = reader.nextInt();
			currentShip.updatePosition(game.getGround().getHexById(hex));
			// Introduire condition : seulement 2 Hex
			reader.close();
		}

	}
}
