package com.hust.tetris.java.design;

import java.awt.Cursor;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class will help design the JButton with it's normal image, hover image and pressed image. 
 * It's also put the button in the right position which u want.  
 * */

public class DesignButton {
	
	/**
	 * This method design JButton when having normal, hover and pressed imgs. 
	 * @return designed JButton.
	 * */
	
	public static JButton design(String normal, String hover, String pressed, int x, int y) {
		URL normalUrl = DesignButton.class.getResource(normal);
	    URL hoverUrl = DesignButton.class.getResource(hover);
	    URL pressedUrl = DesignButton.class.getResource(pressed);
		
	    ImageIcon iNormal = new ImageIcon(normalUrl);
	    ImageIcon iHover = new ImageIcon(hoverUrl);
	    ImageIcon iPressed = new ImageIcon(pressedUrl);

	    JButton btn = new JButton(iNormal);
	    btn.setRolloverIcon(iHover);
	    btn.setPressedIcon(iPressed);

	    btn.setBorderPainted(false);
	    btn.setContentAreaFilled(false);
	    btn.setFocusPainted(false);
	    btn.setOpaque(false);
	    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    btn.setRolloverEnabled(true);

	    btn.setPreferredSize(new Dimension(iNormal.getIconWidth(), iNormal.getIconHeight()));
	    
	    btn.setBounds(x, y, iNormal.getIconWidth(), iNormal.getIconHeight());
	    return btn;
	}
	
	/**
	 * This method design JButton when just having normal and pressed imgs. 
	 * @return designed JButton.
	 * */
	
	public static JButton design(String normal, String pressed, int x, int y) {
		URL normalUrl = DesignButton.class.getResource(normal);
	    URL pressedUrl = DesignButton.class.getResource(pressed);
	    
	    ImageIcon iNormal = new ImageIcon(normalUrl);
	    ImageIcon iPressed = new ImageIcon(pressedUrl);

	    JButton btn = new JButton(iNormal);
	    btn.setPressedIcon(iPressed);

	    btn.setBorderPainted(false);
	    btn.setContentAreaFilled(false);
	    btn.setFocusPainted(false);
	    btn.setOpaque(false);
	    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    btn.setRolloverEnabled(true);

	    btn.setPreferredSize(new Dimension(iNormal.getIconWidth(), iNormal.getIconHeight()));
	    
	    btn.setBounds(x, y, iNormal.getIconWidth(), iNormal.getIconHeight());
	    return btn;
	}
	
}
