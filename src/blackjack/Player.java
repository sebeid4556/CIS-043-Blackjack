package blackjack;

public class Player extends PlayerEntity{
	private boolean hasDrawn;
	
	public Player(String name)
	{
		super(name);
	}

	public boolean getHasDrawn()
	{
		return hasDrawn;
	}

	public void setHasDrawn(boolean flag)
	{
		hasDrawn = flag;
	}
}
