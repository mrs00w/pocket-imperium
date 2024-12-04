package pimperium;

import java.util.Scanner;
import java.util.*;

public class Player {
	public String name;
	private static int nombrePlayer = 0;
	private boolean isAlive;
	private int idPlayer;
	private int couleurVaisseau;
	private Stack<Ship> shipsHorsPlateau;
	private List<Ship> shipsSurPlateau;
//	private Set<CommandCard> cards;
	private CommandCard [] cardOrder;
	public int score;
	
	public Player() {
		this.isAlive = true;
		this.score = 0;
		this.idPlayer = nombrePlayer;
		nombrePlayer++;
		this.name = "Player"+nombrePlayer;
		this.choisirCouleurVaisseau();
		this.shipsHorsPlateau = new Stack<Ships>();
		for (int i=0;i<15;i++) {
			shipsHorsPlateau.push(new Ship());
		}
//		Set<CommandCard> cards = new HashSet<CommandCard>();
//		cards.add(new ExpandCard());
//		cards.add(new ExploreCard());
//		cards.add(new ExterminateCard());
//		this.cards = cards;
	}

	public Stack<Ship> getShipsHorsPlateau() {
		return this.shipsHorsPlateau;
	}

	public List<Ship> getShipsSurPlateau() {
		return this.shipsSurPlateau;
	}

	public CommandCard[] getCardOrder() {
		return this.cardOrder;
	}

	public CommandCard getCard(int i) {
		return this.cardOrder[i];
	}

	public void setPlayerName() {
		Scanner reader = new Scanner(System.in); // Reading from System.in
		System.out.println("Enter a player name: ");
		this.name = reader.next(); // Scans the next token of the input as an int
		reader.close();
	}
	
	public void choisirCouleurVaisseau() {
		//détecter erreur dans le cas où ce n'est pas un int qui est renseigné ?
		//ou si int != de 0,1,2
		Scanner inputReader = new Scanner(System.in);
		System.out.println("Choisir votre couleur parmi jaune(0) rouge(1) ou bleu(2)");
		int couleur = inputReader.nextInt();
		this.couleurVaisseau = couleur;
	}
	
	public void plan() {
		for (int i=0; i<3; i++) {
			Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println("Quelle carte voulez-vous jouez en" + i + "(Expand, Explore ou Exterminate) :");
			String r = reader.next();
			// L'ordre est à surveiller, il y a souvent des erreurs de ce côté là
			if (r.toLowerCase() == "expand") {
				cardOrder[i] = expand;
			} else if (r.toLowerCase() == "explore") {
				cardOrder[i] = explore;
			} else if (r.equalsIgnoreCase("exterminate")) {
				cardOrder[i] = exterminate;
			} else {
				System.out.println("Choix invalide. Veuillez recommencer.");
				i--; // Refaire la même position si l'entrée est invalide
			}
		}
		
	}

	public void perform(int currentRound) {
		cardOrder[currentRound].execute(currentRound);
	}

}
