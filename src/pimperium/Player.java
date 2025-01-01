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
	private List<Hex> hexesOccupes = new ArrayList<Hex>();
//	private Set<CommandCard> cards;
	private CommandCard[] commandCards = {new ExpandCard(this), new ExploreCard(this), new ExterminateCard(this)};
	private CommandCard[] cardOrder = commandCards;
	//A l'initialisation, l'ordre des cartes est par défaut expand, explore et exterminate
	private int score;
	private int priority;
	private Set<SectorCard> controlledSectors;

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

	public int getIdPlayer() {
		return idPlayer;
	}

	public CommandCard[] getCommandCards() {
		return commandCards;
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

	public List<Hex> getHexesOccupes() {
		return hexesOccupes;
	}

	public CommandCard[] getCardOrder() {
		return this.cardOrder;
	}

	public CommandCard getCard(int i) {
		return this.cardOrder[i];
	}

	public String getName(){return this.name;}

	public int getPriority() {return this.priority;}

	public void addPoints(int points) {
		this.score += points;
	}

	public void controlSector(SectorCard sector) {
		controlledSectors.add(sector);
	}

	public boolean controlsSector(SectorCard sector) {
		return controlledSectors.contains(sector);
	}

	public boolean controlsHex(Hex hex) {
		return hexesOccupes.contains(hex);
	}

	public int getScore() {
		return score;
	}

	public List<Ship> findShipsByHexId(int hexId) {
		// Filtre tous les vaisseaux correspondant à l'ID de l'Hex
		return shipsSurPlateau.stream()
				.filter(s -> s.getPosition() != null && s.getPosition().getIdHex() == hexId)
				.collect(Collectors.toList());
	}

	public List<Ship> chooseShipsToMove(List<Ship> shipsInHex) {
		Scanner scanner = new Scanner(System.in);

		if (shipsInHex.isEmpty()){
			System.out.println("Vous ne pouvez pas déplacer de vaisseau depuis cet hexagone.");
			return new ArrayList<Ship>(); //on renvoie une liste vide
		}

		System.out.println(STR."Il y a \{shipsInHex.size()} vaisseaux dans cet Hex.");
		System.out.print("Combien de vaisseaux souhaitez-vous déplacer ? ");

		int count = scanner.nextInt();
		while (count < 1 || count > shipsInHex.size()) {
			System.out.println(STR."Nombre invalide. Veuillez choisir entre 1 et \{shipsInHex.size()} vaisseaux.");
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
		Set<CommandCard> chosenCards = new HashSet<>();

		for (int i=0; i<3; i++) {
			Scanner reader = new Scanner(System.in); // Reading from System.in
			System.out.println(this.name + ", quelle carte voulez-vous jouez en " + (i+1) + "(Expand, Explore ou Exterminate) :");
			String r = reader.next();
//			reader.close();
			// L'ordre est à surveiller, il y a souvent des erreurs de ce côté là
			CommandCard selectedCard = null;

			if (r.equalsIgnoreCase("expand")) {
				selectedCard = commandCards[0];
			} else if (r.equalsIgnoreCase("explore")) {
				selectedCard = commandCards[1];
			} else if (r.equalsIgnoreCase("exterminate")) {
				selectedCard = commandCards[2];
			}

			if (selectedCard == null) {
				System.out.println("Choix invalide. Veuillez recommencer.");
				i--; // Refaire la même position si l'entrée est invalide
			} else if (chosenCards.contains(selectedCard)) {
				System.out.println("Vous avez déjà choisi cette carte. Veuillez en choisir une autre.");
				i--; // Refaire la même position si la carte a déjà été choisie
			} else {
				cardOrder[i] = selectedCard;
				chosenCards.add(selectedCard); // Ajouter la carte choisie à l'ensemble
				System.out.println("La " + (i + 1) + "ième carte choisie est " + selectedCard.getClass().getSimpleName());
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
