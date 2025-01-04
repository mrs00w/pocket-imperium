package pimperium;

import java.util.*;

public class SectorCard {
	private String typecard;
	private int id; // Identifiant unique de la carte
	private int[] idSystems;
	private boolean isCentral; // Vrai si c'est la carte centrale
	private boolean canRotate; // Orientation actuelle : "normal" ou "rotated180"
	private boolean hasShips=false;
	private List<Hex> hexes;
	private String orientation;
	private int positionOnMap;

	public SectorCard(int id) {
		this.id = id;
		this.typecard = typecard;
		this.canRotate = canRotate; // Orientation par défaut
		this.hexes = new ArrayList<>();
	}

	public SectorCard(int id, int positionSystem11, int positionSystem12, int positionSystem2) {
		this.id = id;
		this.idSystems = new int[3];
		idSystems[0] = positionSystem11;
		idSystems[1] = positionSystem12;
		idSystems[2] = positionSystem2;
	}

	public int getId() {
		return id;
	}

	public void setHasShips(boolean hasShips) {
		this.hasShips = hasShips;
	}

	public boolean getHasShips(){
		return this.hasShips;
	}

	public void setPositionOnMap(int positionOnMap) {
		this.positionOnMap = positionOnMap;
	}

	public int getPositionOnMap() {
		return positionOnMap;
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
