package blackjack;

public class Game {	
	//========================================================================
	//Objects
	//========================================================================
	
	UserInterface UI;
	
	public Game()
	{
		init();
	}
	
	private void init()
	{
		UI = new UserInterface();
	}
}
