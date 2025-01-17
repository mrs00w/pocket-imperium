package pimperium;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Représente un joueur dans le jeu Pocket Imperium.
 * <p>
 * Chaque joueur possède un ensemble de vaisseaux, des cartes Command, et contrôle des secteurs et des hexagones.
 * Cette classe gère les attributs et les actions possibles d'un joueur pendant la partie.
 */

public class Player {
	/**
	 * Le nom du joueur
	 */
	private String name;
	/**
	 * le nombre du joueur dans la partie
	 */
	private static int nombrePlayer = 0;
	/**
	 * la couleur du joueur
	 */
	private Color color;
	/**
	 * les vaisseaux possédés par le joueur en dehors du plateau
	 */

	private final Stack<Ship> shipsHorsPlateau;
	/**
	 * les vaisseaux possédés par le joueur présents sur le plateau
	 */
	private final List<Ship> shipsSurPlateau = new ArrayList<Ship>();
	/**
	 * les Hex occupés par le joueur
	 */
	private final List<Hex> hexesOccupes = new ArrayList<Hex>();
	/**
	 * les cartes Command possédés par le joueur
	 */
	private final CommandCard[] commandCards = {new ExpandCard(this), new ExploreCard(this),
			new ExterminateCard(this)};
	/**
	 * l'ordre des cartes Command choisi par le joueur
	 */
	private CommandCard[] cardOrder = commandCards;
	/**
	 * le score du joueur
	 */
	//A l'initialisation, l'ordre des cartes est par défaut expand, explore et exterminate
	private int score;
	/**
	 * la priorité du joueur. Utilisé pour l'ordre de jeu des joueurs
	 */
	private int priority;
	/**
	 * Les secteurs controllés par le joueur
	 */
	private final Set<SectorCard> controlledSectors;

	/**
	 * Crée un joueur
	 */

	public Player() {
		this.score = 0;
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

	/**
	 * Ajoute un Hex parmi les Hex occupés par le joueur
	 * @param hex le nouvel Hex contrôlé par le joueur
	 */

	public void addHexesOccupes(Hex hex) {
		hexesOccupes.add(hex);
	}

	/**
	 * Ajoute les Hex parmi les Hex occupés par le joueur
	 * @param hexes les nouveaux Hex contrôlés par le joueur
	 */

	public void addHexesOccupes(List<Hex> hexes) {
		hexesOccupes.addAll(hexes);
	}

	/**
	 * Change la couleur du joueur
	 * @param color couleur du joueur
	 */

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Renvoie la couleur du joueur
	 * @return la couleur du joueur
	 */

	public Color getColor() {
		return color;
	}

	/**
	 * Renvoie les secteurs contrôlés par le joueur
	 * @return les secteurs contrôlés par le joueur
	 */

	public Set<SectorCard> getControlledSectors() {
		return controlledSectors;
	}

	/**
	 * Renvoie les secteurs contrôlés par le joueur
	 * @return les secteurs contrôlés par le joueur
	 */

	public CommandCard[] getCommandCards() {
		return commandCards;
	}

	/**
	 * Renvoie les vaisseaux possédés par le joueur, mais hors du plateau
	 * @return les vaisseaux possédés par le joueur, mais hors du plateau
	 */

	public Stack<Ship> getShipsHorsPlateau() {
		return this.shipsHorsPlateau;
	}

	/**
	 * Renvoie les vaisseaux possédés par le joueur présents sur le plateau
	 * @return les vaisseaux possédés par le joueur présents sur le plateau
	 */

	public List<Ship> getShipsSurPlateau() {
		return this.shipsSurPlateau;
	}

	/**
	 * Renvoie les hexagones occupés par le joueur.
	 *
	 * @return une liste des hexagones occupés.
	 */
	public List<Hex> getHexesOccupes() {
		return hexesOccupes;
	}

	/**
	 * Renvoie l'ordre des cartes Command du joueur.
	 *
	 * @return un tableau des cartes Command dans leur ordre d'exécution.
	 */
	public CommandCard[] getCardOrder() {
		return this.cardOrder;
	}

	/**
	 * Renvoie une carte Command spécifique à un index donné.
	 *
	 * @param i l'index de la carte Command.
	 * @return la carte Command correspondante.
	 */
	public CommandCard getCard(int i) {
		return this.cardOrder[i];
	}

	/**
	 * Renvoie le nom du joueur.
	 *
	 * @return le nom du joueur.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Renvoie la priorité du joueur.
	 *
	 * @return la priorité du joueur.
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * Ajoute un nombre de points au score du joueur.
	 *
	 * @param points le nombre de points à ajouter.
	 */
	public void addPoints(int points) {
		this.score += points;
		System.out.println("Points ajoutés : " + points + ". Nouveau score : " + this.score);
	}

	/**
	 * Ajoute un secteur contrôlé par le joueur.
	 *
	 * @param sector le secteur à ajouter.
	 */
	public void addControlledSector(SectorCard sector) {
		controlledSectors.add(sector);
	}

	/**
	 * Vérifie si le joueur contrôle un secteur donné.
	 *
	 * @param sector le secteur à vérifier.
	 * @return vrai si le joueur contrôle le secteur, faux sinon.
	 */
	public boolean controlsSector(SectorCard sector) {
		return controlledSectors.contains(sector);
	}

	/**
	 * Renvoie le score du joueur.
	 *
	 * @return le score actuel du joueur.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Recherche les vaisseaux présents dans un hexagone donné.
	 *
	 * @param hexId l'identifiant de l'hexagone.
	 * @return une liste des vaisseaux présents dans l'hexagone.
	 */
	public List<Ship> findShipsByHexId(int hexId) {
		return shipsSurPlateau.stream()
				.filter(s -> s.getPosition() != null && s.getPosition().getIdHex() == hexId)
				.collect(Collectors.toList());
	}

	/**
	 * Permet au joueur de choisir les vaisseaux à déplacer dans un hexagone.
	 *
	 * @param shipsInHex les vaisseaux disponibles dans l'hexagone.
	 * @return une liste des vaisseaux sélectionnés par le joueur.
	 */

	public List<Ship> chooseShipsToMove(List<Ship> shipsInHex) {
		Scanner scanner = new Scanner(System.in);

		if (shipsInHex.isEmpty()){
			System.out.println("Vous ne pouvez pas déplacer de vaisseau depuis cet hexagone.");
			return new ArrayList<Ship>(); //on renvoie une liste vide
		}

		System.out.println("Il y a " + shipsInHex.size() + " vaisseaux dans cet Hex.");
		System.out.print("Combien de vaisseaux souhaitez-vous déplacer ? ");

		int count = scanner.nextInt();

		while (count < 1 || count > shipsInHex.size()) {
			System.out.println("Nombre invalide. Veuillez choisir entre 1 et " + shipsInHex.size() + " vaisseaux.");
			// On peut gérer cette erreur autrement par la suite
			count = scanner.nextInt();
		}

		return shipsInHex.subList(0, count); // Retourne les n premiers vaisseaux choisis
	}

	/**
	 * Permet au joueur de définir son nom via une saisie utilisateur.
	 */

	//Pour les joueurs humains
	public void setPlayerName() {
		Scanner reader = new Scanner(System.in);
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

	/**
	 * Définit un nom spécifique pour le joueur.
	 *
	 * @param name le nom à attribuer au joueur.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Permet au joueur de planifier l'ordre d'exécution des cartes Command.
	 */
	
	public void plan() {
		Set<CommandCard> chosenCards = new HashSet<>();

		for (int i=0; i<3; i++) {
			Scanner reader = new Scanner(System.in);
			System.out.println(this.name + ", quelle carte voulez-vous jouez en " + (i+1) + "(Expand, Explore ou Exterminate) :");
			String r = reader.next();
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

	/**
	 * Exécute une carte Command à un tour donné.
	 *
	 * @param currentRound l'indice du tour actuel.
	 */
	public void perform(int currentRound) {
		cardOrder[currentRound].execute(currentRound);
	}

	/**
	 * Renvoie une représentation textuelle du joueur.
	 *
	 * @return une chaîne décrivant le joueur.
	 */
	@Override
	public String toString() {
		return "Player name : " + name;
	}

	/**
	 * Met à jour la priorité du joueur pour l'ordre de jeu.
	 */

	public void updatePrio() {
		if (this.priority == 0){
			this.priority=2;
		}else {
			this.priority--;
		}
	}
}
