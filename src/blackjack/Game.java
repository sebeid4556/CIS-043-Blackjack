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

	//========================================================================
	//Game-Specific variables
	//========================================================================
	private Deck returnCardDeck = new Deck();
	private Deck servingCardDeck = new Deck(false, returnCardDeck);

	final ArrayList<Card> tableCards = new ArrayList<>(); // cards table has
	final ArrayList<Card> playerCards = new ArrayList<>(); // cards player has

	int balance;	//if this goes to 0 then its game over
	int bid; // current bid
	int tableEarning; // total earning for table
	int playerEarning; // total earning for player
	boolean purchasedInsurance; // did player earned insurance?

	int tableRank; // table's total rank
	int playerRank; // player's total rank
	boolean tableHasAce; // does the table have an ace
	boolean playerHasAce; // does the player have an ace

	public Game()
	{
		init();
	}

	private void init()
	{
		//start UI
		UI = new UserInterface(this);

		balance = INITIAL_BALANCE;

		//start new round
		reset();		
	}

	//start new round
	private void reset()
	{	
		// game has not started -> no insurance, no ace dealt, ranks are 0
		purchasedInsurance = false;
		tableHasAce = playerHasAce = false;
		playerRank = tableRank = 0;

		bid = INITIAL_BID;

		// return player and table's card return deck
		returnCardDeck.addCards(playerCards);
		returnCardDeck.addCards(tableCards);
		playerCards.clear();
		tableCards.clear();
		servingCardDeck.moveCards(returnCardDeck);	//move returned cards back to serving deck and shuffle

		UI.setBidFieldState(true);
		UI.setActionBarState(UserInterface.STATE_DEAL);
		UI.resetTable();
		UI.updateHUD(balance, bid);
		UI.setTint(false);
		UI.updateGameState(GAME_STATE_ONGOING);
	}

	//make the player draw a card
	private void playerDrawCard()
	{
		final Card playerCard = servingCardDeck.take();
		playerCards.add(playerCard);	//player is dealt a card from the serving deck
		if (playerCard.getRank().equals(Rank.Ace))	//if the card is an ace set flag 
		{
			playerHasAce = true;
		}
		//playerRank += playerCard.getRank().getNumericValue();	//increment player rank
		playerRank = Util.calculateHandRank(playerCards);
	}

	// logic to handle table's turn
	private void tableTurn() {
		// if table's rank is less than or 16, it continues to draw a card
		// otherwise stays
		// for this assume ace is 11
		// finally, a winner/loser is decided and game ends
		int tableRankFinal = tableRank + (tableHasAce ? 10 : 0);

		for (int i = 2  ; i < MAX_CARDS && tableRankFinal <= 16; i++)	//keep drawing until max cards or over 16 
		{
			final Card tableCard = servingCardDeck.take();     //draw a card       
			tableCards.add(tableCard);

			if (!tableHasAce && tableCard.getRank().equals(Rank.Ace)) {
				tableHasAce = true;
				tableRankFinal += 10;
			}
			tableRank += tableCard.getRank().getNumericValue();
			tableRankFinal += tableCard.getRank().getNumericValue();
		}

		UI.updateTableHand(tableCards);	//update the screen
	}

	private int computePlayerScore() {
		if (playerHasAce) {
			return (playerRank + 10) > 21 ? playerRank : playerRank + 10;
		} else {
			return playerRank;
		}
	}

	private int computeTableScore() {
		if (tableHasAce) {
			return  (tableRank + 10) > 21 ? tableRank : tableRank + 10;
		} else {
			return tableRank;
		}
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
		final int playerScore = computePlayerScore();
		final int tableScore = computeTableScore();        

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
		if(!UI.validateBidAmount(balance)) return;
		bid = UI.getBidAmount();

		UI.setBidFieldState(false);
		UI.setActionBarState(UserInterface.STATE_INSURANCE_OR_SURRENDER);		

		UI.resetTable();


		//table draws one card
		final Card tableCard = servingCardDeck.take();
		if(tableCard.getRank().equals(Rank.Ace))
		{
			tableHasAce = true;
		}
		tableCards.add(tableCard);
		tableRank = Util.calculateHandRank(tableCards);
		UI.updateTableHand(tableCards);		

		//player draws two cards
		for(int i = 0;i < 2;i++)
		{
			playerDrawCard();
		}		
		UI.updatePlayerHand(playerCards);				
	}

	//when the hit button is clicked
	public void onHit()
	{	
		UI.setActionBarState(UserInterface.STATE_PLAY);

		playerDrawCard();

		//draw the new rank and disable the double button
		UI.updatePlayerHand(playerCards);
		UI.setDoubleState(false);

		// if player goes over 21, then it's a bust
		if (playerRank > 21) 
		{
			UI.setActionBarState(UserInterface.STATE_RESULT);

			determineWinner();
		}           
		else if(playerRank == 21 || (playerRank == 11 && playerHasAce))
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
		UI.updatePlayerHand(playerCards);

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
