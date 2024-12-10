package pimperium;

import java.util.ArrayList;
import java.util.List;

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

	public void resolveConflict() {

	}

	public void invade(List<Ship> ships, int idhex) {
		Hex hexToGo = game.getGround().getHexById(idhex);
		// Prendre en compte le fait que le Hex ne doit pas appartenir au joueur
//		if (hexToGo.getCurrentOccupant().equals()) {
//			// Regarder parmi les voisins du Hex
	}
//

		public void execute(int currentRound) {
			System.out.println(player.getName() + " envahit un système"); // Pour le débuggage
//			invade(player.getShipsSurPlateau(), currentRound);
	}
}
