package blackjack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class UserInterface extends JFrame implements ActionListener{

	//========================================================================
	//Constants
	//=======================================================================

	//These two might not be necessary
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 600;

	private Color BG_COLOR = new Color(0, 0, 0);

	private String TITLE = "BLACKJACK";

	//action bar states
	public static final int STATE_DEAL = 0;
	public static final int STATE_INSURANCE_OR_SURRENDER = 1;
	public static final int STATE_PLAY = 2;
	public static final int STATE_RESULT = 3;
	public static final int STATE_DEAL_AGAIN = 4;

	//========================================================================
	//Objects
	//========================================================================
	private JPanel background;	//common background
	//Custom objects
	private Hud HUD;
	private Table table;	//basically a canvas (extends JPanel)
	private ActionBar action_bar;

	private Game gameObj;

	public UserInterface(Game gameObj)
	{			
		this.gameObj = gameObj;

		init();
	}

	private void init()
	{	
		setTitle(TITLE);

		//create components
		background = new JPanel();

		table = new Table();
		HUD = new Hud(this);
		action_bar = new ActionBar(this);

		//Configure components

		background.setBackground(BG_COLOR);		
		background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
		background.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

		//Add components to containers

		background.add(HUD);
		background.add(table);
		background.add(action_bar);				

		add(background);

		//configure frame

		setLayout(new BorderLayout());		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		pack();
		setVisible(true);
	}

	//update the cards shown on the screen for the Table
	public void updateTableHand(ArrayList<Card> hand)
	{
		table.updateTableHand(hand);
	}

	//update the cards shown on the screen for the player
	public void updatePlayerHand(ArrayList<Card> hand)
	{
		table.updatePlayerHand(hand);
	}

	//reset the table back to initial states
	public void resetTable()
	{
		table.reset();
	}

	//show updated balance and bid
	public void updateHUD(int balance, int bid)
	{
		HUD.updateBalance(balance);
		HUD.updateBid(bid);
	}

	public void setTint(boolean flag)
	{
		table.setTint(flag);
	}

	public void updateGameState(int state)
	{
		table.updateCurrentGameState(state);
	}

	//enable or disable insurance, surrender, and ready buttons
	private void setInsuranceAndSurrenderState(boolean flag)
	{
		action_bar.button_insurance.setEnabled(flag);
		action_bar.button_surrender.setEnabled(flag);
		action_bar.button_ready.setEnabled(flag);
	}

	//enable or disable play buttons
	private void setPlayState(boolean flag)
	{
		action_bar.button_hit.setEnabled(flag);
		action_bar.button_double.setEnabled(flag);
		action_bar.button_stand.setEnabled(flag);
	}

	//enable or disable double button
	//this method is public because the game logic needs to access it
	public void setDoubleState(boolean flag)
	{
		action_bar.button_double.setEnabled(flag);
	}

	public void setBidFieldState(boolean flag)
	{		
		HUD.fldBid.setEditable(flag);
	}
	
	//get the game mode from the drop down menu
	public int getGameMode()
	{
		return HUD.getModeComboBoxValue();
	}
	
	//enable or disable the drop down menu
	public void setModeComboBoxState(boolean flag)
	{
		HUD.cmbMode.setEnabled(flag);
	}

	//return the amount entered into text field
	public int getBidAmount()
	{
		return Integer.valueOf(HUD.fldBid.getText());
	}

	public boolean validateBidAmount(int balance)
	{
		if(!Util.isNumeric(HUD.fldBid.getText()))
		{
			JOptionPane.showMessageDialog(this, "Bid must be a positive whole number", "Invalid Bid", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		int bid = getBidAmount();
		if(bid > balance)
		{
			JOptionPane.showMessageDialog(this, "Bid cannot be greater than the available balance", "Invalid Bid", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else if(bid < 2)
		{
			JOptionPane.showMessageDialog(this, "Minimum bid is $2", "Invalid Bid", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else if(bid % 2 != 0)
		{
			JOptionPane.showMessageDialog(this, "Bid must be even", "Invalid Bid", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else	//valid bid
		{
			return true;
		}
	}

	//enable/disable buttons depending on game state
	public void setActionBarState(int state)
	{
		try {
			switch(state)
			{
			case STATE_DEAL:
				action_bar.button_deal.setEnabled(true);
				setInsuranceAndSurrenderState(false);
				setPlayState(false);
				action_bar.button_restart.setEnabled(false);
				break;
			case STATE_INSURANCE_OR_SURRENDER:
				action_bar.button_deal.setEnabled(false);
				setInsuranceAndSurrenderState(true);
				setPlayState(false);
				action_bar.button_restart.setEnabled(false);
				break;
			case STATE_PLAY:
				action_bar.button_deal.setEnabled(false);
				setInsuranceAndSurrenderState(false);
				setPlayState(true);
				action_bar.button_restart.setEnabled(false);
				break;
			case STATE_DEAL_AGAIN:
				action_bar.button_deal.setEnabled(true);
				setInsuranceAndSurrenderState(false);				
				setPlayState(false);
				action_bar.button_restart.setEnabled(false);
				action_bar.button_ready.setEnabled(true);	//enable the ready button for deal again
				break;
			case STATE_RESULT:
				action_bar.button_deal.setEnabled(false);
				setInsuranceAndSurrenderState(false);
				setPlayState(false);
				action_bar.button_restart.setEnabled(true);
				break;
			default:
				throw new IllegalArgumentException();					
			}
		}catch(Exception e){
			System.out.println("Invalid argument for setActionBarState");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();		
		if(source == action_bar.button_deal)
		{			
			gameObj.onDeal();			
		}
		else if(source == action_bar.button_hit)
		{
			gameObj.onHit();
		}
		else if(source == action_bar.button_double)
		{
			gameObj.onDouble();
		}
		else if(source == action_bar.button_stand)
		{
			gameObj.onStand();
		}
		else if(source == action_bar.button_insurance)
		{
			gameObj.onInsurance();
		}
		else if(source == action_bar.button_surrender)
		{
			gameObj.onSurrender();
		}
		else if(source == action_bar.button_ready)
		{
			gameObj.onReady();
		}
		else if(source == action_bar.button_restart)
		{
			gameObj.onRestart();
		}
	}
}
