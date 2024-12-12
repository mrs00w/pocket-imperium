package pimperium;
import java.util.*;

public class Hex {
	private Player currentOccupant;
	private static int numHex = 0;
	private int idHex;
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
	}

	public Hex(int id, int lvl, boolean isPartiel) {
		this.currentOccupant = null;
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = id;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		//peut être à supprimer si on teste levelSystem = 3 au lieu de isTriPrime
//		this.isTriPrime = tp;
		this.isPartiel = true;
		this.neighbors = new ArrayList<>();
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

	public List<Hex> getNeightbors() {

		return neighbors;
	}
}
