package blackjack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Hud extends JPanel{

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 50;
	private static final Color BACKGROUND_COLOR = new Color(50, 50, 50);	//gray
	private static final Color MISC_FOREGROUND_COLOR = new Color(200, 200, 200);	//light gray

	private static final String BALANCE_BASE_TEXT = "BALANCE: $";
	private static final String BID_BASE_TEXT = "BID: $";
	private static final String MODE_BASE_TEXT = "MODE:";
	private static final String MODE_TEXT_NORMAL = "NORMAL";
	private static final String MODE_TEXT_SPECIAL = "SPECIAL";
	private static final String[] MODES = {MODE_TEXT_NORMAL, MODE_TEXT_SPECIAL};	//for the drop down

	private JLabel lblBalance;
	private JLabel lblBid;		
	public JTextField fldBid;	//public to allow access to UserInterface
	private JLabel lblMode;
	public JComboBox cmbMode;	

	private Font font;
	private static final int fontSize = 20;	

	public Hud(UserInterface uiObj)
	{
		loadResources();
		configure(uiObj);
	}

	//load font
	private void loadResources()
	{
		font = new Font("Courier New Bold", Font.PLAIN, fontSize);
	}

	//configure the components
	private void configure(UserInterface uiObj)
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.black);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setLayout(new GridLayout());

		lblMode = new JLabel(MODE_BASE_TEXT);
		lblBalance = new JLabel(BALANCE_BASE_TEXT);
		lblBid = new JLabel(BID_BASE_TEXT);		
		fldBid = new JTextField();
		cmbMode = new JComboBox(MODES);

		lblMode.setOpaque(true);
		lblMode.setBackground(BACKGROUND_COLOR);
		lblMode.setForeground(Color.white);
		lblMode.setFont(font);		
		lblMode.setHorizontalAlignment(JLabel.RIGHT);
		cmbMode.setOpaque(true);
		cmbMode.setBackground(MISC_FOREGROUND_COLOR);
		cmbMode.setFont(font);
		lblBalance.setOpaque(true);
		lblBalance.setBackground(BACKGROUND_COLOR);
		lblBalance.setForeground(Color.white);
		lblBalance.setFont(font);
		lblBalance.setHorizontalAlignment(JLabel.RIGHT);
		lblBid.setOpaque(true);
		lblBid.setBackground(BACKGROUND_COLOR);
		lblBid.setForeground(Color.white);
		lblBid.setFont(font);
		lblBid.setText(BID_BASE_TEXT);
		lblBid.setHorizontalAlignment(JLabel.RIGHT);
		fldBid.setOpaque(true);
		fldBid.setBackground(MISC_FOREGROUND_COLOR);
		fldBid.setFont(font);		

		fldBid.addActionListener(uiObj);

		add(lblMode);
		add(cmbMode);
		add(lblBalance);
		add(lblBid);
		add(fldBid);
	}

	//update the balance value shown on the screen
	public void updateBalance(int balance)
	{
		lblBalance.setText(BALANCE_BASE_TEXT + String.format("%,d", balance));
	}

	//update the bid amount shown in the text field
	public void updateBid(int bid)
	{
		fldBid.setText(String.format("%,d", bid));
	}

	//return the selected index of the drop down menu
	public int getModeComboBoxValue()
	{
		return cmbMode.getSelectedIndex();
	}
}
