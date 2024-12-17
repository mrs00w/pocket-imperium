package pimperium;

import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

public class Player {
	private String name;
	private static int nombrePlayer = 0;
	private boolean isAlive;
	private int idPlayer;
	private int couleurVaisseau;
	private Stack<Ship> shipsHorsPlateau;
	private List<Ship> shipsSurPlateau = new ArrayList<Ship>();
//	private Set<CommandCard> cards;
	private CommandCard[] commandCards = {new ExpandCard(this), new ExploreCard(this), new ExterminateCard(this)};
	private CommandCard[] cardOrder = commandCards;
	//A l'initialisation, l'ordre des cartes est par défaut expand, explore et exterminate
	private int score;
	private int priority;

	public Player() {
		this.isAlive = true;
		this.score = 0;
		this.idPlayer = nombrePlayer;
		this.priority=nombrePlayer;
		nombrePlayer++;
		this.name = "Player"+nombrePlayer;
		this.cardOrder = new CommandCard[3];
        //this.choisirCouleurVaisseau();
        //imposer la couleur aux joueurs finalement
        this.shipsHorsPlateau = new Stack<Ship>();
		for (int i=0;i<15;i++) {
			shipsHorsPlateau.push(new Ship(this));
		}
		//Avant même le premier tour, chaque joueur doit choisir le premier système sur lequel s'installer
	}

//	private void addNewShips(int nbShips) {
//		Scanner scanner = new Scanner(System.in);
//		List<Hex> hexes = Ground.getHexes().filter((Hex hex) -> hex.getLevelSystem() == 1 && hex.getCurrentOccupant() == null).toList();
//		if(!this.shipsHorsPlateau.isEmpty()){
//			int id = 10;
//			while (true) {
//				System.out.println("Choisir un hexagone (entrer son ID) : ");
//				//On vérifie que l'entrée est un int
//				if (!scanner.hasNextInt()) {
//					System.out.println("Entrée invalide, veuillez entrer un entier.");
//					scanner.next(); // Consomme l'entrée incorrecte pour éviter une boucle infinie
//					continue; // Recommence la boucle pour demander un autre ID
//				}
//				while ((!hexes.stream().anyMatch((Hex hex) -> hex.getIdHex() == id)) || (Ground.getHexById(id).getCurrentOccupant()!=null)){
//					//tant que l'Hex choisi n'est ni valide ni de niveau 1 ou que le système est déjà occupé
//					System.out.println("Cet Hexagone n'est pas valide, entrez en un nouveau.");
//					scanner.nextInt();
//				}
//				Hex getHex = Ground.getHexById(id);
//				this.shipsHorsPlateau.peek().setPosition(getHex);
//				//On ajoute le nouveau vaisseau à la liste des vaisseaux situés sur le plateau
//				this.shipsSurPlateau.add(shipsHorsPlateau.peek());
//				this.shipsHorsPlateau.pop();
//			}
//		}else{
//			System.out.println("Vous n'avez plus de vaisseau à placer sur le plateau");
//		}
//		scanner.close();
//	}

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

	public String getName(){return this.name;}

	public int getPriority() {return this.priority;}

	public List<Ship> findShipsByHexId(int hexId) {
		// Filtre tous les vaisseaux correspondant à l'ID de l'Hex
		return shipsSurPlateau.stream()
				.filter(s -> s.getPosition() != null && s.getPosition().getIdHex() == hexId)
				.collect(Collectors.toList());
	}

	public List<Ship> chooseShipsToMove(List<Ship> shipsInHex) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Il y a " + shipsInHex.size() + " vaisseaux dans cet Hex.");
		System.out.print("Combien de vaisseaux souhaitez-vous déplacer ? ");

		int count = scanner.nextInt();
		while (count < 1 || count > shipsInHex.size()) {
			System.out.println("Nombre invalide. Veuillez choisir entre 1 et " + shipsInHex.size() + " vaisseaux.");
			// On peut gérer cette erreur autrement par la suite
			count = scanner.nextInt();
//			scanner.close();
		}

		return shipsInHex.subList(0, count); // Retourne les n premiers vaisseaux choisis
	}

	public void setPlayerName() {
		Scanner reader = new Scanner(System.in); // Reading from System.in
		while(true){
			System.out.println(this.name + " enter a player name: ");
			if (reader.hasNextLine()) { // Vérifie s'il y a une ligne à lire
				this.name = reader.nextLine(); // Lire la ligne complète
				break;
			} else {
				System.out.println("No input found.");
			}
			reader.close();
		}
	}

	//Pour le moment les couleurs sont imposées
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
			System.out.println(this.name + ", quelle carte voulez-vous jouez en " + (i+1) + "(Expand, Explore ou Exterminate) :");
			String r = reader.next();
//			reader.close();
			// L'ordre est à surveiller, il y a souvent des erreurs de ce côté là
			if (r.equalsIgnoreCase("expand")) {
				cardOrder[i] = commandCards[0];
				System.out.println("La "+(i+1)+"ieme carte choisie est la carte expand");
			} else if (r.equalsIgnoreCase("explore")) {
				cardOrder[i] = commandCards[1];
				System.out.println("La "+(i+1)+"ieme carte choisie est la carte explore");
			} else if (r.equalsIgnoreCase("exterminate")) {
                cardOrder[i] = commandCards[2];
				System.out.println("La "+(i+1)+"ieme carte choisie est la carte exterminate");
			} else {
				System.out.println("Choix invalide. Veuillez recommencer.");
				i--; // Refaire la même position si l'entrée est invalide
			}
		}
		
	}

	public void perform(int currentRound) {
		// Il faut aussi prendre en compte l'ordre dans lequel les joueurs doivent jouer les cartes ici et regarder si
		// d'autres joueurs ont joué cette carte.

		cardOrder[currentRound].execute(currentRound);
	}

	@Override
	public String toString() {
		return "Player name : "+name;
	}

	public void updatePrio() {
		if (this.priority == 0){
			this.priority=2;
		}else {
			this.priority--;
		}
	}
}
