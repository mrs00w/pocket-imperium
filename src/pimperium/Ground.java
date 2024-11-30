package pimperium;

import java.util.List;

public class Ground {
	//faire une liste dynamique pour ajouter les cartes une par une
	private SectorCard[] sectorcards;
	private List<Hex> hexes;
	
	public Ground() {
		//Mise en place du terrain au moment de l'initialisation
		for (int i=0;i<23;i++) {
			hexes.add(new Hex(i, 0, false));
		}
		for (int i=0;i<16;i++) {
			hexes.add(new Hex(i, 1, false));
		}
		for (int i=0;i<7;i++) {
			hexes.add(new Hex(i, 2, false));
		}
		for (int i=0; i<4; i++) {
			hexes.add(new Hex(i, 3, true));
		}
	}

	public Hex getHexById(int id) {
		if (id >= 0 && id < getHexes().size()) {
			hexes = getHexes();
			return hexes.get(id);  // Retourner l'Hex à l'index correspondant à l'ID
		}
		return null;  // Retourner null si l'ID est invalide
	}

	public List<Hex> getHexes() {
		return hexes;
	}
}
//	public List<Hex> setGround() {
//		for (int i=0;i<23;i++) {
//			hexes.add(new Hex(i, 0, false));
//		}
//		for (int i=0;i<16;i++) {
//			hexes.add(new Hex(i, 1, false));
//		}
//		for (int i=0;i<16;i++) {
//			hexes.
//		}
//	}
