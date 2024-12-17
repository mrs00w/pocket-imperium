package pimperium;

import java.util.*;

public class Ground {
	//faire une liste dynamique pour ajouter les cartes une par une
	private static List<Hex> hexes;
	private List<SectorCard> sectorcards;
	private int centralCardIndex;

	public Ground() {
		this.sectorcards = new ArrayList<SectorCard>();
		for (int i=1; i<10; i++) {
			sectorcards.add(new SectorCard(i));
		}
		this.hexes = new ArrayList<Hex>();
		this.setupGround();
	}

	public List<Hex> getHexes() {
			return hexes;
	}

	public Hex getHexById(int id) {
		if (hexes.stream().anyMatch((Hex hex) -> hex.getIdHex() == id)) { //On vérifie que l'hexagone existe
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
		sectorcards.get(1).getHexes().add(hexes.get(0));
		hexes.add(new Hex(21, 0));
		sectorcards.get(1).getHexes().add(hexes.get(1));
		hexes.add(new Hex(31, 1));
		sectorcards.get(2).getHexes().add(hexes.get(2));
		hexes.add(new Hex(41, 0));
		sectorcards.get(2).getHexes().add(hexes.get(3));
		hexes.add(new Hex(51, 1));
		sectorcards.get(3).getHexes().add(hexes.get(4));
		hexes.add(new Hex(61, 1));
		sectorcards.get(3).getHexes().add(hexes.get(5));

		hexes.add(new Hex(12, 1));
		sectorcards.get(1).getHexes().add(hexes.get(6));
		hexes.add(new Hex(22, 0));

		hexes.add(new Hex(32, 1));
		sectorcards.get(2).getHexes().add(hexes.get(8));
		hexes.add(new Hex(42, 0));

		hexes.add(new Hex(52, 0));
		sectorcards.get(3).getHexes().add(hexes.get(10));

		hexes.add(new Hex(13, 0));
		sectorcards.get(1).getHexes().add(hexes.get(11));
		hexes.add(new Hex(23, 1));
		sectorcards.get(1).getHexes().add(hexes.get(12));
		hexes.add(new Hex(33, 2));
		sectorcards.get(2).getHexes().add(hexes.get(13));
		hexes.add(new Hex(43, 0));
		sectorcards.get(2).getHexes().add(hexes.get(14));
		hexes.add(new Hex(53, 0));
		sectorcards.get(3).getHexes().add(hexes.get(15));
		hexes.add(new Hex(63, 2));
		sectorcards.get(3).getHexes().add(hexes.get(16));

		hexes.add(new Hex(14, 2));
		sectorcards.get(1).getHexes().add(hexes.get(17));
		hexes.add(new Hex(24, 0));
		// Là c'est des demi, je ne sais pas encore où les mettre dans cette version. Ou peut être que si
		// je vais voir
		hexes.add(new Hex(34, 3));
		sectorcards.get(2).getHexes().add(hexes.get(19));
		hexes.add(new Hex(44, 0));

		hexes.add(new Hex(54, 1));
		sectorcards.get(3).getHexes().add(hexes.get(21));

		hexes.add(new Hex(15, 1));
		sectorcards.get(1).getHexes().add(hexes.get(22));
		hexes.add(new Hex(25, 0));
		sectorcards.get(1).getHexes().add(hexes.get(23));
		hexes.add(new Hex(35, 3));
		sectorcards.get(2).getHexes().add(hexes.get(24));
		hexes.add(new Hex(45, 3));
		sectorcards.get(2).getHexes().add(hexes.get(25));
		hexes.add(new Hex(55, 0));
		sectorcards.get(3).getHexes().add(hexes.get(26));
		hexes.add(new Hex(65, 2));
		sectorcards.get(3).getHexes().add(hexes.get(27));

		hexes.add(new Hex(16, 1));
		sectorcards.get(1).getHexes().add(hexes.get(28));
		hexes.add(new Hex(26, 0));

		hexes.add(new Hex(36, 3));
		sectorcards.get(2).getHexes().add(hexes.get(30));
		hexes.add(new Hex(46, 0));

		hexes.add(new Hex(56, 1));
		sectorcards.get(3).getHexes().add(hexes.get(32));

		hexes.add(new Hex(17, 1));
		sectorcards.get(1).getHexes().add(hexes.get(33));
		hexes.add(new Hex(27, 1));
		sectorcards.get(1).getHexes().add(hexes.get(34));
		hexes.add(new Hex(37, 0));
		sectorcards.get(2).getHexes().add(hexes.get(35));
		hexes.add(new Hex(47, 1));
		sectorcards.get(2).getHexes().add(hexes.get(36));
		hexes.add(new Hex(57, 0));
		sectorcards.get(3).getHexes().add(hexes.get(37));
		hexes.add(new Hex(67, 0));
		sectorcards.get(3).getHexes().add(hexes.get(38));

		hexes.add(new Hex(18, 2));
		sectorcards.get(1).getHexes().add(hexes.get(39));
		hexes.add(new Hex(28, 0));

		hexes.add(new Hex(38, 2));
		sectorcards.get(2).getHexes().add(hexes.get(41));
		hexes.add(new Hex(48, 0));

		hexes.add(new Hex(58, 2));
		sectorcards.get(3).getHexes().add(hexes.get(43));

		hexes.add(new Hex(19, 0));
		sectorcards.get(1).getHexes().add(hexes.get(44));
		hexes.add(new Hex(29, 0));
		sectorcards.get(1).getHexes().add(hexes.get(45));
		hexes.add(new Hex(39, 0));
		sectorcards.get(2).getHexes().add(hexes.get(46));
		hexes.add(new Hex(49, 1));
		sectorcards.get(2).getHexes().add(hexes.get(47));
		hexes.add(new Hex(59, 1));
		sectorcards.get(3).getHexes().add(hexes.get(48));
		hexes.add(new Hex(69, 1));
		sectorcards.get(3).getHexes().add(hexes.get(49));

		for (Hex h: hexes) {
			creerListeVoisins(h);
		}
		// Pour vérifier la liste de voisin des hexs
		/*
		for (Hex h: hexes){
			System.out.print(h.getIdHex() + " et ses voisins : ");
			for (Hex h1: h.getNeighbors()){
				System.out.println(h1.getIdHex());
			}
		} */
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
		SectorCard temp = sectorcards.get(index1);
		sectorcards.set(index1, sectorcards.get(index2));
		sectorcards.set(index2, temp);
	}

	public void rotateCard(int index) {
		if (index == centralCardIndex) {
			System.out.println("La carte centrale ne peut pas être tournée !");
			return;
		}
		sectorcards.get(index).rotate();
	}

	public void swapAndRotateTopBottom(int indexTop, int indexBottom) {
		if (indexTop < 0 || indexTop > 2 || indexBottom < 6 || indexBottom > 8) {
			System.out.println("Indices invalides ! Les cartes doivent être dans les lignes supérieures ou inférieures.");
			return;
		}

		swapCards(indexTop, indexBottom);

		// Faire une rotation de 180° sur les deux cartes
		sectorcards.get(indexTop).rotate();
		sectorcards.get(indexBottom).rotate();
	}

	public void shuffleMap() {
		Random random = new Random();

		// Étape 1 : Mélanger les cartes latérales (3 et 5) avec 50% de chance
		if (random.nextBoolean()) { // 50% de chance
			SectorCard temp = sectorcards.get(3);
			sectorcards.set(3, sectorcards.get(5));
			sectorcards.set(5, temp);
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
				SectorCard temp = sectorcards.get(topIndex);
				sectorcards.set(topIndex, sectorcards.get(bottomIndex));
				sectorcards.set(bottomIndex, temp);

				// Rotation de 180° pour l'alignement
				sectorcards.get(topIndex).rotate();
				sectorcards.get(bottomIndex).rotate();
			}
		}

		// Étape 4 : La carte centrale reste fixe (indice 4)
		// Rien à faire ici
	}
}
