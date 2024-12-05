package pimperium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
		this.hexes = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			sectorcards[i] = new SectorCard(i, i == centralCardIndex); // Marquer la carte centrale
		}
		//Mise en place du terrain au moment de l'initialisation
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

	public Hex getHexById(int id) {
		if (id >= 0 && id < getHexes().size()) {
			hexes = getHexes();
			return hexes.get(id);  // Retourner l'Hex à l'index correspondant à l'ID
		}
		return null;  // Retourner null si l'ID est invalide
	}

	public void setupGround() {

	}

	public List<Hex> getHexes() {
		return hexes;
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
