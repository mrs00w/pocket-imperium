package pimperium;
import gui.Controller;
import java.util.*;

/**
 * Représente la Command Card Expand
 */

public class ExpandCard implements CommandCard{

	/**
	 * Le nom du joueur qui possède cette Command Card
	 */

	private final Player player;

	/**
	 * Crée une carte Expand
	 * @param player
	 */

	public ExpandCard(Player player) {
		this.player = player;
	}

	/**
	 * Récupère la priorité de cette carte
	 * @return la priorité de la carte
	 */

	public int getPriority() {
        return 1;
	}

	/**
	 * Récuère le nom de la carte
	 * @return le nom de la carte
	 */

	public String getName(){return "expand";}

	/**
	 * Exécute les instructions de la carte Expand. Le joueur augmente ses vaisseaux sur le plâteau. Il ne peut en
	 * ajouter que sur les systèmes qu'il contrôle
	 * @param currentRound La position de la carte à exécuter
	 */

	public void execute(int currentRound) {
		System.out.println(STR."\n\{player.getName()} ajoute de nouveaux vaisseaux sur le plateau");
		Game game = Game.getInstance();
		Controller controller = game.getController();
		List<Player> players = game.getPlayers();

		Scanner reader = new Scanner(System.in);
		if (player instanceof Bot){
			// Décision aléatoire : 1 chance sur 6 de passer la carte
			int decision = new Random().nextInt(6) + 1;  // Génère un nombre entre 1 et 6
			if (decision == 1) {
				System.out.println("Le bot a décidé de passer la carte.");
				return;
			}
			System.out.println("Le bot a décidé de jouer la carte Expand.");
		}

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
				.filter(hex -> hex.getLevelSystem() != 0)
				.toList();

		// Vérification : le joueur contrôle-t-il des systèmes ?
		if (controlledHexes.isEmpty()) {
			System.out.println(player.getName()+" ne contrôle aucun hex pour ajouter des vaisseaux.");
			return;
		}

		// Initialisation pour la lecture utilisateur

		for (int i = 0; i < shipsToAdd; i++) {
			if (player.getShipsHorsPlateau().isEmpty()){
				System.out.println(player.getName()+" n'a plus de vaisseaux à placer");
				return;
			}
			Ship currentShip = player.getShipsHorsPlateau().pop();

			Hex selectedHex = null;
			if(player instanceof Bot){
				selectedHex = controlledHexes.get(new Random().nextInt(controlledHexes.size()));
			}else {
				while (true) {
					System.out.println(STR."Où voulez-vous placer votre vaisseau n°\{i + 1} ? Entrez l'ID d'un hex contrôlé ou tapez 0 pour passer votre tour : ");
					int hexId;
					try {
						hexId = reader.nextInt();
					} catch (Exception e) {
						System.out.println("Entrée invalide. Veuillez entrer un nombre.");
						continue;
					}

					if (hexId == 0) {
						System.out.println("Vous avez choisi de ne pas expand pendant ce tour.");
						return;
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
					break;
				}
			}
			if (selectedHex!=null) {
				// Mettre à jour la position du vaisseau
				currentShip.updatePosition(selectedHex);
				player.getShipsSurPlateau().add(currentShip);
				selectedHex.getShips().add(currentShip);
                int shipCount = selectedHex.getShips().size();
                controller.updateHexLabel(selectedHex.getIdHex(), shipCount, player);
				System.out.println(STR."\{player.getName()} a ajouté un vaisseau à l'hex : \{selectedHex.getIdHex()}");
			}
		}
	}

}
