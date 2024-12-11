package pimperium;

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
		List<Player> players = game.getPlayers();

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

		// Collecte des hexes contrôlés par le joueur
		List<Hex> controlledHexes = game.getGround().getHexes().stream()
				.filter(hex -> hex.getCurrentOccupant() == player)
				.toList();

		// Vérification : le joueur contrôle-t-il des hexes ?
		if (controlledHexes.isEmpty()) {
			System.out.println("Vous ne contrôlez aucun hex pour ajouter des vaisseaux.");
			return;
		}

		// Initialisation pour la lecture utilisateur
		Scanner reader = new Scanner(System.in);
		Map<Integer, Integer> hexAllocation = new HashMap<>(); // Suivi des hexes déjà utilisés

		for (int i = 0; i < shipsToAdd; i++) {
			Ship currentShip = player.getShipsHorsPlateau().pop();

			Hex selectedHex;
			while (true) {
				System.out.println(STR."Où voulez-vous placer votre vaisseau n°\{i + 1} ? Entrez l'ID d'un hex contrôlé :");
				int hexId = reader.nextInt();

				// Vérifier si l'hex est contrôlé et est un hex valide
				selectedHex = controlledHexes.stream()
						.filter(hex -> hex.getIdHex() == hexId)
						.findFirst()
						.orElse(null);

				if (selectedHex == null) {
					System.out.println("Erreur : Hex non valide ou non contrôlé. Réessayez.");
					continue;
				}

				// Vérification de la limite de placement (2 vaisseaux max par hex)
				int allocatedShips = hexAllocation.getOrDefault(selectedHex.getIdHex(), 0);
				if (allocatedShips >= 2) {
					System.out.println("Erreur : Vous ne pouvez pas placer plus de 2 vaisseaux sur cet hex.");
					continue;
				}

				// Si tout est valide, ajouter à l'allocation et sortir de la boucle
				hexAllocation.put(selectedHex.getIdHex(), allocatedShips + 1);
				break;
			}

			// Mettre à jour la position du vaisseau
			currentShip.updatePosition(selectedHex);
			player.getShipsSurPlateau().add(currentShip);
			System.out.println(STR."Vaisseau ajouté à l'hex : \{selectedHex.getIdHex()}");
		}
	}

}
