package pimperium;

public class Ship {
	private int positionHex;
	private Hex position;
	private Player player;

	public Ship() {
		//début partie, ship en dehors du terrain de jeu
		this.position = null;
		this.positionHex = 0;
		this.player = player;
	}

	public void invadeSystem(Hex h) {

	}

	public void updatePosition(Hex h) {
//		this.position = h;
	}

	public Hex getPosition() {
		return this.position;  // Cette variable stocke la position actuelle du vaisseau
	}

	public Ship findShipInHex(Hex targetHex) {
		for (Ship ship : player.getShipsSurPlateau()) {
			if (ship.getPosition() == targetHex) {  // Comparer la position du vaisseau avec l'Hex donné
				return ship;
			}
		}
		return null;  // Aucun vaisseau trouvé dans cet Hex
	}
}
