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

	public void execute(int currentround) {
		System.out.println(STR."\{this.player.getName()} envahit un système");

        Controller controller = game.getController();
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

        System.out.println("Quel système voulez vous envahir ? (ID)");

        int idHex;
        try {
            idHex = reader.nextInt();
        } catch (Exception e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            return;
        }

        Hex targetHex = game.getGround().getHexById(idHex);

        // Vérifier que le joueur n'occupe pas déjà le système
        if (targetHex.getCurrentOccupant() == player) {
            System.out.println("Vous occupez déjà ce système.");
            return;
        }

        // Vérifier que le système est adjacent
        List<Hex> neighbors = targetHex.getNeighbors();
        List<Hex> validSources = neighbors.stream()
                .filter(h -> h.getCurrentOccupant() == player && !h.getShips().isEmpty())
                .toList();

        if (validSources.isEmpty()) {
            System.out.println("Aucun hex adjacent ne contient vos vaisseaux pour l'invasion.");
            return;
        }

        // Demander au joueur combien de vaisseaux il souhaite utiliser
        System.out.println("Combien de vaisseaux voulez-vous utiliser pour l'invasion ?");
        int numberOfShips;
        try {
            numberOfShips = reader.nextInt();
        } catch (Exception e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            return;
        }

        if (numberOfShips <= 0) {
            System.out.println("Vous devez utiliser au moins un vaisseau.");
            return;
        }

        List<Ship> invasionFleet = new LinkedList<>();

        // Collecter les vaisseaux à partir des hexes adjacents
        Hex sourceHex = null;
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

            for (int i = 0; i < shipsToTake; i++) {
                Ship movingShip = sourceHex.getShips().removeFirst();
                invasionFleet.add(movingShip);
				controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), sourceHex.getCurrentOccupant());
            }
        }

        // Vérifier l'occupant actuel de la cible
        Player currentOccupant = targetHex.getCurrentOccupant();
        List<Ship> defendingFleet = targetHex.getShips();

        // Résolution de l'invasion
        if (!defendingFleet.isEmpty()) {
            int smallestFleetSize = Math.min(invasionFleet.size(), defendingFleet.size());

            // Retirer les vaisseaux de chaque côté

            for (int i = 0; i < smallestFleetSize; i++) {
                invasionFleet.removeFirst();
                defendingFleet.removeFirst();
            }

            // IL faut aussi retirer les vaisseaux en question du plateau

            System.out.println(STR."Combat terminé : \{smallestFleetSize} vaisseaux détruits de chaque côté.");
        }

        // Résultat de l'invasion
        if (invasionFleet.isEmpty()) {
            System.out.println("Invasion échouée.");
        } else {
            System.out.println("Invasion réussie ! Vous contrôlez maintenant le système.");
            targetHex.setCurrentOccupant(player);
            targetHex.setShips(invasionFleet);
        }

		if (targetHex.getShips().isEmpty()) {
			System.out.println("Le système" + targetHex.getIdHex() + "est désormais innocupé");
			targetHex.setCurrentOccupant(null);
		}

        controller.updateHexLabel(targetHex.getIdHex(), targetHex.getShips().size(), targetHex.getCurrentOccupant());
		controller.updateHexLabel(sourceHex.getIdHex(), sourceHex.getShips().size(), sourceHex.getCurrentOccupant());

        // Chaque vaisseau ne peut être utilisé qu'une fois par round, donc marquer les vaisseaux comme "utilisés"
        invasionFleet.forEach(ship -> ship.setUsed(true));
    }
}
