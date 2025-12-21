package com.hust.tetris.java;

import com.hust.tetris.java.resources.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;

import com.hust.tetris.java.design.DesignButton;
import com.hust.tetris.java.piece.Piece;
import com.hust.tetris.java.*;


public class GamePanel extends JPanel {
	private TetrisGame game;
	private int tileSize;
	private int sidePanelWidth = 232;
	private final Color cyan = new Color(2, 119, 189);
	private final Color red = new Color(211, 47, 47);
	private final Color orange = new Color(255, 143, 0);
	private final Color green = new Color(46, 125, 50);
	private final Color purple = new Color(186, 104, 200);
	private final Color gray = new Color(110, 128, 123);
	private Image background;
	private JButton reset;
	private JButton pause;
	Font TetrisPixel;
	Font Combo;
	//private JTextField name;
	
	public GamePanel() {
		setLayout(null);
		this.game = new TetrisGame();
		Board board = game.getBoard();
		this.tileSize = board.getTileSize();
		int rows = board.getRowSize(); // row&col of board
		int cols = board.getColSize();
		int boardWidthPx = cols * tileSize; // by Pixel
		int boardHeightPx = rows * tileSize;
		int x = boardWidthPx + sidePanelWidth;
		
		reset = DesignButton.design(ResourceManager.RESET_NORMAL, ResourceManager.RESET_HOVER, ResourceManager.RESET_PRESSED, 458, 443);
		pause = DesignButton.design(ResourceManager.PAUSE_NORMAL, ResourceManager.PAUSE_HOVER, ResourceManager.PAUSE_PRESSED, 437, 443);
	    try {
	    	InputStream is = getClass().getResourceAsStream(ResourceManager.TETRIS_FONT);
	    	this.TetrisPixel = Font.createFont(Font.TRUETYPE_FONT, is);
	    	is = getClass().getResourceAsStream(ResourceManager.COMBO_FONT);
	    	this.Combo = Font.createFont(Font.TRUETYPE_FONT, is);
	    } catch (FontFormatException | IOException exception) {
	        JOptionPane.showMessageDialog(null, exception.getMessage());
	    } 
	    
		//name = new JTextField(); 
		//name.setSize(boardWidthPx, boardHeightPx);
		
		add(reset);
		add(pause);
		
		//add(name);
		setPreferredSize(new Dimension(x, boardHeightPx));
		setBackground(Color.GRAY);
	}

	public JButton getReset() {
		return reset;
	}

	public JButton getPause() {
		return pause;
	}

	public TetrisGame getGame() {
		return game;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Board board = game.getBoard();
		Piece current = game.getCurrentPiece();
		Piece next = game.getNextPiece();

		int rows = board.getRowSize();
		int cols = board.getColSize();
		int boardHeightPx = rows * tileSize;

		int t = game.getStartTick().getValue();

		if (t == 0) {
			background = new ImageIcon(getClass().getResource(ResourceManager.GAME_FIRST)).getImage();
		} else if (t == 1) {
			background = new ImageIcon(getClass().getResource(ResourceManager.GAME_TWO)).getImage();
		} else if (t == 2) {
			background = new ImageIcon(getClass().getResource(ResourceManager.GAME_THREE)).getImage();
		} else {
			background = new ImageIcon(getClass().getResource(ResourceManager.GAME_START)).getImage();
		}

		g.drawImage(background, 0, 0, background.getWidth(this), boardHeightPx, this);

		String[][] boardImage = board.getBoardImage();

		// locked blocks
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String url = boardImage[r][c];
				if (url != null) {
					Image img = new ImageIcon(getClass().getResource(url)).getImage();
					g.drawImage(img, (c + 1) * tileSize, r * tileSize, tileSize, tileSize, null);
				}
			}
		}

		// ghost piece
		if (current != null && !game.isGameOver() && !game.isPaused() && game.getStartTick().getValue() == 3) {
			int oldX = current.getX();
			int oldY = current.getY();

			// drop until cannot move
			while (board.canMove(current, 0, 1)) {
				current.movePiece(0, 1);
			}

			int ghostX = current.getX();
			int ghostY = current.getY();
			current.setPosition(oldX, oldY); // return to the current coordination

			int[][] shape = current.getShape();
			int baseCol = ghostX;
			int baseRow = ghostY;

			for (int r = 0; r < shape.length; r++) {
				for (int c = 0; c < shape[r].length; c++) {
					if (shape[r][c] == 1) {
						int drawX = (baseCol + c + 1) * tileSize;
						int drawY = (baseRow + r) * tileSize;
						g.setColor(Color.WHITE);
						g.drawRect(drawX, drawY, tileSize, tileSize);
						g.setColor(new Color(160, 160, 160, 120));
						g.fillRect(drawX, drawY, tileSize, tileSize);
					}
				}
			}
		}

		// current piece
		if (current != null && game.getStartTick().getValue() == 3 && !game.isGameOver()) {
			int[][] shape = current.getShape();
			int baseCol = current.getX();
			int baseRow = current.getY();
			String url = current.getURLImage();
			Image img = new ImageIcon(getClass().getResource(url)).getImage();

			for (int r = 0; r < shape.length; r++) {
				for (int c = 0; c < shape[r].length; c++) {
					if (shape[r][c] == 1) {
						int drawX = (baseCol + c + 1) * tileSize;
						int drawY = (baseRow + r) * tileSize;
						g.drawImage(img, drawX, drawY, tileSize, tileSize, null);
					}
				}
			}
		}
		// next Piece & liquid in the pipe
		if (next != null && game.getStartTick().getValue() == 3 && !game.isGameOver()) {
			int[][] nShape = next.getShape();

			String temp = next.getURLImage();
			Color n = gray;

			if (temp.equals(ResourceManager.CYAN_PIECE)) {
				n = cyan;
			} else if (temp.equals(ResourceManager.RED_PIECE)) {
				n = red;
			} else if (temp.equals(ResourceManager.GREEN_PIECE)) {
				n = green;
			} else if (temp.equals(ResourceManager.PURPLE_PIECE)) {
				n = purple;
			} else {
				n = orange;
			}

			if (game.getTickNextPiece().getValue() < 16) {
				g.setColor(gray);
				g.fillRect(401, 160, 48, 17);
				g.setColor(n);
				g.fillRect(401 + (16 - game.getTickNextPiece().getValue()) * 3, 160,
						game.getTickNextPiece().getValue() * 3, 17);
			} else {
				g.setColor(n);
				g.fillRect(401, 160, 48, 17);
			}

			int pieceRows = nShape.length;
			int pieceCols = nShape[0].length;
			int pieceWidth = pieceCols * 18; // Set titleSize of nextPice = 18
			int pieceHeight = pieceRows * 18;

			int rectX = 365;
			int rectY = 24;
			int rectSize = 96;

			int startX = rectX + (rectSize - pieceWidth) / 2;
			int startY = rectY + (rectSize - pieceHeight) / 2;

			for (int r = 0; r < pieceRows; r++) {
				for (int c = 0; c < pieceCols; c++) {
					if (nShape[r][c] == 1) {
						int drawX = startX + c * 18;
						int drawY = startY + r * 18;
						if (game.getTickNextPiece().getValue() % 2 == 0) {
							g.setColor(Color.LIGHT_GRAY);
						} else {
							g.setColor(Color.GRAY);
						}
						g.fillRect(drawX, drawY, 18, 18);
					}
				}
			}
		}

		// score
		int scoreBoxX = 313;
		int scoreBoxY = 380;
		g.setColor(Color.BLACK);
		g.setFont(TetrisPixel.deriveFont(Font.PLAIN , 24F));

		String scoreText = "" + game.getScore().getValue();
		g.drawString(scoreText, scoreBoxX + 2, scoreBoxY + 32);
		g.setColor(new Color(56, 142, 60));
		if (game.getInScore().getValue() > 0) {
			String inScore = "+" + game.getInScore().getValue();
			g.setFont(TetrisPixel.deriveFont(Font.PLAIN , 32F));
			g.drawString(inScore, scoreBoxX + 2, scoreBoxY - 8);
		}

		if (game.getPowScore().getValue() >= 2) {
			String pow = "x" + game.getPowScore().getValue();
			g.setColor(new Color(56, 142, 60));
			g.setFont(Combo.deriveFont(Font.PLAIN , 36F)); /////////////////////////////////////////////////
			g.drawString(pow, 460, scoreBoxY - 8);
		}

		int currentTime = game.getTime().getValue();

		if (currentTime > 0) {
			if (currentTime < 30 && currentTime >= 20) {
				if (currentTime % 2 == 1) {
					g.setColor(new Color(174, 234, 0));
				} else {
					g.setColor(new Color(198, 255, 0));
				}
			} else if (currentTime < 20 && currentTime >= 15) {
				if (currentTime % 2 == 0) {
					g.setColor(new Color(198, 255, 0));
				} else {
					g.setColor(new Color(238, 255, 65));
				}
			} else if (currentTime < 15 && currentTime >= 10) {
				if (currentTime % 2 == 1) {
					g.setColor(new Color(238, 255, 65));
				} else {
					g.setColor(new Color(255, 241, 118));
				}
			} else {
				if (currentTime % 2 == 0) {
					g.setColor(new Color(255, 241, 118));
				} else {
					g.setColor(new Color(253, 216, 53));
				}
			}

			g.fillRect(330, 325, 150, 12);
			g.setColor(gray);
			g.fillRect(330 + currentTime * 5, 325, (30 - currentTime) * 5, 12);
		} else {
			g.setColor(gray);
			g.fillRect(330, 325, 150, 12);
		}

		// pause & game over
		if (game.isGameOver()) {
			
			this.reset.setVisible(false);
			this.pause.setVisible(false);
			
			g.setColor(new Color(0, 0, 0, 180));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.WHITE);
			g.setFont(TetrisPixel.deriveFont(Font.PLAIN , 28F));
			String m = "GAME OVER";
			String n = "Press R or M to continue";
			
			int msgWidth = g.getFontMetrics().stringWidth(m);

			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;

			g.drawString(m, centerX - msgWidth / 2, centerY - 10);
			g.setFont(TetrisPixel.deriveFont(Font.PLAIN , 18F));
			int continueWidth = g.getFontMetrics().stringWidth(n);
			g.drawString(n, centerX  - continueWidth / 2, centerY + 20);
		} else if (game.isPaused()) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.WHITE);
			g.setFont(TetrisPixel.deriveFont(Font.PLAIN , 26F));
			String msg = "PAUSED";
			int msgWidth = g.getFontMetrics().stringWidth(msg);
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			g.drawString(msg, centerX - msgWidth / 2, centerY);
		}
	}

}
