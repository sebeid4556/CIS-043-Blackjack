package blackjack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//utility class for commonly used operations
public class Util {
	
	//this method will always count an ace as 1, so the responsibility to account for it being an 11 
	//falls to the game logic
	public static int calculateHandRank(ArrayList<Card> hand)
	{		
		int rank = 0;		
		for(Card card : hand)
		{						
			rank += card.getRank().getNumericValue();
		}
		return rank;
	}
	
	//check if hand has an ace
	public static boolean hasAce(ArrayList<Card> hand)
	{
		for(Card card : hand)
		{
			if(card.getRank().equals(Rank.Ace)) return true;
		}
		return false;
	}
	
	//load image and return image object
	public static BufferedImage loadImage(final String path)
	{		
		BufferedImage img = null;
		try {			
			img = ImageIO.read(new File(path));			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to load " + path);
			e.printStackTrace();
		}		
		return img;
	}
	
	//check if the string contains only numbers
	public static boolean isNumeric(String str)
	{
		if(str.matches("[0-9]+")) return true;
		return false;
	}
}
