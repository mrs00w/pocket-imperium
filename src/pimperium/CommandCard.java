package pimperium;

public interface CommandCard {
	public void execute(int currentRound);

	public int getPriority();
}
