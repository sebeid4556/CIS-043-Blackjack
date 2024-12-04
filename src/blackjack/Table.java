package blackjack;

import javax.imageio.ImageIO;
import java.awt.font.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Table extends JPanel{

	//========================================================================
	//Constants
	//========================================================================
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 600;
	private static final String BACKGROUND_IMAGE_PATH = "images/background.jpg"; 
	private static final String TINT_IMAGE_PATH = "images/tint.png";
	private static final String YOUWIN_IMAGE_PATH = "images/banners/youwin.png";
	private static final String YOULOSE_IMAGE_PATH = "images/banners/youlose.png";
	private static final String NOWINNER_IMAGE_PATH = "images/banners/nowinner.png";
	private static final String BLACKJACK_IMAGE_PATH = "images/banners/blackjack.png";

	private static final int CENTER_POS_X = WIDTH / 2;
	private static final int CENTER_POS_Y = HEIGHT / 2;

	private static final int TABLE_CARDS_POS_X = 200;
	private static final int TABLE_CARDS_POS_Y = 50;
	private static final int PLAYER_CARDS_POS_X = 200;
	private static final int PLAYER_CARDS_POS_Y = 350;

	private static final int CARD_WIDTH = 100;
	private static final int CARD_HEIGHT = 140;
	private static final int CARD_OFFSET = CARD_WIDTH / 2;

	private static final int TABLE_RANK_POS_X = 800;
	private static final int TABLE_RANK_POS_Y = 100;
	private static final int PLAYER_RANK_POS_X = 800;
	private static final int PLAYER_RANK_POS_Y = 400;

	private static final int TABLE_NAME_POS_X = 100;
	private static final int TABLE_NAME_POS_Y = 100;
	private static final int PLAYER_NAME_POS_X = 100;
	private static final int PLAYER_NAME_POS_Y = 400;

	//========================================================================
	//Resources
	//========================================================================
	private BufferedImage imgBackground;
	private BufferedImage imgTint;
	private BufferedImage imgYouWin;
	private BufferedImage imgYouLose;
	private BufferedImage imgNoWinner;	
	private BufferedImage imgBlackjack;
	private Font font;
	private int font_size = 30;
	private Color font_color = Color.white;

	private ArrayList<Card> tableCards = new ArrayList<>();
	private ArrayList<Card> playerCards = new ArrayList<>();

	private int tableRank;
	private int playerRank; 

	private boolean playerHasAce = false;
	private boolean tableHasAce = false;

	private boolean isTinted = false;

	private int gameState;	//set by UserInterface, set by Game

	private String tableName = "";
	private String playerName = "";

	public Table()
	{
		loadResources();
		configure();
	}

	//load images and fonts
	private void loadResources()
	{
		imgBackground = Util.loadImage(BACKGROUND_IMAGE_PATH);
		imgTint = Util.loadImage(TINT_IMAGE_PATH);
		imgYouWin = Util.loadImage(YOUWIN_IMAGE_PATH);
		imgYouLose = Util.loadImage(YOULOSE_IMAGE_PATH);
		imgNoWinner = Util.loadImage(NOWINNER_IMAGE_PATH);
		imgBlackjack = Util.loadImage(BLACKJACK_IMAGE_PATH);

		font = new Font("TimesRoman", Font.PLAIN, font_size);		
	}	

	//configure the JPanel
	private void configure()
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}
	
	public void setNames(String playerName, String dealerName)
	{
		this.playerName = playerName;
		this.tableName = dealerName;
	}

	//update the cards shown on screen for the table
	public void updateTableHand(ArrayList<Card> hand)
	{
		tableCards = hand;
		tableRank = Util.calculateHandRank(tableCards);	//update the displayed table rank
		tableHasAce = Util.hasAce(tableCards);
		repaint();
	}

	//update the cards shown on screen for the player
	public void updatePlayerHand(ArrayList<Card> hand)
	{
		playerCards = hand;
		playerRank = Util.calculateHandRank(playerCards);
		playerHasAce = Util.hasAce(playerCards);
		repaint();
	}

	//reset cards and ranks
	public void reset()
	{
		tableCards.clear();
		playerCards.clear();
		playerRank = tableRank =  0;

		playerHasAce = tableHasAce = false;

		repaint();
	}

	//tint the screen
	public void setTint(boolean flag)
	{
		isTinted = flag;
		repaint();
	}

	//used to determine which banner to show on the result screen
	public void updateCurrentGameState(int state)
	{
		gameState = state;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)	//draw the scene and the cards
	{		
		super.paintComponent(g);
		g.drawImage(imgBackground, 0, 0, WIDTH, HEIGHT, 0, 0, imgBackground.getWidth(), imgBackground.getHeight(), null);

		//draw table's hand
		int centered_x = CENTER_POS_X - ((CARD_OFFSET * tableCards.size()) / 2);	//center the cards
		for(int i = 0;i < tableCards.size();i++)
		{
			Card card = tableCards.get(i);
			int offset = (i * CARD_OFFSET);	//slight overlap on each card
			g.drawImage(card.getImage(),
					centered_x + offset, TABLE_CARDS_POS_Y,	//top left 
					centered_x + offset + CARD_WIDTH, TABLE_CARDS_POS_Y + CARD_HEIGHT,	//bottom right 
					0, 0, card.getImage().getWidth(), card.getImage().getHeight(), //source clip to draw from
					null);
		}
		//draw player's hand
		centered_x = CENTER_POS_X - ((CARD_OFFSET * playerCards.size()) / 2);	//center the cards
		for(int i = 0;i < playerCards.size();i++)
		{
			Card card = playerCards.get(i);
			int offset = (i * CARD_OFFSET);	//slight overlap on each card
			g.drawImage(card.getImage(),
					centered_x + offset, PLAYER_CARDS_POS_Y, 
					centered_x + offset + CARD_WIDTH, PLAYER_CARDS_POS_Y + CARD_HEIGHT, 
					0, 0, card.getImage().getWidth(), card.getImage().getHeight(), 
					null);
		}

		g.setFont(font);
		g.setColor(font_color);

		//draw player names
		g.drawString(tableName, TABLE_NAME_POS_X, TABLE_NAME_POS_Y);
		g.drawString(playerName, PLAYER_NAME_POS_X, PLAYER_NAME_POS_Y);

		//draw table and player ranks
		String tableRankStr = String.valueOf(tableRank);
		if(tableHasAce && (tableRank + 10 <= 21))
		{
			tableRankStr = String.valueOf(tableRank) + " OR " + String.valueOf(tableRank + 10);
		}
		String playerRankStr = String.valueOf(playerRank);
		if(playerHasAce && (playerRank + 10 <= 21))
		{
			playerRankStr = String.valueOf(playerRank) + " OR " + String.valueOf(playerRank + 10);
		}
		g.drawString(tableRankStr, TABLE_RANK_POS_X, TABLE_RANK_POS_Y);
		g.drawString(playerRankStr, PLAYER_RANK_POS_X, PLAYER_RANK_POS_Y);

		if(isTinted)
		{
			g.drawImage(imgTint, 
					0, 0, WIDTH, HEIGHT, 
					0, 0, imgTint.getWidth(), imgTint.getHeight(), 
					null);
		}

		//show result banner
		BufferedImage banner = null;
		switch(gameState)
		{
		case Game.GAME_STATE_ONGOING:				
			break;
		case Game.GAME_STATE_YOUWIN:
			banner = imgYouWin;
			break;
		case Game.GAME_STATE_YOULOSE:
			banner = imgYouLose;
			break;
		case Game.GAME_STATE_NOWINNER:
			banner = imgNoWinner;
			break;
		case Game.GAME_STATE_BLACKJACK:
			banner = imgBlackjack;
			break;
		default:
			;
		}
		if(banner != null)	//don't show banner if game is still ongoing
		{
			g.drawImage(banner, 
					CENTER_POS_X - (banner.getWidth() / 2), CENTER_POS_Y - banner.getHeight() - 50, 
					CENTER_POS_X - (banner.getWidth() / 2) + banner.getWidth(), CENTER_POS_Y - (banner.getHeight() / 2) + banner.getHeight(), 
					0, 0, banner.getWidth(), banner.getHeight(),
					null);		
		}
	}
}
