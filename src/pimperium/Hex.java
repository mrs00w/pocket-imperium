package pimperium;

public class Hex {
	private Player currentOccupant;
	private int idHex;
	private int levelSystem;
	private boolean isTriPrime;
	
	public Hex(int id, int lvl, boolean tp) {
		this.currentOccupant = null;
		//implémenter l'id en fonction du nombre d'Hex créés précédemment
		this.idHex = id;
		//Vérifier que bien compris entre 0 et 3
		this.levelSystem = lvl;
		//peut être à supprimer si on teste levelSystem = 3 au lieu de isTriPrime
		this.isTriPrime = tp;
	}
	public void changeOccupant(Player p) {
		this.currentOccupant = p;
	}
}
