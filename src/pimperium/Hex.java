package pimperium;
import java.util.*;

/**
 * Repésente un Hex
 */

public class Hex {
	/**
	 * Le joueur occupant Tri Prime
	 */
	private static Player triPrimeOccupant;
	/**
	 * Le joueur occupant cet Hex
	 */
	private Player currentOccupant;
	/**
	 * L'Id de l'Hex
	 */
	private int idHex;
	/**
	 * La position du Hex par rapport au secteur
	 */
	private int positionSector;
	/**
	 * L'id du secteur où se trouve cet Hex
	 */
	private int idSector;
	/**
	 * Le secteur où se trouve cet Hex
	 */
	private SectorCard sector;
	/**
	 * Le chiffre de la colonne de l'Hex
	 */
	private int col;
	/**
	 * Le chiffre de la ligne de l'Hex
	 */
	private int ligne;
	/**
	 * Le niveau du système dans cet Hex
	 */
	private int levelSystem;
	/**
	 * La liste des Hex voisins de cet Hex
	 */
	private List<Hex> neighbors;
	/**
	 * Les vaisseaux présents dans cet Hex
	 */
	private List<Ship> ships;

	/**
	 * Récupère l'id du secteur
	 * @return l'id du secteur
	 */

	public int getSectorId() {
		return this.idSector;
	}

	/**
	 * Récupère le secteur
	 * @return le secteur
	 */

	public SectorCard getSector() {
		return sector;
	}

	/**
	 * Change l'occupant du Tri Prime
	 */

	public static void setTriPrimeOccupant(Player triPrimeOccupant) {
		Hex.triPrimeOccupant = triPrimeOccupant;
	}

	/**
	 * Créé un Hex. Utilisé pour les Hex complet.
	 * @param id id de l'Hex
	 * @param lvl niveau du système
	 * @param positionSector la position dans le secteur
	 * @param idSector l'id du secteur
	 * @param sector le secteur
	 */

	public Hex(int id, int lvl, int positionSector, int idSector, SectorCard sector) {
		this.currentOccupant = null;
		this.idSector = idSector;
		this.sector = sector;
		this.positionSector = positionSector;
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = id;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		this.col = idHex / 10;
		this.ligne = idHex % 10;
		//peut être à supprimer si on teste levelSystem = 3 au lieu de isTriPrime
		this.neighbors = new ArrayList<Hex>();
		this.ships = new ArrayList<Ship>();
	}

	/**
	 * Crée un Hex. Utilisé pour les demi-Hex
	 * @param id id du Hex
	 * @param lvl niveau du système
	 */

	public Hex(int id, int lvl) {
		this.currentOccupant = null;
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = id;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		this.col = idHex / 10;
		this.ligne = idHex % 10;
		//peut être à supprimer si on teste levelSystem = 3 au lieu de isTriPrime
		this.neighbors = new ArrayList<Hex>();
		this.ships = new ArrayList<Ship>();
	}

	/**
	 * Récupère les vaisseaux de l'Hex
	 * @return les vaisseaux dans l'Hex
	 */

	public List<Ship> getShips() {
		return ships;
	}

	/**
	 * Change l'occupant de l'Hex
	 * @param currentOccupant Nouvel occupant de l'Hex
	 */

	public void setCurrentOccupant(Player currentOccupant) {
		this.currentOccupant = currentOccupant;
	}

	/**
	 * Change les vaisseaux présents dans l'Hex
	 * @param ships les vaisseaux présents dans l'Hex
	 */

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	/**
	 * Renvoie le niveau du système
	 * @return le niveau du système
	 */

	public int getLevelSystem() {
		return levelSystem;
	}

	/**
	 * Renvoie la colonne de l'Hex
	 * @return la colonne de l'Hex
	 */

	public int getCol() {
		return col;
	}

	/**
	 * Renvoie la ligne de l'Hex
	 * @return la ligne de l'Hex
	 */

	public int getLigne() {
		return ligne;
	}

	/**
	 * Change les Hex voisins de l'Hex
	 */

	public void setNeighbors(List<Hex> neighbors) {
		this.neighbors = neighbors;
	}

	/**
	 * Renvoie l'occupant de l'Hex
	 * @return l'occupant de l'Hex
	 */

	public Player getCurrentOccupant() {
		if (this.ships.isEmpty()) {
			return null;
		} else {
			return this.ships.getFirst().getPlayer();
		}
	}

	/**
	 * Renvoie l'id de l'Hex
	 * @return l'id de l'Hex
	 */

	public int getIdHex() {
		return idHex;
	}

	/**
	 * Renvoie les voisins de l'Hex
	 * @return les voisins de l'Hex
	 */

	public List<Hex> getNeighbors() {
		return neighbors;
	}

}
