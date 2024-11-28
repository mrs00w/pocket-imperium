package pimperium;

import java.util.Scanner;

public class Player {
	private static int nombrePlayer = 0;
	private boolean isAlive;
	private int idPlayer;
	private int couleurVaisseau;
	private Ship[] ships;
	private CommandCard[] cards;
	
	public Player() {
		this.isAlive = true;
		this.idPlayer = nombrePlayer;
		nombrePlayer++;
		this.choisirCouleurVaisseau();
		//Ajouter ships et commandcards
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
		
	}
	public void perform(int i) {
		
	}
}
