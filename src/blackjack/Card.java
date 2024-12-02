package blackjack;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Card {
    private final Rank rank;
    private final Suite suite;
    private ImageIcon icon;
    private BufferedImage image;

    public Card(final Rank rank, final Suite suite) {
        this.rank = rank;
        this.suite = suite;
        //this.icon = new ImageIcon(getClass().getResource("images/cards/" + rank.getImageFileName() + "_of_" + suite.getImageFileName() + ".png"));
        try {
			this.image = ImageIO.read(new File("images/cards/" + rank.getImageFileName() + "_of_" + suite.getImageFileName() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public Rank getRank() {
        return rank;
    }

    public Suite getSuite() {
        return suite;
    }

    public ImageIcon getIcon() {
        return icon;
    }
    
    public BufferedImage getImage()
    {
    	return image;
    }

    @Override
    public String toString() {
        return rank.toString() + suite.toString();
    }
}
