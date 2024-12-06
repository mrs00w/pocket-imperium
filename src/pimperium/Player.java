package pimperium;

import java.util.Scanner;
import java.util.*;

public class Player {
	private String name;
	private static int nombrePlayer = 0;
	private boolean isAlive;
	private int idPlayer;
	private int couleurVaisseau;
	private Stack<Ship> shipsHorsPlateau;
	private List<Ship> shipsSurPlateau;
//	private Set<CommandCard> cards;
	private CommandCard[] commandCards = {new ExpandCard(this), new ExploreCard(this), new ExterminateCard(this)};
	private CommandCard[] cardOrder;
	private int score;
	
	public Player() {
		this.isAlive = true;
		this.score = 0;
		this.idPlayer = nombrePlayer;
		nombrePlayer++;
		this.name = "Player" + nombrePlayer;
		this.couleurVaisseau = this.idPlayer;
		//this.choisirCouleurVaisseau();
		//imposer la couleur aux joueurs finalement

		//this.cardOrder = new CommandCard[3];
		//this.shipsHorsPlateau = new Stack<Ship>();
//		for (int i=0;i<15;i++) {
//			shipsHorsPlateau.push(new Ship());
//		}
		//le temps des tests

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
		return this.commandCards;
	}

	public CommandCard getCard(int i) {
		return this.commandCards[i];
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
				cardOrder[i] = commandCards[0];
			} else if (r.toLowerCase() == "explore") {
				cardOrder[i] = commandCards[1];
			} else if (r.equalsIgnoreCase("exterminate")) {
				cardOrder[i] = commandCards[2];
			} else {
				System.out.println("Choix invalide. Veuillez recommencer.");
				i--; // Refaire la même position si l'entrée est invalide
			}
		}

	}

	public void perform(int currentRound) {
		cardOrder[currentRound].execute(currentRound);
	}

	@Override
	public String toString() {
		return "Player name : "+name;
	}
}
