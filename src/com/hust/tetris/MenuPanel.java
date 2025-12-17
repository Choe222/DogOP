package com.hust.tetris;

import com.hust.tetris.design.DesignButton;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;

/**
 * Fix this class to build the Menu panel whatever u want.
 * */

public class MenuPanel extends JPanel {
	
	private JButton start;
	private JButton levelO;
	private JButton levelTo;
	private JButton levelTe;
	private JButton levelFo;
	private JButton levelFi;
	private JButton how;
	
    public MenuPanel(TetrisFrame frame) {
    	setLayout(null);
    	
    	//"level" Button
    	levelO = DesignButton.design("./levelOne.png","./levelOnePressed.png",57,201);
    	levelTo = DesignButton.design("./levelTo.png","./levelToPressed.png",77,201);
    	levelTe = DesignButton.design("./levelTe.png","./levelTePressed.png",97,201);
    	levelFo = DesignButton.design("./levelFor.png","./levelForPressed.png",117,201);
    	levelFi = DesignButton.design("./levelFi.png","./levelFiPressed.png",137,201);
    	
    	//"How to play" Button
    	how = DesignButton.design("./how.png","./howPressed.png",130,150);
    	
    	//"start" Button
        start = DesignButton.design("./normalStart.png", "./hoverStart.png", "./pressedStart.png", 57,371);
        
        //Menu img
    	String url = new String("./Menu.png");
    	Image bgI = new ImageIcon(getClass().getResource(url)).getImage();
    	
        add(start);
        add(levelO);
        add(levelTo);
        add(levelTe);
        add(levelFo);
        add(levelFi);
        add(how);
        setPreferredSize(new Dimension(bgI.getWidth(this), bgI.getHeight(this)));
    }
 
    public JButton getStart() {
		return start;
	}

	public JButton getLevelO() {
		return levelO;
	}

	public JButton getLevelTo() {
		return levelTo;
	}

	public JButton getLevelTe() {
		return levelTe;
	}

	public JButton getLevelFo() {
		return levelFo;
	}

	public JButton getLevelFi() {
		return levelFi;
	}

	@Override
    protected void paintComponent(Graphics g) {
    	String url = new String("./Menu.png");
    	Image bgI = new ImageIcon(getClass().getResource(url)).getImage();

    	g.drawImage(bgI, 0, 0, bgI.getWidth(this), bgI.getHeight(this), this);
    }
}
