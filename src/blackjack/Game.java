package blackjack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Game{	
	//========================================================================
	//Objects
	//========================================================================
	private UserInterface UI;

	//========================================================================
	//Constants
	//========================================================================
	private static final int INITIAL_BALANCE = 1000;
	private static final int INITIAL_BID = 100;

	//maximum number of cards possible
	// 4 aces, 4 2's, and 3 3's -> 11 cards, rank = 4 + 8 + 9 = 21
	private static final int MAX_CARDS = 11;

	//allow access to Table class
	public static final int GAME_STATE_ONGOING = 0;
	public static final int GAME_STATE_YOUWIN = 1;
	public static final int GAME_STATE_YOULOSE = 2;
	public static final int GAME_STATE_NOWINNER = 3;
	public static final int GAME_STATE_BLACKJACK = 4;

	//game modes
	public static final int GAME_MODE_NORMAL = 0;
	//(J, Q, K) are (11, 12, 13) respectively
	//and you have the choice to only draw one card
	public static final int GAME_MODE_SPECIAL = 1;

	//========================================================================
	//Game-Specific variables
	//========================================================================
	private Deck returnCardDeck = new Deck();
	private Deck servingCardDeck = new Deck(false, returnCardDeck);

	final ArrayList<Card> tableCards = new ArrayList<>(); // cards table has
	final ArrayList<Card> playerCards = new ArrayList<>(); // cards player has

	private int balance;	//if this goes to 0 then its game over
	private int bid; // current bid
	private boolean purchasedInsurance; // did player earned insurance?

	private Player player = new Player("YOU");
	private Dealer table = new Dealer("DEALER");

	private int gameMode;

	public Game()
	{
		init();
	}

	private void init()
	{
		//start UI
		UI = new UserInterface(this);

		balance = INITIAL_BALANCE;

		UI.setNames(player.name, table.name);

		//start new round
		reset();		
	}

	//start new round
	private void reset()
	{			
		// game has not started -> no insurance, no ace dealt, ranks are 0
		purchasedInsurance = false;

		player.setHasAce(false);
		table.setHasAce(false);
		player.setHasDrawn(false);

		bid = INITIAL_BID;	//set back to default every round

		returnCardDeck.addCards(player.cards);
		returnCardDeck.addCards(table.cards);
		player.cards.clear();
		table.cards.clear();
		servingCardDeck.moveCards(returnCardDeck);	//move returned cards back to serving deck and shuffle

		UI.setModeComboBoxState(true);	//enable drop down
		UI.setBidFieldState(true);	//enable bid field
		UI.setActionBarState(UserInterface.STATE_DEAL);
		UI.resetTable();
		UI.updateHUD(balance, bid);
		UI.setTint(false);
		UI.updateGameState(GAME_STATE_ONGOING);
	}

	//change the current game mode and alter the values of face cards
	private void changeGameMode(int gameMode)
	{
		try
		{
			this.gameMode = gameMode;
			if(this.gameMode == GAME_MODE_NORMAL)
			{
				Rank.Jack.changeNumericValue(10);
				Rank.Queen.changeNumericValue(10);
				Rank.King.changeNumericValue(10);
			}
			else if(this.gameMode == GAME_MODE_SPECIAL)
			{
				Rank.Jack.changeNumericValue(11);
				Rank.Queen.changeNumericValue(12);
				Rank.King.changeNumericValue(13);
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}catch(Exception e)
		{
			System.out.println("Invalid game mode");
		}
	}

	//make the player draw a card
	private void playerDrawCard()
	{
		final Card playerCard = servingCardDeck.take();	//player is dealt a card from the serving deck
		player.cards.add(playerCard);
		if (playerCard.getRank().equals(Rank.Ace))	//if the card is an ace set flag 
		{
			player.setHasAce(true);
		}
	}

	// logic to handle table's turn
	private void tableTurn() {
		// if table's rank is less than or 16, it continues to draw a card
		// otherwise stays
		// for this assume ace is 11
		// finally, a winner/loser is decided and game ends
		int tableRankFinal = table.getRank() + (table.getHasAce() ? 10 : 0);

		for (int i = 2  ; i < MAX_CARDS && tableRankFinal <= 16; i++)	//keep drawing until max cards or over 16 
		{
			final Card tableCard = servingCardDeck.take();     //draw a card       
			table.cards.add(tableCard);

			if (!table.getHasAce() && tableCard.getRank().equals(Rank.Ace)) {
				table.setHasAce(true);
				tableRankFinal += 10;
			}			
			tableRankFinal += tableCard.getRank().getNumericValue();
		}

		UI.updateTableHand(table.cards);	//update the screen
	}

	private void blackjack()
	{
		balance += 2 * bid;

		UI.setActionBarState(UserInterface.STATE_RESULT);
		UI.setTint(true);
		UI.updateHUD(balance, bid);
		UI.updateGameState(GAME_STATE_BLACKJACK);
	}

	//determines winner or loser
	private void determineWinner() {
		final int playerScore = player.computeScore();
		final int tableScore = table.computeScore();

		if (playerScore > 21) {
			// if player score is > 21, doesn't matter what is the
			// score of the table, player loses

			balance -= bid;

			UI.updateGameState(GAME_STATE_YOULOSE);
		} else if (tableScore > 21) {
			// else, if table went overboard, player is the winner

			balance += bid;

			UI.updateGameState(GAME_STATE_YOUWIN);
		} else if (playerScore == tableScore) {
			// both are less than 21, but same score, no winner
			UI.updateGameState(GAME_STATE_NOWINNER);
		} else if (playerScore > tableScore) {
			// player has the higher score, player wins winner

			balance += bid;

			UI.updateGameState(GAME_STATE_YOUWIN);
		} else if(tableScore == 21){
			// table scored a blackjack so you lose
			if(purchasedInsurance)	//if insurance was bought, then only lose half of the bid
			{
				bid /= 2;
			}
			balance -= bid;

			UI.updateGameState(GAME_STATE_YOULOSE);
		} else { // if (tableScore > playerScore) ...
			// table has higher score, so player is the loser            

			balance -= bid;

			UI.updateGameState(GAME_STATE_YOULOSE);
		}

		//update the value of balance

		UI.setActionBarState(UserInterface.STATE_RESULT);
		UI.updateHUD(balance, bid);
		UI.setTint(true);
	}

	//========================================================================
	//Button callback methods
	//========================================================================

	//when the deal button is clicked
	public void onDeal()
	{
		changeGameMode(UI.getGameMode());

		//only do this the first time
		if(!player.getHasDrawn())
		{
			if(!UI.validateBidAmount(balance)) return;
			bid = UI.getBidAmount();

			UI.setModeComboBoxState(false);	//disable drop down
			UI.setBidFieldState(false);	//disable bid field
			UI.setActionBarState(UserInterface.STATE_INSURANCE_OR_SURRENDER);		

			UI.resetTable();

			//table draws one card
			final Card tableCard = servingCardDeck.take();
			if(tableCard.getRank().equals(Rank.Ace))
			{
				table.setHasAce(true);
			}
			table.cards.add(tableCard);
			UI.updateTableHand(table.cards);
		}	

		if(gameMode == GAME_MODE_SPECIAL)	//only draw one card at a time if game mode is SPECIAL
		{
			playerDrawCard();
			//UI.updatePlayerHand(playerCards);
			UI.updatePlayerHand(player.cards);

			//if(!playerHasDrawn)	//if this is the first time
			if(!player.getHasDrawn())
			{
				UI.setActionBarState(UserInterface.STATE_DEAL_AGAIN);	//click deal one more time to draw another card				
				Util.delay(250);				
			}
			else	//second time
			{
				bid *= 2;	//double bid
				UI.updateHUD(balance, bid);
				UI.setActionBarState(UserInterface.STATE_INSURANCE_OR_SURRENDER);
				if(purchasedInsurance)	//disable the insurance button after once
				{
					UI.setInsuranceState(false);
				}
			}

			//playerHasDrawn = true;
			player.setHasDrawn(true);

		}
		else	//NORMAL MODE
		{
			//player draws two cards
			for(int i = 0;i < 2;i++)
			{
				playerDrawCard();
			}		
			//UI.updatePlayerHand(playerCards);
			UI.updatePlayerHand(player.cards);
		}

		//check score for blackjack
		if(player.getRank() == 21 || (player.getRank() == 11 && player.getHasAce()))
		{
			blackjack();
		}
	}

	//when the hit button is clicked
	public void onHit()
	{	
		UI.setActionBarState(UserInterface.STATE_PLAY);

		playerDrawCard();

		//draw the new rank and disable the double button
		UI.updatePlayerHand(player.cards);
		UI.setDoubleState(false);

		// if player goes over 21, then it's a bust
		if(player.getRank() > 21)
		{
			UI.setActionBarState(UserInterface.STATE_RESULT);

			determineWinner();
		}           
		else if(player.getRank() == 21 || (player.getRank() == 11 && player.getHasAce()))
		{
			blackjack();
		}
	}

	//when the double button is clicked
	public void onDouble()
	{
		bid *= 2;
		UI.updateHUD(balance, bid);

		playerDrawCard();
		//UI.updatePlayerHand(playerCards);
		UI.updatePlayerHand(player.cards);

		tableTurn();

		determineWinner();
	}

	//when the stand button is clicked
	public void onStand()
	{
		tableTurn();
		determineWinner();
	}

	//when the insurance button is clicked
	public void onInsurance()
	{
		bid *= 2;
		UI.updateHUD(balance, bid);

		purchasedInsurance = true;

		if(purchasedInsurance)	//disable the insurance button after once
		{
			UI.setInsuranceState(false);
		}
	}

	//when the surrender button is clicked
	public void onSurrender()
	{
		balance -= bid;

		UI.updateGameState(GAME_STATE_YOULOSE);

		UI.setActionBarState(UserInterface.STATE_RESULT);
		UI.updateHUD(balance, bid);
		UI.setTint(true);		
	}

	//when the ready button is clicked
	public void onReady()
	{
		UI.setActionBarState(UserInterface.STATE_PLAY);
	}

	//when the restart button is clicked
	public void onRestart()
	{
		reset();
	}
}
