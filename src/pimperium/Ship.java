package pimperium;

public class Ship {
	private Hex position;
	
	public Ship() {
		//début partie, ship en dehors du terrain de jeu
		this.position = null;
	}
	public void invadeSystem(Hex h) {
		
	}
	public void updatePosition(Hex h) {
		this.position = h;
	}
}
