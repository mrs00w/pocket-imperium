package pimperium;

public class Ship {
	private Hex position;
	private Player player;
	private boolean used;

	public Ship(Player player) {
		//début partie, ship en dehors du terrain de jeu
		this.position = null;
		this.player = player;
		this.used = used;
	}

	public boolean isUsed() {
		return used;
	}

	public void markAsUsed(boolean used) {
		this.used = used;
	}

	public Player getPlayer() {
		return player;
	}

	public void invadeSystem(Hex h) {

	}

	@Override
	public String toString() {
		return "position : "+this.position+" du joueur "+this.player;
	}

	public void updatePosition(Hex h) {
//		this.position = h;
	}

	public Hex getPosition() {
		return this.position;  // Cette variable stocke la position actuelle du vaisseau
	}

	public void setPosition(Hex newHex){
		this.position=(Hex) newHex;
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
