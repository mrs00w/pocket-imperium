package pimperium;

import gui.Controller;

import java.util.ArrayList;
import java.util.*;

public class ExterminateCard implements CommandCard {

	private Game game;
	private Player player;

	public ExterminateCard(Player player) {
		this.game = Game.getInstance();
		this.player = player;
	}

	public int getPriority() {
		return 3;
	}

    public String getName(){return "exterminate";}

	public void execute(int currentRound) {
        System.out.println(STR."\{this.player.getName()} envahit un système");
        // Filtre des joueurs ayant choisi Explore pour compter le nbr de vaisseaux a déplacer
        List<Player> players = new ArrayList<>(game.getPlayers());
        List<Player> exterminatePlayers = players.stream()
                .filter(p -> p.getCard(currentRound) instanceof ExterminateCard)
                .toList();

        // Déterminer le nombre de mouvements autorisés
        int exterminateCount = exterminatePlayers.size();
        int fleetMovementsAllowed = switch (exterminateCount) {
            case 1 -> 3;  // 3 mouvements si seul joueur à explorer
            case 2 -> 2;  // 2 mouvements si 2 joueurs explorent
            default -> 1; // 1 mouvement sinon
        };

        if (player instanceof Bot){
            // Décision aléatoire : 1 chance sur 6 de passer la carte
            int decision = new Random().nextInt(6) + 1;  // Génère un nombre entre 1 et 6
            if (decision == 1) {
                System.out.println("Le bot a décidé de passer la carte.");
                return;
            }
            System.out.println("Le bot a décidé de jouer la carte Exterminate.");
        }else {

            System.out.println(player.getName() + ", voulez vous jouer cette carte ?");
            System.out.println("1. Passer la carte");
            System.out.println("2. Jouer la carte");

            Scanner reader = new Scanner(System.in);
            int choice;
            try {
                choice = reader.nextInt();
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                return;
            }
            if (choice == 1) {
                System.out.println("Vous avez choisi de passer cette carte.");
                // Rien à faire ici : le joueur ne joue pas cette carte
                return;
            }
        }
        Controller controller = game.getController();
        int n = 0;
        while (n < fleetMovementsAllowed) {
            System.out.println("Quel système voulez vous envahir ? (ID)");
            Hex targetHex;
            Hex sourceHex;
            List<Ship> invasionFleet = new LinkedList<>();

            if(player instanceof Bot) {
                // Étape 1 : Trouver les hexagones candidats à l'invasion
                List<Hex> hexesOccupes = player.getHexesOccupes();
                List<Hex> potentialTargets = new ArrayList<>();

                for (Hex hex : hexesOccupes) {
                    long availableShips = hex.getShips().stream().filter(ship -> !ship.isUsed()).count();
                    if (availableShips > 0) {
                        for (Hex neighbor : hex.getNeighbors()) {
                            if (neighbor.getLevelSystem() > 0 && neighbor.getCurrentOccupant() != player) {
                                potentialTargets.add(neighbor);
                            }
                        }
                    }
                }
                // Étape 2 : Vérifier s'il y a des cibles disponibles
                if (potentialTargets.isEmpty()) {
                    System.out.println("Aucune cible valide pour le Bot.");
                    return;
                }
                // Étape 3 : Choisir une cible aléatoire
                Random random = new Random();
                targetHex = potentialTargets.get(random.nextInt(potentialTargets.size()));
                System.out.println("Le Bot choisit d'envahir le système : " + targetHex.getIdHex());

                // Étape 4 : Déterminer les hexagones sources valides
                List<Hex> validSources = targetHex.getNeighbors().stream()
                        .filter(h -> h.getCurrentOccupant() == player && h.getShips().stream().anyMatch(ship -> !ship.isUsed()))
                        .toList();

                // Étape 5 : Choisir un hex source aléatoire
                sourceHex = validSources.get(random.nextInt(validSources.size()));
                System.out.println("Le Bot choisit de déplacer des vaisseaux depuis l'hex : " + sourceHex.getIdHex());

                // Étape 6 : Déterminer le nombre de vaisseaux à déplacer
                int numberOfShips = Math.min(3, (int) sourceHex.getShips().stream().filter(ship -> !ship.isUsed()).count());
                System.out.println("Le Bot déplace " + numberOfShips + " vaisseaux.");
                for (int i = 0; i < numberOfShips; i++) {
                    Ship movingShip = sourceHex.getShips().removeFirst();
                    invasionFleet.add(movingShip);
                    controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), sourceHex.getCurrentOccupant());
                }
            }else {
                Scanner reader = new Scanner(System.in);
                int idHex;
                try {
                    idHex = reader.nextInt();
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    continue;
                }

                targetHex = game.getGround().getHexById(idHex);

                // Vérifier que le joueur n'occupe pas déjà le système
                if (targetHex.getCurrentOccupant() == player) {
                    System.out.println("Vous occupez déjà ce système.");
                    continue;
                }

                // Vérifier que le système est adjacent
                List<Hex> neighbors = targetHex.getNeighbors();
                List<Hex> validSources = neighbors.stream()
                        .filter(h -> h.getCurrentOccupant() == player && h.getShips().stream().anyMatch(ship -> !ship.isUsed()))
                        .toList();
                if (validSources.isEmpty()) {
                    System.out.println("Aucun hex adjacent ne contient vos vaisseaux pour l'invasion.");
                    continue;
                }

                // Demander au joueur combien de vaisseaux il souhaite utiliser
                System.out.println("Combien de vaisseaux voulez-vous utiliser pour l'invasion ?");
                int numberOfShips;
                try {
                    numberOfShips = reader.nextInt();
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    continue;
                }

                if (numberOfShips <= 0) {
                    System.out.println("Vous devez utiliser au moins un vaisseau.");
                    continue;
                }
                if (numberOfShips> validSources.size()) {
                    System.out.println("Vous n'avez pas suffisament de vaisseaux disponibles.");
                    continue;
                }

                // Collecter les vaisseaux à partir des hexes adjacents
                sourceHex = null;
                while (invasionFleet.size() < numberOfShips) {
                    System.out.println("Sélectionnez un hex adjacent (ID) pour fournir des vaisseaux :");
                    validSources.forEach(h -> System.out.println(STR."Hex ID: \{h.getIdHex()} - Vaisseaux disponibles: \{h.getShips().size()}"));

                    int sourceHexId = reader.nextInt();
                    sourceHex = validSources.stream()
                            .filter(h -> h.getIdHex() == sourceHexId)
                            .findFirst()
                            .orElse(null);

                    if (sourceHex == null) {
                        System.out.println("Hex invalide. Veuillez réessayer.");
                        continue;
                    }

                    System.out.println("Combien de vaisseaux voulez-vous prendre de cet hex ?");
                    int shipsToTake = reader.nextInt();

                    if (shipsToTake <= 0 || shipsToTake > sourceHex.getShips().size()) {
                        System.out.println("Nombre de vaisseaux invalide. Veuillez réessayer.");
                        continue;
                    }

                    //Dans le cas où le joueur choisi plus de vaisseaux que ce qu'il avait prévu
                    if (invasionFleet.size() + shipsToTake > numberOfShips) {
                        shipsToTake = numberOfShips - invasionFleet.size();
                    }

                    for (int i = 0; i < shipsToTake; i++) {
                        Ship movingShip = sourceHex.getShips().removeFirst();
                        invasionFleet.add(movingShip);
                        controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), sourceHex.getCurrentOccupant());
                    }
                }
            }
            // Vérifier l'occupant actuel de la cible
            Player currentOccupant = targetHex.getCurrentOccupant();
            List<Ship> defendingFleet = targetHex.getShips();

            // Résolution de l'invasion
            if (!defendingFleet.isEmpty()) {
                int smallestFleetSize = Math.min(invasionFleet.size(), defendingFleet.size());

                // Retirer les vaisseaux de chaque côté

                if(currentOccupant!=null) {
                    for (int i = 0; i < smallestFleetSize; i++) {
                        invasionFleet.getFirst().updatePosition(null);
                        //on retire le vaisseau du plateau
                        player.getShipsHorsPlateau().push(invasionFleet.getFirst());
                        player.getShipsSurPlateau().remove(invasionFleet.getFirst());
                        invasionFleet.removeFirst();
                        defendingFleet.getFirst().updatePosition(null);
                        //On retire le vaisseau du plateau
                        currentOccupant.getShipsSurPlateau().remove(defendingFleet.getFirst());
                        currentOccupant.getShipsHorsPlateau().push(defendingFleet.getFirst());
                        defendingFleet.removeFirst();
                    }
                }

                // IL faut aussi retirer les vaisseaux en question du plateau

                System.out.println(STR."Combat terminé : \{smallestFleetSize} vaisseaux détruits de chaque côté.");
            }

            // Résultat de l'invasion
            if (invasionFleet.isEmpty()) {
                System.out.println("Invasion échouée.");
                //La liste de ships de l'hex est mise à jour avec les ships restants au joueur attaqué
                sourceHex.setShips(defendingFleet);
            } else {
                System.out.println("Invasion réussie ! Vous contrôlez maintenant le système.");
                targetHex.setCurrentOccupant(player);
                targetHex.setShips(invasionFleet);
            }

            if (targetHex.getShips().isEmpty()) {
                System.out.println("Le système" + targetHex.getIdHex() + "est désormais innocupé");
                targetHex.setCurrentOccupant(null);
            }

            if (sourceHex.getShips().isEmpty()) {
                System.out.println("Le système" + sourceHex.getIdHex() + "est désormais innocupé");
                targetHex.setCurrentOccupant(null);
            }

            controller.updateHexLabel(targetHex.getIdHex(), targetHex.getShips().size(), targetHex.getCurrentOccupant());
            controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), sourceHex.getCurrentOccupant());

            //Dans le cas où l'un des deux joueurs participant à l'attaque n'a plus de vaisseaux sur le terrain, celui-ci meurt
            if(currentOccupant!=null) {
                if (currentOccupant.getShipsSurPlateau().isEmpty()) {
                    game.getPlayers().remove(currentOccupant);
                    System.out.println(currentOccupant.getName() + " est mort, il est hors jeu.");
                }
            }
            if(player.getShipsSurPlateau().isEmpty()){
                game.getPlayers().remove(player);
                System.out.println(player.getName()+" est mort, il est hors jeu.");
            }

            // Chaque vaisseau ne peut être utilisé qu'une fois par round, donc marquer les vaisseaux comme "utilisés"
            invasionFleet.forEach(ship -> ship.setUsed(true));
            n++;
        }
//        //On fait une liste avec tous les vaisseaux qui ont été utilisés par le joueur lors de ce tour
//        List<Hex> occupiedHexes = game.getGround().getHexes().stream()
//                .filter(hex -> hex.getCurrentOccupant() == player)
//                .toList();
//
//        // Remet à false l'attribut isUsed de tous les vaisseaux utilisés
//        occupiedHexes.forEach(hex ->
//                hex.getShips().forEach(ship -> ship.setUsed(false))
//        );
    }
}
