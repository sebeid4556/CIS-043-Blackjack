package blackjack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionBar extends JPanel{
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 50;
	
	private String BUTTON_NAME_INSURANCE = "INSURANCE";
	private String BUTTON_NAME_SURRENDER = "SURRENDER";
	private String BUTTON_NAME_READY = "READY";
	private String BUTTON_NAME_DEAL = "DEAL";
	private String BUTTON_NAME_STAND = "STAND";	
	private String BUTTON_NAME_DOUBLE = "DOUBLE";
	private String BUTTON_NAME_HIT = "HIT";
	private String BUTTON_NAME_RESTART = "RESTART";
	
	//public to allow access to UserInterface class
	public JButton button_insurance;
	public JButton button_surrender;
	public JButton button_ready;
	public JButton button_deal;
	public JButton button_stand;	
	public JButton button_double;
	public JButton button_hit;
	public JButton button_restart;
	
	public ActionBar(UserInterface uiObj)
	{	
		setLayout(new GridLayout());
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
		setBorder(BorderFactory.createRaisedBevelBorder());
		
		button_insurance = new JButton(BUTTON_NAME_INSURANCE);
		button_surrender = new JButton(BUTTON_NAME_SURRENDER);
		button_ready = new JButton(BUTTON_NAME_READY);
		button_deal = new JButton(BUTTON_NAME_DEAL);
		button_stand = new JButton(BUTTON_NAME_STAND);		
		button_double = new JButton(BUTTON_NAME_DOUBLE);
		button_hit = new JButton(BUTTON_NAME_HIT);
		button_restart = new JButton(BUTTON_NAME_RESTART);
		
		button_insurance.addActionListener(uiObj);
		button_surrender.addActionListener(uiObj);
		button_ready.addActionListener(uiObj);
		button_deal.addActionListener(uiObj);
		button_hit.addActionListener(uiObj);
		button_double.addActionListener(uiObj);
		button_stand.addActionListener(uiObj);
		button_restart.addActionListener(uiObj);
		
		add(button_insurance);
		add(button_surrender);
		add(button_ready);
		add(button_deal);
		add(button_hit);
		add(button_double);
		add(button_stand);
		add(button_restart);
	}
}
