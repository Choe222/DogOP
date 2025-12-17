package com.hust.tetris;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.hust.tetris.TetrisGame.TickResult;
import com.hust.tetris.design.DesignButton;
import com.hust.tetris.pieces.Piece;

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

		reset = DesignButton.design("./resetButton.png", "./resetButtonHover.png", "./resetButtonPressed.png", 458, 443);
		pause = DesignButton.design("./pauseButton.png", "./pauseButtonHover.png", "./pauseButtonPressed.png", 437, 443);
		add(reset);
		add(pause);
		setPreferredSize(new Dimension(x, boardHeightPx));
		setBackground(Color.BLACK);
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
			background = new ImageIcon(getClass().getResource("./gameFirst.png")).getImage();
		} else if (t == 1) {
			background = new ImageIcon(getClass().getResource("./gameTwo.png")).getImage();
		} else if (t == 2) {
			background = new ImageIcon(getClass().getResource("./gameThree.png")).getImage();
		} else {
			background = new ImageIcon(getClass().getResource("./gameStart.png")).getImage();
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

			if (temp.equals("./cyan.png")) {
				n = cyan;
			} else if (temp.equals("./red.png")) {
				n = red;
			} else if (temp.equals("./green.png")) {
				n = green;
			} else if (temp.equals("./purple.png")) {
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
		g.setFont(new Font("Dialog", Font.BOLD, 24));

		String scoreText = "" + game.getScore().getValue();
		g.drawString(scoreText, scoreBoxX + 2, scoreBoxY + 32);
		g.setColor(new Color(56, 142, 60));
		if (game.getInScore().getValue() > 0) {
			String inScore = "+" + game.getInScore().getValue();
			g.setFont(new Font("Dialog", Font.BOLD, 24));
			g.drawString(inScore, scoreBoxX + 2, scoreBoxY - 8);
		}

		if (game.getPowScore().getValue() >= 2) {
			String pow = "x" + game.getPowScore().getValue();
			g.setColor(new Color(56, 142, 60));
			g.setFont(new Font("Dialog", Font.BOLD, 16));
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
			g.setColor(new Color(0, 0, 0, 180));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 28));
			String m = "GAME OVER";
			String n = "Score: " + game.getScore().getValue();

			int msgWidth = g.getFontMetrics().stringWidth(m);
			int scoreWidth = g.getFontMetrics().stringWidth(n);

			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;

			g.drawString(m, centerX - msgWidth / 2, centerY - 10);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString(n, centerX - scoreWidth / 2, centerY + 20);
		} else if (game.isPaused()) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 26));
			String msg = "PAUSED";
			int msgWidth = g.getFontMetrics().stringWidth(msg);
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			g.drawString(msg, centerX - msgWidth / 2, centerY);
		}
	}

}
