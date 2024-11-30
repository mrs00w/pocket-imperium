package pimperium;

public class ExterminateCard implements CommandCard{

	private Player player;

	public ExterminateCard(Player player) {
		this.player = player;
	}

	public int getPriority() {
		int priority = 3;
		return priority;
	}

	public void execute(int currentRound) {
		
	}
}
