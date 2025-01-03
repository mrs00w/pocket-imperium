package pimperium;

import gui.Controller;

import java.util.*;

public class ExpandCard implements CommandCard{

	private final Player player;

	public ExpandCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
        return 1;
	}

	public void execute(int currentRound) {
		System.out.println(STR."\{player.getName()} ajoute de nouveaux vaisseaux sur le plateau");
		Game game = Game.getInstance();
		Controller controller = game.getController();
		List<Player> players = game.getPlayers();

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


		//
 		//Cette partie là on l'utilise pour chaque CommandCard, on peut (je pense) mettre cette méthode dans commandCard et la réutiliser (au lieu de la réécrire)
		// Filtrer les joueurs ayant choisi la carte Expand
		List<Player> expandPlayers = players.stream()
				.filter(p -> p.getCard(currentRound) instanceof ExpandCard)
				.toList();

		int expandCount = expandPlayers.size();

		// Calcul du nombre de vaisseaux à ajouter
		int shipsToAdd = switch (expandCount) {
			case 1 -> 3;
			case 2 -> 2;
			default -> 1;
		};
		//Jusqu'ici c'est ce qu'on réécrit



		// Collecte des hexes contrôlés par le joueur
		List<Hex> controlledHexes = game.getGround().getHexes().stream()
				.filter(hex -> hex.getCurrentOccupant() == player)
				.filter(hex -> hex.getLevelSystem() != 0)
				.toList();

		// Vérification : le joueur contrôle-t-il des systèmes ?
		//dans le cas où le joueur n'est pas mort (donc il a encore des vaisseaux sur la map)
		//mais il ne controle pas de système
		if (controlledHexes.isEmpty()) {
			System.out.println("Vous ne contrôlez aucun hex pour ajouter des vaisseaux.");
			return;
		}

		// Initialisation pour la lecture utilisateur
		Scanner reader = new Scanner(System.in);
//		Map<Integer, Integer> hexAllocation = new HashMap<>(); // Suivi des hexes déjà utilisés

		for (int i = 0; i < shipsToAdd; i++) {
			if (player.getShipsHorsPlateau().isEmpty()){
				System.out.println("Vous n'avez plus de vaisseaux à placer");
				break;
			}
			Ship currentShip = player.getShipsHorsPlateau().pop();

			Hex selectedHex = null;
			while (true) {
				System.out.println(STR."Où voulez-vous placer votre vaisseau n°\{i + 1} ? Entrez l'ID d'un hex contrôlé ou tapez 0 pour passer votre tour :");
				int hexId;
				try {
					hexId = reader.nextInt();
				} catch (Exception e) {
					System.out.println("Entrée invalide. Veuillez entrer un nombre.");
					return;
				}

				if (hexId==0){
					System.out.println("Vous avez choisi de ne pas expand pendant ce tour.");
					break;
				}

				// Vérifier que l'hex est un système contrôlé par le joueur et est un hex valide
				selectedHex = controlledHexes.stream()
						.filter(hex -> hex.getIdHex() == hexId)
						.findFirst()
						.orElse(null);

				if (selectedHex == null) {
					System.out.println("Erreur : Hex non valide ou non contrôlé. Réessayez.");
					continue;
				}
//
////				// Vérification de la limite de placement (2 vaisseaux max par hex)
//				int allocatedShips = hexAllocation.getOrDefault(selectedHex.getIdHex(), 0);
//
//				//On n'a pas besoin d'imposer une limite de placement
////				if (allocatedShips >= 2) {
////					System.out.println("Erreur : Vous ne pouvez pas placer plus de 2 vaisseaux sur cet hex.");
////					continue;
////				}
//
//				// Si tout est valide, ajouter à l'allocation et sortir de la boucle
//				hexAllocation.put(selectedHex.getIdHex(), allocatedShips + 1);
				break;
			}
			if (selectedHex!=null) {
				// Mettre à jour la position du vaisseau
				currentShip.updatePosition(selectedHex);
				player.getShipsSurPlateau().add(currentShip);
				selectedHex.getShips().add(currentShip);
                int shipCount = selectedHex.getShips().size();
                controller.updateHexLabel(selectedHex.getIdHex(), shipCount, player);
				System.out.println(STR."Vaisseau ajouté à l'hex : \{selectedHex.getIdHex()}");
			}
		}
	}

}
