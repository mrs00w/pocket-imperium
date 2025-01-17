package pimperium;

/**
 * Interface représentant une CommandCard
 */

public interface CommandCard {
	/**
	 * Permet d'exécuter les instructions de la carte choisie
	 * @param currentRound La position de la carte à exécuter
	 */

	public void execute(int currentRound);

	/**
	 * Renvoie la priorité de carte. L'ordre étant Expand, Explore, Exterminate
	 * @return La priorité de la carte
	 */

	public int getPriority();

	/**
	 * Renvoie le nom de la carte
	 * @return le nom de la carte
	 */

	public String getName();
}
