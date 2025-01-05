package pimperium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bot extends Player {
    private Random random;

    public Bot(int id) {
        super();
        this.random = new Random();
        this.setName("Bot "+(id+1)); // Un nom unique pour le bot
        System.out.println("Le bot "+this.getName()+" a été ajouté à la partie.");
    }

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
//    @Override
//    public void plan() {
//        System.out.println(STR."\{this.getName()} est en train de planifier ses actions.");
//
//        CommandCard[] commandCards = getCardOrder();
//        CommandCard[] availableCards = getCommandCards();
//
//        for (int i = 0; i < commandCards.length; i++) {
//            // Exemple de logique basée sur les conditions
//            if (getShipsSurPlateau().size() > 10) {
//                commandCards[i] = availableCards[2]; // Exterminate
//            } else if (getHexesOccupes().size() < 3) {
//                commandCards[i] = availableCards[0]; // Expand
//            } else {
//                commandCards[i] = availableCards[1]; // Explore
//            }
//            System.out.println(STR."La carte \{i + 1} choisie par le bot est : \{commandCards[i]}");
//        }
//    }

    public void makeMove() {
        // Logique pour décider où déplacer les vaisseaux
        if (!getShipsSurPlateau().isEmpty()) {
            Hex targetHex = chooseHexToMove();
            if (targetHex != null) {
                System.out.println(STR."\{this.getName()} déplace un vaisseau vers l'hexagone \{targetHex.getIdHex()}");
            }
        }
    }

    private Hex chooseHexToMove() {
        Game game = Game.getInstance();
        // Exemple : Choisir un hex au hasard parmi ceux disponibles
        List<Hex> hexes = game.getGround().getHexes(); // Supposant que `Ground.getHexes()` donne tous les hexes
        return hexes.get(random.nextInt(hexes.size()));
    }
}
