package pimperium;

import java.util.*;

public class Ground {
	//faire une liste dynamique pour ajouter les cartes une par une
	private SectorCard[] sectorcards;
	private List<Hex> hexes;
	private int colums;
	private int rows;
	private int centralCardIndex;

	public Ground() {
		this.rows = 3;
		this.colums = 3;
		this.sectorcards = new SectorCard[9];
		this.hexes = new ArrayList<Hex>();
		for (int i = 0; i < 9; i++) {
			sectorcards[i] = new SectorCard(i, i == centralCardIndex); // Marquer la carte centrale
		}
		//Mise en place du terrain au moment de l'initialisation
//		List<Hex> hexes = new ArrayList<>();
//		for (int i=0;i<23;i++) {
//			hexes.add(new Hex(i, 0, false));
//		}
//		for (int i=0;i<16;i++) {
//			hexes.add(new Hex(i, 1, false));
//		}
//		for (int i=0;i<7;i++) {
//			hexes.add(new Hex(i, 2, false));
//		}
//		for (int i=0; i<4; i++) {
//			hexes.add(new Hex(i, 3, true));
//		}
	}

	public List<Hex> getHexes() {
			return hexes;
	}

	public Hex getHexById(int id) {
		if (id >= 0 && id < getHexes().size()) {
			hexes = getHexes();
			return hexes.get(id);  // Retourner l'Hex à l'index correspondant à l'ID
		}
		return null;  // Retourner null si l'ID est invalide
	}

	public void setupGround() {
		// par convention, l'id est setup comme suis :
		// le premier chiffre : colonne
		// le second chiffre : ligne
		hexes.add(new Hex(11, 2));
		hexes.add(new Hex(21, 0));
		hexes.add(new Hex(31, 1));
		hexes.add(new Hex(41, 0));
		hexes.add(new Hex(51, 1));
		hexes.add(new Hex(61, 1));

		hexes.add(new Hex(12, 1));
		hexes.add(new Hex(22, 0));
		hexes.add(new Hex(32, 1));
		hexes.add(new Hex(42, 0));
		hexes.add(new Hex(52, 0));

		hexes.add(new Hex(13, 0));
		hexes.add(new Hex(23, 1));
		hexes.add(new Hex(33, 2));
		hexes.add(new Hex(43, 0));
		hexes.add(new Hex(53, 0));
		hexes.add(new Hex(63, 2));

		hexes.add(new Hex(14, 2));
		hexes.add(new Hex(24, 0));
		hexes.add(new Hex(34, 3));
		hexes.add(new Hex(44, 0));
		hexes.add(new Hex(54, 1));

		hexes.add(new Hex(15, 1));
		hexes.add(new Hex(25, 0));
		hexes.add(new Hex(35, 3));
		hexes.add(new Hex(45, 3));
		hexes.add(new Hex(55, 0));
		hexes.add(new Hex(65, 2));

		hexes.add(new Hex(16, 1));
		hexes.add(new Hex(26, 0));
		hexes.add(new Hex(36, 3));
		hexes.add(new Hex(46, 0));
		hexes.add(new Hex(56, 1));

		hexes.add(new Hex(17, 1));
		hexes.add(new Hex(27, 1));
		hexes.add(new Hex(37, 0));
		hexes.add(new Hex(47, 1));
		hexes.add(new Hex(57, 0));
		hexes.add(new Hex(67, 0));

		hexes.add(new Hex(18, 2));
		hexes.add(new Hex(28, 0));
		hexes.add(new Hex(38, 2));
		hexes.add(new Hex(48, 0));
		hexes.add(new Hex(58, 2));

		hexes.add(new Hex(19, 0));
		hexes.add(new Hex(29, 0));
		hexes.add(new Hex(39, 0));
		hexes.add(new Hex(49, 1));
		hexes.add(new Hex(59, 1));
		hexes.add(new Hex(69, 1));

		for (Hex h: hexes) {
			creerListeVoisins(h);
		}
	}

	// ***************************************************************

	private Hex findHexByColAndLigne(int col, int ligne) {
		return hexes.stream()
				.filter(h -> h.getCol() == col && h.getLigne() == ligne)
				.findFirst()
				.orElse(null);
	}

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

	public void swapCards(int index1, int index2) {
		if (index1 == centralCardIndex || index2 == centralCardIndex) {
			System.out.println("La carte centrale ne peut pas être déplacée !");
			return;
		}
		SectorCard temp = sectorcards[index1];
		sectorcards[index1] = sectorcards[index2];
		sectorcards[index2] = temp;
	}

	public void rotateCard(int index) {
		if (index == centralCardIndex) {
			System.out.println("La carte centrale ne peut pas être tournée !");
			return;
		}
		sectorcards[index].rotate();
	}

	public void swapAndRotateTopBottom(int indexTop, int indexBottom) {
		if (indexTop < 0 || indexTop > 2 || indexBottom < 6 || indexBottom > 8) {
			System.out.println("Indices invalides ! Les cartes doivent être dans les lignes supérieures ou inférieures.");
			return;
		}

		swapCards(indexTop, indexBottom);

		// Faire une rotation de 180° sur les deux cartes
		sectorcards[indexTop].rotate();
		sectorcards[indexBottom].rotate();
	}

	public void shuffleMap() {
		Random random = new Random();

		// Étape 1 : Mélanger les cartes latérales (3 et 5) avec 50% de chance
		if (random.nextBoolean()) { // 50% de chance
			SectorCard temp = sectorcards[3];
			sectorcards[3] = sectorcards[5];
			sectorcards[5] = temp;
		}

		// Étape 2 : Mélanger les cartes du haut (0, 1, 2) et du bas (6, 7, 8)
		ArrayList<Integer> topIndices = new ArrayList<>(List.of(0, 1, 2));
		ArrayList<Integer> bottomIndices = new ArrayList<>(List.of(6, 7, 8));

		// Mélange des cartes en haut
		Collections.shuffle(topIndices);
		// Mélange des cartes en bas
		Collections.shuffle(bottomIndices);

		// Étape 3 : Échanger entre le haut et le bas en gérant la rotation
		for (int i = 0; i < 3; i++) {
			if (random.nextBoolean()) { // 50% chance d'échanger haut <-> bas
				// Indices correspondants
				int topIndex = topIndices.get(i);
				int bottomIndex = bottomIndices.get(i);

				// Swap entre haut et bas avec rotation
				SectorCard temp = sectorcards[topIndex];
				sectorcards[topIndex] = sectorcards[bottomIndex];
				sectorcards[bottomIndex] = temp;

				// Rotation de 180° pour l'alignement
				sectorcards[topIndex].rotate();
				sectorcards[bottomIndex].rotate();
			}
		}

		// Étape 4 : La carte centrale reste fixe (indice 4)
		// Rien à faire ici
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
