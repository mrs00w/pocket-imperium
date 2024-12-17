package pimperium;
import java.util.*;

public class Hex {
	private Player currentOccupant;
	private static int numHex = 0;
	private int idHex;
	private int col;
	private int ligne;
	private int levelSystem;
	private boolean isTriPrime;
	private boolean isPartiel;
	private List<Hex> neighbors;
	private List<Ship> ships;

	public Hex(int lvl){
		numHex++;
		this.ships = new ArrayList<Ship>();
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = numHex;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		this.neighbors = new ArrayList<Hex>();
		this.currentOccupant=null;
	}

	public Hex(int id, int lvl) {
		this.currentOccupant = null;
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = id;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		this.col = idHex / 10;
		this.ligne = idHex % 10;
		//peut être à supprimer si on teste levelSystem = 3 au lieu de isTriPrime
//		this.isTriPrime = tp;
		this.isPartiel = true;
		this.neighbors = new ArrayList<Hex>();
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setCurrentOccupant(Player currentOccupant) {
		this.currentOccupant = currentOccupant;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public int getLevelSystem() {
		return levelSystem;
	}

	public int getCol() {
		return col;
	}

	public int getLigne() {
		return ligne;
	}

	public void setNeighbors(List<Hex> neighbors) {
		this.neighbors = neighbors;
	}

	public Player getCurrentOccupant() {
		return currentOccupant;
	}

	public void changeOccupant(Player p) {
		this.currentOccupant = p;
	}

	public int getIdHex() {
		return idHex;
	}

	public List<Hex> getNeighbors() {
		return neighbors;
	}

	public void addShip(Ship ship) {
		ships.add(ship);
	}
}
