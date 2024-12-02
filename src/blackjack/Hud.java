package blackjack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Hud extends JPanel{
	
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 50;
	private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);	//gray
	
	private static final String BALANCE_BASE_TEXT = "BALANCE: $";
	private static final String BID_BASE_TEXT = "                     BID: $";
	
	private JLabel lblBalance;
	private JLabel lblBid;	
	public JTextField fldBid;	//public to allow access to UserInterface
	
	private Font font;
	private static final int fontSize = 20;	
	
	public Hud(UserInterface uiObj)
	{
		loadResources();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new GridLayout());
		
		lblBalance = new JLabel(BALANCE_BASE_TEXT);
		lblBid = new JLabel(BID_BASE_TEXT);
		fldBid = new JTextField();
		
		lblBalance.setOpaque(true);
		lblBalance.setBackground(BACKGROUND_COLOR);
		lblBalance.setForeground(Color.white);
		lblBalance.setFont(font);
		lblBid.setOpaque(true);
		lblBid.setBackground(BACKGROUND_COLOR);
		lblBid.setForeground(Color.white);
		lblBid.setFont(font);
		lblBid.setText(BID_BASE_TEXT);
		fldBid.setOpaque(true);
		fldBid.setBackground(new Color(200, 200, 200));
		fldBid.setFont(font);		
		
		fldBid.addActionListener(uiObj);
		
		add(lblBalance);
		add(lblBid);
		add(fldBid);
	}
	
	private void loadResources()
	{
		font = new Font("Courier New Bold", Font.PLAIN, fontSize);
	}
	
	public void updateBalance(int balance)
	{
		lblBalance.setText(BALANCE_BASE_TEXT + String.format("%,d", balance));
	}
	
	public void updateBid(int bid)
	{
		fldBid.setText(String.format("%,d", bid));
	}
}
