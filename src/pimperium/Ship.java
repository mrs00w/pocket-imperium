package pimperium;

/**
 * Représente un vaisseau
 */

public class Ship {
	/**
	 * La position du vaisseau
	 */
	private Hex position;
	/**
	 * Le joueur qui possède le vaisseau
	 */
	private Player player;
	/**
	 * Marqueur qui indique si le vaisseau a déjà été utilisé ce tour
	 */
	private boolean used;

	/**
	 * Représente un vaisseau
	 * @param player le joueur possédant le vaisseau
	 */

	public Ship(Player player) {
		//début partie, ship en dehors du terrain de jeu
		this.position = null;
		this.player = player;
		this.used = false;
	}

	/**
	 * Renvoie si le vaisseau a été utilisé
	 * @return marqueur indiquant si le vaisseau a déjà été utilisé ce tour
	 */

	public boolean isUsed() {
		return used;
	}

	/**
	 * Place un marqueur utilisé ou retire ce marqueur de ce vaisseau
	 * @param used boolean qui indique si le vaisseau est utilisé
	 */

	public void setUsed(boolean used) {
		this.used = used;
	}

	/**
	 * Renvoie le joueur qui possède le vaisseau
	 * @return le joueur qui possède le vaisseau
	 */

	public Player getPlayer() {
		return player;
	}

	/**
	 * Renvoie la position et le joueur possédant le vaisseau
	 * @return la position et le joueur possédant le vaisseau
	 */

	@Override
	public String toString() {
		return "position : "+this.position+" du joueur "+this.player;
	}

	/**
	 * Change la position du vaisseau
	 * @param h la nouvelle position du vaisseau
	 */

	public void updatePosition(Hex h) {
		this.position = h;
	}

	/**
	 * Récupère la position du vaisseau
	 * @return la position du vaisseau
	 */

	public Hex getPosition() {
		return this.position;  // Cette variable stocke la position actuelle du vaisseau
	}

	/**
	 * Change la position du vaisseau
	 * @param newHex la nouvelle position du vaisseau
	 */

	public void setPosition(Hex newHex){
		this.position= newHex;
	}

	/**
	 * Renvoie un vaisseau dans l'Hex de destination
	 * @param targetHex l'Hex de destination
	 * @return le vaisseau présent dans l'Hex de destination
	 */

	public Ship findShipInHex(Hex targetHex) {
		for (Ship ship : player.getShipsSurPlateau()) {
			if (ship.getPosition() == targetHex) {  // Comparer la position du vaisseau avec l'Hex donné
				return ship;
			}
		}
		return null;  // Aucun vaisseau trouvé dans cet Hex
	}
}
