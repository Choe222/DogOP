package com.hust.tetris.java;

import java.awt.*;
import javax.swing.*;


/**
 * Outer frame with GamePanel in center and control bar (Start, Level, Pause).
 */

public class TetrisFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel root;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    
    public TetrisFrame() {
        super("Tetris");
        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);
        
        gamePanel = new GamePanel();
        menuPanel = new MenuPanel(this);
        
        Controller controller = new Controller(gamePanel, menuPanel, this);

        root.add(gamePanel, "GAME_PANEL");
        root.add(menuPanel, "MENU_PANEL");
        setContentPane(root);  
        showCard("MENU_PANEL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }
    
    public void showCard(String name) {
        cardLayout.show(root, name);

        Component card = null;
        for (Component c : root.getComponents()) {
            if (c.isVisible()) {
            	card = c;
            	break;
            } 
        }

        if (card != null) {
            root.setPreferredSize(card.getPreferredSize());
            SwingUtilities.getWindowAncestor(root).pack(); 
            card.requestFocusInWindow();
        }
    }
    
}
