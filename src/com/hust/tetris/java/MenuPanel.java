package com.hust.tetris.java;

import com.hust.tetris.java.*;
import com.hust.tetris.java.design.DesignButton;
import com.hust.tetris.java.resources.ResourceManager;

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
    	levelO = DesignButton.design(ResourceManager.LV1_NORMAL,ResourceManager.LV1_PRESSED,57,201);
    	levelTo = DesignButton.design(ResourceManager.LV2_NORMAL,ResourceManager.LV2_PRESSED,77,201);
    	levelTe = DesignButton.design(ResourceManager.LV3_NORMAL,ResourceManager.LV3_PRESSED,97,201);
    	levelFo = DesignButton.design(ResourceManager.LV4_NORMAL,ResourceManager.LV4_PRESSED,117,201);
    	levelFi = DesignButton.design(ResourceManager.LV5_NORMAL,ResourceManager.LV5_PRESSED,137,201);
    	
    	//"How to play" Button
    	how = DesignButton.design(ResourceManager.HOW_TO_PLAY_NORMAL,ResourceManager.HOW_TO_PLAY_PRESSED,130,150);
    	
    	//"start" Button
        start = DesignButton.design(ResourceManager.START_NORMAL, ResourceManager.START_HOVER, ResourceManager.START_PRESSED, 57,371);
        
        //Menu img
    	Image bgI = new ImageIcon(getClass().getResource(ResourceManager.MENU)).getImage();
    	
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
    	Image bgI = new ImageIcon(getClass().getResource(ResourceManager.MENU)).getImage();

    	g.drawImage(bgI, 0, 0, bgI.getWidth(this), bgI.getHeight(this), this);
    }
}
