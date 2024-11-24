package blackjack;

import java.awt.*;
import javax.swing.*;

public class UserInterface {
	
	//========================================================================
	//Constants
	//=======================================================================
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 600;
	
	/*public static final int TOP_BAR_HEIGHT = WINDOW_HEIGHT / 12;
	public static final int TABLE_HEIGHT = (WINDOW_HEIGHT * 2) / 3;
	public static final int BOTTOM_BAR_HEIGHT = WINDOW_HEIGHT / 12;*/
	public static final int TOP_BAR_HEIGHT = 50;
	public static final int TABLE_HEIGHT = 500;
	public static final int BOTTOM_BAR_HEIGHT = 50;
	
	private Color BG_COLOR = new Color(0, 0, 0);
	private Color TABLE_COLOR = new Color(0, 70, 0);
	private Color TOP_BAR_COLOR = new Color(50, 50, 50);
	private Color BOTTOM_BAR_COLOR = new Color(50, 50, 50);
	
	private String TITLE = "Blackjack";
	private String BUTTON_NAME_STAND = "STAND";
	private String BUTTON_NAME_SPLIT = "SPLIT";
	private String BUTTON_NAME_DOUBLE = "DOUBLE";
	private String BUTTON_NAME_HIT = "HIT";
	
	//========================================================================
	//Jswing components
	//========================================================================
	private JFrame frame;
	
	private JPanel background;	//common background
	private JPanel top_bar;	//top HUD (will contain info)
	private JPanel table;	//all the plays happen here
	private JPanel bottom_bar;	//bottom HUD (will contain buttons)
	
	private JButton button_stand;
	private JButton button_split;
	private JButton button_double;
	private JButton button_hit;
	
	public UserInterface()
	{
		init();
	}
	
	private void init()
	{
		//Create component objects
		
		frame = new JFrame(TITLE);
		
		background = new JPanel();
		
		top_bar = new JPanel();		
		table = new JPanel();		
		bottom_bar = new JPanel();		
		
		button_stand = new JButton(BUTTON_NAME_STAND);
		button_split = new JButton(BUTTON_NAME_SPLIT);
		button_double = new JButton(BUTTON_NAME_DOUBLE);
		button_hit = new JButton(BUTTON_NAME_HIT);
		
		//Configure components
		
		background.setBackground(BG_COLOR);		
		background.setLayout(new BoxLayout(background, BoxLayout.PAGE_AXIS));
		background.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		top_bar.setBackground(TOP_BAR_COLOR);
		top_bar.setPreferredSize(new Dimension(WINDOW_WIDTH, TOP_BAR_HEIGHT));
		
		table.setBackground(TABLE_COLOR);
		table.setPreferredSize(new Dimension(WINDOW_WIDTH, TABLE_HEIGHT));
		
		bottom_bar.setBackground(BOTTOM_BAR_COLOR);
		bottom_bar.setLayout(new GridLayout());
		bottom_bar.setPreferredSize(new Dimension(WINDOW_WIDTH, BOTTOM_BAR_HEIGHT));
		
		//Add components to containers
		
		background.add(top_bar);
		background.add(table);
		background.add(bottom_bar);
		
		bottom_bar.add(button_stand);
		bottom_bar.add(button_split);
		bottom_bar.add(button_double);
		bottom_bar.add(button_hit);
		
		frame.add(background);
		
		//configure frame
		
		frame.setLayout(new BorderLayout());		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		frame.pack();
		frame.setVisible(true);
	}
}
