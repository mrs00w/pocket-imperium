package pimperium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Représente un joueur joué par l'ordinateur
 */

public class Bot extends Player {
    /**
     * Variable aléatoire permettant au bot de prendre des décisions
     */
    private Random random;

    /**
     * Créer un joueur virtuel
     * @param id Identifiant du bot
     */

    public Bot(int id) {
        super();
        this.random = new Random();
        this.setName("Bot "+(id+1)); // Un nom unique pour le bot
        System.out.println("Le bot "+this.getName()+" a été ajouté à la partie.");
    }

    /**
     * Permet au bot de choisir une carte aléatoirement
     */

    public void plan() {
        System.out.println(this.getName() + " est en train de planifier ses actions.");

        // Récupération des cartes disponibles
        CommandCard[] commandCards = getCardOrder();
        List<CommandCard> cardList = new ArrayList<>(List.of(getCommandCards()));

        // Mélange aléatoire des cartes
        Collections.shuffle(cardList);

        // Affectation des cartes dans un ordre aléatoire
        for (int i = 0; i < cardList.size(); i++) {
            commandCards[i] = cardList.get(i);
            System.out.println("La carte " + (i + 1) + " choisie par le bot est : " + commandCards[i].getName());
        }
    }

    /**
     * Récupère le nom du joueur
     * @return le nom du bot
     */

    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * Permet au bot de déplacer des vaisseaux
     */

    public void makeMove() {
        // Logique pour décider où déplacer les vaisseaux
        if (!getShipsSurPlateau().isEmpty()) {
            Hex targetHex = chooseHexToMove();
            if (targetHex != null) {
                System.out.println(STR."\{this.getName()} déplace un vaisseau vers l'hexagone \{targetHex.getIdHex()}");
            }
        }
    }

    /**
     * Récupère un Hex aléatoirement parmi tous les Hex de la carte
     * @return un Hex aléatoire
     */

    private Hex chooseHexToMove() {
        Game game = Game.getInstance();
        // Exemple : Choisir un hex au hasard parmi ceux disponibles
        List<Hex> hexes = game.getGround().getHexes();
        return hexes.get(random.nextInt(hexes.size()));
    }
}
