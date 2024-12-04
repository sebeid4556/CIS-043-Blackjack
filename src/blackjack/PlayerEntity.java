package blackjack;

import java.util.ArrayList;

public class PlayerEntity {
	public ArrayList<Card> cards = new ArrayList<>(); 	
	protected boolean hasAce;
	protected String name;
	
	public PlayerEntity(String name)
	{
		this.name = name;
	}

	public boolean getHasAce()
	{
		return hasAce;
	}

	public void setHasAce(boolean flag)
	{
		hasAce = flag;
	}

	//will always treat Ace as 1
	public int getRank()
	{
		return Util.calculateHandRank(cards);
	}

	public int computeScore()
	{
		int rank = getRank(); 
		if (hasAce) {
			return (rank + 10) > 21 ? rank : rank + 10;
		} else {
			return rank;
		}
	}
}
