package pimperium;

import java.util.*;

/**
 * Classe représentant le terrain de jeu.
 */
public class Ground {
	/**
	 * Liste dynamique contenant les hexagones (Hex).
	 */
	private static List<Hex> hexes;

	/**
	 * Liste des cartes de secteur.
	 */
	private final List<SectorCard> sectorcards;

	/**
	 * Liste des hexagones représentant le TriPrime.
	 */
	private final List<Hex> triPrime;

	/**
	 * Constructeur de la classe Ground.
	 * Initialise les cartes de secteur, les hexagones et configure le terrain.
	 */

	public Ground() {
		this.sectorcards = new ArrayList<SectorCard>();
		for (int i=1; i<10; i++) {
			sectorcards.add(new SectorCard(i));
		}
		this.hexes = new ArrayList<Hex>();
		this.triPrime = new ArrayList<Hex>();
		this.setupGround();
	}

	/**
	 * Retourne la liste des hexagones formant le TriPrime.
	 *
	 * @return Liste des hexagones du TriPrime.
	 */
	public List<Hex> getTriPrime() {
		return triPrime;
	}

	/**
	 * Retourne le joueur occupant actuellement le TriPrime.
	 *
	 * @return Joueur occupant le TriPrime, ou null si aucun.
	 */

	public Player getTriPrimeOccupant() {
		Player occupant = null;
		for (Hex hex : triPrime) {
			if (hex.getCurrentOccupant() != null) occupant = hex.getCurrentOccupant();
		}
		return occupant;
	}

	/**
	 * Retourne la liste de tous les hexagones du terrain.
	 *
	 * @return Liste des hexagones.
	 */
	public List<Hex> getHexes() {
		return hexes;
	}

	/**
	 * Retourne un hexagone en fonction de son identifiant.
	 *
	 * @param id Identifiant de l'hexagone recherché.
	 * @return Hexagone correspondant à l'identifiant, ou null si non trouvé.
	 */

	public Hex getHexById(int id) {
		Hex hex = null;
		for (Hex h : hexes) {
			if (h.getIdHex() == id) { //On cherche l'hexagone dont l'ID correspond a celui recherché
				hex = h;
			}
		}
		return hex;  //Si l'hex a été trouvé il est renvoyé sinon on renvoie null
	}

	/**
	 * Retourne une carte de secteur en fonction de son identifiant.
	 *
	 * @param id Identifiant de la carte de secteur recherchée.
	 * @return Carte de secteur correspondante, ou null si non trouvée.
	 */

	public SectorCard getSectorById(int id) {
		for (SectorCard sc : sectorcards) {
			if (sc.getId() == id) { //On cherche l'hexagone dont l'ID correspond a celui recherché
				return sc;
			}
		}
		return null;  //Si l'hex a été trouvé il est renvoyé sinon on renvoie null
	}

	/**
	 * Retourne la liste des cartes de secteur.
	 *
	 * @return Liste des cartes de secteur.
	 */
	public List<SectorCard> getSectors() {
		return sectorcards;
	}

	/**
	 * Configure le terrain en initialisant les hexagones et leurs voisins.
	 */
	public void setupGround() {
		// Initialisation de la carte fixe avec des hexagones et des secteurs
		// Par convention, l'id est structuré comme suit :
		// - Premier chiffre : colonne
		// - Deuxième chiffre : ligne

		hexes.add(new Hex(11, 2, 1, 1, getSectorById(1)));
		sectorcards.get(0).getHexes().add(hexes.get(0));
		hexes.add(new Hex(21, 0, 2,1, getSectorById(1)));
		sectorcards.get(0).getHexes().add(hexes.get(1));
		hexes.add(new Hex(31, 1, 1,2, getSectorById(2)));
		sectorcards.get(1).getHexes().add(hexes.get(2));
		hexes.add(new Hex(41, 0, 2,2, getSectorById(2)));
		sectorcards.get(1).getHexes().add(hexes.get(3));
		hexes.add(new Hex(51, 1, 1,3, getSectorById(3)));
		sectorcards.get(2).getHexes().add(hexes.get(4));
		hexes.add(new Hex(61, 1, 2,3, getSectorById(3)));
		sectorcards.get(2).getHexes().add(hexes.get(5));

		hexes.add(new Hex(12, 1, 3,1, getSectorById(1)));
		sectorcards.get(0).getHexes().add(hexes.get(6));
		hexes.add(new Hex(22, 0));

		hexes.add(new Hex(32, 1, 3,2, getSectorById(2)));
		sectorcards.get(1).getHexes().add(hexes.get(8));
		hexes.add(new Hex(42, 0));

		hexes.add(new Hex(52, 0, 3,3, getSectorById(3)));
		sectorcards.get(2).getHexes().add(hexes.get(10));

		hexes.add(new Hex(13, 0, 4,1, getSectorById(1)));
		sectorcards.get(0).getHexes().add(hexes.get(11));
		hexes.add(new Hex(23, 1, 5,1, getSectorById(1)));
		sectorcards.get(0).getHexes().add(hexes.get(12));
		hexes.add(new Hex(33, 2, 4,2, getSectorById(2)));
		sectorcards.get(1).getHexes().add(hexes.get(13));
		hexes.add(new Hex(43, 0, 5,2, getSectorById(2)));
		sectorcards.get(1).getHexes().add(hexes.get(14));
		hexes.add(new Hex(53, 0, 4,3, getSectorById(3)));
		sectorcards.get(2).getHexes().add(hexes.get(15));
		hexes.add(new Hex(63, 2, 5,3, getSectorById(3)));
		sectorcards.get(2).getHexes().add(hexes.get(16));

		hexes.add(new Hex(14, 2, 1,4, getSectorById(4)));
		sectorcards.get(0).getHexes().add(hexes.get(17));
		hexes.add(new Hex(24, 0));
		// Là c'est des demi, je ne sais pas encore où les mettre dans cette version. Ou peut être que si
		// je vais voir
		hexes.add(new Hex(34, 3, 1,5, getSectorById(5)));
		sectorcards.get(1).getHexes().add(hexes.get(19));
		hexes.add(new Hex(44, 0));

		hexes.add(new Hex(54, 1, 1,6, getSectorById(6)));
		sectorcards.get(2).getHexes().add(hexes.get(21));

		hexes.add(new Hex(15, 1, 2,4, getSectorById(4)));
		sectorcards.get(3).getHexes().add(hexes.get(22));
		hexes.add(new Hex(25, 0, 3,4, getSectorById(4)));
		sectorcards.get(3).getHexes().add(hexes.get(23));
		hexes.add(new Hex(35, 3, 2,5, getSectorById(5)));
		sectorcards.get(4).getHexes().add(hexes.get(24));
		hexes.add(new Hex(45, 3, 3,5, getSectorById(5)));
		sectorcards.get(4).getHexes().add(hexes.get(25));
		hexes.add(new Hex(55, 0, 2,6, getSectorById(6)));
		sectorcards.get(5).getHexes().add(hexes.get(26));
		hexes.add(new Hex(65, 2, 3,6, getSectorById(6)));
		sectorcards.get(5).getHexes().add(hexes.get(27));

		hexes.add(new Hex(16, 1, 4,4, getSectorById(4)));
		sectorcards.get(3).getHexes().add(hexes.get(28));
		hexes.add(new Hex(26, 0));

		hexes.add(new Hex(36, 3, 4,5, getSectorById(5)));
		sectorcards.get(4).getHexes().add(hexes.get(30));
		hexes.add(new Hex(46, 0));

		hexes.add(new Hex(56, 1, 4,6, getSectorById(6)));
		sectorcards.get(5).getHexes().add(hexes.get(32));

		hexes.add(new Hex(17, 1, 1,7, getSectorById(7)));
		sectorcards.get(6).getHexes().add(hexes.get(33));
		hexes.add(new Hex(27, 1, 2,7, getSectorById(7)));
		sectorcards.get(6).getHexes().add(hexes.get(34));
		hexes.add(new Hex(37, 0, 1,8, getSectorById(8)));
		sectorcards.get(7).getHexes().add(hexes.get(35));
		hexes.add(new Hex(47, 1, 2,8, getSectorById(8)));
		sectorcards.get(7).getHexes().add(hexes.get(36));
		hexes.add(new Hex(57, 0, 1,9, getSectorById(9)));
		sectorcards.get(8).getHexes().add(hexes.get(37));
		hexes.add(new Hex(67, 0, 2,9, getSectorById(9)));
		sectorcards.get(8).getHexes().add(hexes.get(38));

		hexes.add(new Hex(18, 2, 3,7, getSectorById(7)));
		sectorcards.get(6).getHexes().add(hexes.get(39));
		hexes.add(new Hex(28, 0));

		hexes.add(new Hex(38, 2, 3,8, getSectorById(8)));
		sectorcards.get(7).getHexes().add(hexes.get(41));
		hexes.add(new Hex(48, 0));

		hexes.add(new Hex(58, 2, 3,9, getSectorById(9)));
		sectorcards.get(8).getHexes().add(hexes.get(43));

		hexes.add(new Hex(19, 0, 4,7, getSectorById(7)));
		sectorcards.get(6).getHexes().add(hexes.get(44));
		hexes.add(new Hex(29, 0, 5,7, getSectorById(7)));
		sectorcards.get(6).getHexes().add(hexes.get(45));
		hexes.add(new Hex(39, 0, 4,8, getSectorById(8)));
		sectorcards.get(7).getHexes().add(hexes.get(46));
		hexes.add(new Hex(49, 1, 5,8, getSectorById(8)));
		sectorcards.get(7).getHexes().add(hexes.get(47));
		hexes.add(new Hex(59, 1, 4,9, getSectorById(9)));
		sectorcards.get(8).getHexes().add(hexes.get(48));
		hexes.add(new Hex(69, 1, 5,9, getSectorById(9)));
		sectorcards.get(8).getHexes().add(hexes.get(49));

		// Ajout d'autres hexagones...
		// (Code de configuration détaillé)

		// Définition des voisins pour chaque hexagone

		for (Hex h: hexes) {
			creerListeVoisins(h);
		}
		// Ajout des hexagones spécifiques au TriPrime
		getTriPrime().add(hexes.get(19));
		getTriPrime().add(hexes.get(24));
		getTriPrime().add(hexes.get(25));
		getTriPrime().add(hexes.get(30));
	}

	/**
	 * Recherche un hexagone en fonction de sa colonne et de sa ligne.
	 *
	 * @param col Colonne de l'hexagone recherché.
	 * @param ligne Ligne de l'hexagone recherché.
	 * @return Hexagone correspondant, ou null si non trouvé.
	 */

	private Hex findHexByColAndLigne(int col, int ligne) {
		return hexes.stream()
				.filter(h -> h.getCol() == col && h.getLigne() == ligne)
				.findFirst()
				.orElse(null);
	}

	/**
	 * Crée la liste des voisins pour un hexagone donné.
	 *
	 * @param h Hexagone pour lequel les voisins doivent être définis.
	 */

	public void creerListeVoisins(Hex h) {
		for (Hex hex: hexes) {
			int col = hex.getCol();
			int ligne = hex.getLigne();
			List<Hex> neighbors = new ArrayList<>();

			// Calculer les voisins potentiels selon la parité de la ligne
			if (ligne % 2 == 0) { // Ligne paire
				neighbors.add(findHexByColAndLigne(col, ligne - 1)); // Haut-gauche
				neighbors.add(findHexByColAndLigne(col + 1, ligne - 1)); // Haut-droit
				neighbors.add(findHexByColAndLigne(col - 1, ligne)); // Gauche
				neighbors.add(findHexByColAndLigne(col + 1, ligne)); // Droite
				neighbors.add(findHexByColAndLigne(col, ligne + 1)); // Bas-gauche
				neighbors.add(findHexByColAndLigne(col + 1, ligne + 1)); // Bas-droit
			} else { // Ligne impaire
				neighbors.add(findHexByColAndLigne(col - 1, ligne - 1)); // Haut-gauche
				neighbors.add(findHexByColAndLigne(col, ligne - 1)); // Haut-droit
				neighbors.add(findHexByColAndLigne(col - 1, ligne)); // Gauche
				neighbors.add(findHexByColAndLigne(col + 1, ligne)); // Droite
				neighbors.add(findHexByColAndLigne(col - 1, ligne + 1)); // Bas-gauche
				neighbors.add(findHexByColAndLigne(col, ligne + 1)); // Bas-droit
			}

			// Filtrer les voisins nulls (si l'Hex correspondant n'existe pas)
			neighbors.removeIf(Objects::isNull);

			// Ajouter la liste des voisins à l'Hex
			hex.setNeighbors(neighbors);
		}
	}
}
