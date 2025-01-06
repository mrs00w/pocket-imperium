package pimperium;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

public class Player {
	private String name;
	private static int nombrePlayer = 0;
	private Color color;
	private final int idPlayer;
	private final Stack<Ship> shipsHorsPlateau;
	private final List<Ship> shipsSurPlateau = new ArrayList<Ship>();
	private final List<Hex> hexesOccupes = new ArrayList<Hex>();
//	private Set<CommandCard> cards;
	private final CommandCard[] commandCards = {new ExpandCard(this), new ExploreCard(this), new ExterminateCard(this)};
	private CommandCard[] cardOrder = commandCards;
	//A l'initialisation, l'ordre des cartes est par défaut expand, explore et exterminate
	private int score;
	private int priority;
	private Set<SectorCard> controlledSectors;

	public Player() {
		this.score = 0;
		this.idPlayer = nombrePlayer;
		this.priority=nombrePlayer;
		nombrePlayer++;
		this.name = "Player"+nombrePlayer;
		this.cardOrder = new CommandCard[3];
		this.controlledSectors = new HashSet<>();
        //this.choisirCouleurVaisseau();
        //imposer la couleur aux joueurs finalement
        this.shipsHorsPlateau = new Stack<Ship>();
		for (int i=0;i<15;i++) {
			shipsHorsPlateau.push(new Ship(this));
		}
		//Avant même le premier tour, chaque joueur doit choisir le premier système sur lequel s'installer
	}

	public void addHexesOccupes(Hex hex) {
		hexesOccupes.add(hex);
	}

	public void addHexesOccupes(List<Hex> hexes) {
		hexesOccupes.addAll(hexes);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public Set<SectorCard> getControlledSectors() {
		return controlledSectors;
	}

	public CommandCard[] getCommandCards() {
		return commandCards;
	}

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
		this.score = score + points;
		System.out.println("Points ajoutés : " + points + ". Nouveau score : " + this.score);
	}

	public void addControlledSector(SectorCard sector) {
		controlledSectors.add(sector);
	}

	public boolean controlsSector(SectorCard sector) {
		return controlledSectors.contains(sector);
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

	//Pour les joueurs humains
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

	public void setName(String name) {
		this.name = name;
	}

	//Pour le moment les couleurs sont imposées
//	public void choisirCouleurVaisseau() {
//		//détecter erreur dans le cas où ce n'est pas un int qui est renseigné ?
//		//ou si int != de 0,1,2
//		Scanner inputReader = new Scanner(System.in);
//		System.out.println("Choisir votre couleur parmi jaune(0) rouge(1) ou bleu(2)");
//		int couleur = inputReader.nextInt();
//		this.couleurVaisseau = couleur;
//	}
	
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
