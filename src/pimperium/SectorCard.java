package pimperium;

import java.util.*;

public class SectorCard {
	private String typecard;
	private int id; // Identifiant unique de la carte
	private boolean isCentral; // Vrai si c'est la carte centrale
	private boolean canRotate; // Orientation actuelle : "normal" ou "rotated180"
	private List<Hex> hexes;
	private String orientation;

	public SectorCard(int id) {
		this.id = id;
		this.typecard = typecard;
		this.canRotate = canRotate; // Orientation par défaut
		this.hexes = new ArrayList<>();
	}

	public List<Hex> getHexes() {
		return hexes;
	}

	public void setHexes(List<Hex> hexes) {
		this.hexes = hexes;
	}

	public void rotate() {
		if (!isCentral) {
			this.orientation = this.orientation.equals("normal") ? "rotated180" : "normal";
		}
	}


}
