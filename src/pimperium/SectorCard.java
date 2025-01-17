package pimperium;

import java.util.*;

/**
 * Représente une carte Secteur
 */

public class SectorCard {
	/**
	 * Identifiant du secteur
	 */
	private int id; // Identifiant unique de la carte
	/**
	 * Booléan qui indique si des vaisseaux sont présents dans le secteur
	 */

	private boolean hasShips=false;
	/**
	 * Liste des Hex appartenant secteur
	 */
	private final List<Hex> hexes;
	/**
	 * Set contenant les joueurs présents dans le secteur
	 */

	private final Set<Player> playerInSector;

	/**
	 * Crée un secteur
	 * @param id identifiant du secteur
	 */

	public SectorCard(int id) {
		this.id = id;
		this.playerInSector = new HashSet<>();// Orientation par défaut
		this.hexes = new ArrayList<>();
	}

	/**
	 * Renvoie l'id du secteur
	 * @return l'id du secteur
	 */

	public int getId() {
		return id;
	}

	/**
	 * Indique si des vaisseaux sont présents dans le secteur
	 * @param hasShips boolean
	 */

	public void setHasShips(boolean hasShips) {
		this.hasShips = hasShips;
	}

	/**
	 * Récupère le marqueur qui indique si des vaisseaux sont présents dans le secteur
	 * @return boolean
	 */

	public boolean getHasShips(){
		return this.hasShips;
	}

	/**
	 * Renvoie les Hex du secteur
	 * @return Liste contenant les Hex du secteur
	 */

	public List<Hex> getHexes() {
		return hexes;
	}

}
