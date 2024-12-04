package pimperium;

public class Ship {
	private Hex position;

	public Ship() {
		//début partie, ship en dehors du terrain de jeu
		this.position = null;
	}

	public void invadeSystem(Hex h) {

	}

	public void updatePosition(int idHex) {
		Hex targetHex = Game.getInstance(null).getGround().getHexById(idHex);

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
