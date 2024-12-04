package pimperium;

import java.util.ArrayList;

public class ExterminateCard implements CommandCard{

	private Player player;

	public ExterminateCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
		int priority = 3;
		return priority;
	}

	public void resolveConflict() {

	}

	public void invade(ArrayList<Ship> ships, int idhex) {
		Game game = Game.getInstance(null);
		Hex hexToGo = game.getGround().getHexById(idhex);
		// Prendre en compte le fait que le Hex ne doit pas appartenir au joueur
		if (hexToGo.getCurrentOccupant() == "") {

		}
	}

	public void execute(int currentRound) {

	}
}
