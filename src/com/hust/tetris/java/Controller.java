package com.hust.tetris.java;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class Controller implements KeyListener {

	private TetrisGame game;
	private GamePanel viewGame;
	private MenuPanel viewMenu;
	private TetrisFrame frame;
	
	private Timer gameLoop;		// loop for the piece
	private Timer scoreLoop;	// loop for the score
	private Timer timeLoop;		// loop for the time left to get more score
	private Timer nextLoop;		// loop for nhấp nháy next piece và flow of the liqid in the pipe.
	
	public static final int SCORE_STEP = 15;	// 1 frame increased 15 points if the inScore >= 15  
	public static final int SCORE_DELAY_MS = 16;	//delay for the frame 
	public static final int NEXT_PIECE_DELAY_MS = 100;
	public static final int TIME_DELAY_MS = 100;

	private int baseDelayMs = 500;
	public static final int MIN_GAME_DELAY_MS = 302;

	public Controller(GamePanel gamePanel, MenuPanel menuPanel, TetrisFrame frame) {
		this.game = gamePanel.getGame();
		this.viewGame = gamePanel;
		this.viewMenu = menuPanel;
		this.frame = frame;

		//add Action cho button của menu
		viewMenu.getLevelO().addActionListener(e -> this.game.setLevel(0));
		viewMenu.getLevelTo().addActionListener(e -> this.game.setLevel(1));
		viewMenu.getLevelTe().addActionListener(e -> this.game.setLevel(2));
		viewMenu.getLevelFo().addActionListener(e -> this.game.setLevel(3));
		viewMenu.getLevelFi().addActionListener(e -> this.game.setLevel(4));
		viewMenu.getStart().addActionListener(e -> this.start(game.getLevel()));

		//add acion cho game
		viewGame.getReset().addActionListener(e -> this.start());
		viewGame.getPause().addActionListener(e -> this.togglePause());
		viewGame.addKeyListener(this);
		viewGame.setFocusable(true);
		
		//start gameloop
		timeLoop = new Timer(TIME_DELAY_MS, e -> onTimeTick());
		scoreLoop = new Timer(SCORE_DELAY_MS, e -> onScoreTick());
		gameLoop = new Timer(baseDelayMs, e -> onTick());
		nextLoop = new Timer(NEXT_PIECE_DELAY_MS, e -> onNextTick());
	}

	// default start
	public void start() {
		start(0);
	}

	public void start(int initLevel) {
		frame.showCard("GAME_PANEL");
		game.getStartTick().setValue(0);
		gameLoop.stop();
		game.reset(initLevel);

		gameLoop.setDelay(calculateSpeed(game.getLevel()));
		gameLoop.start();

		scoreLoop.stop();
		scoreLoop.start();

		timeLoop.stop();
		timeLoop.start();

		nextLoop.stop();
		nextLoop.start();

		viewGame.requestFocusInWindow();
		viewGame.getReset().setVisible(true);
		viewGame.getPause().setVisible(true);
		viewGame.repaint();
	}

	// update cho timeloop
	private void onTimeTick() {
		if (game.isGameOver()) {
			timeLoop.stop();
			return;
		}
		if (game.getStartTick().getValue() == 3) {
			if (game.getTime().getValue() > 0) {
				game.getTime().decrease(1);
			} else {
				game.getPowScore().setValue(0);
			}
		}

		viewGame.repaint();
	}

	//update cho nextloop
	private void onNextTick() {
		if (game.isGameOver()) {
			nextLoop.stop();
			return;
		}
		if (game.getStartTick().getValue() == 3) {
			game.getTickNextPiece().increase(1);
		}
		viewGame.repaint();
	}

	//update for scoreLoop
	private void onScoreTick() {
		if (game.isGameOver()) {
			scoreLoop.stop();
			return;
		}

		if (game.getStartTick().getValue() == 3) {
			int inScore = game.getInScore().getValue();
			if (inScore <= 0)
				return;

			int delta = inScore >= SCORE_STEP ? SCORE_STEP : inScore;
			game.getScore().increase(delta);
			game.getInScore().decrease(delta);
		}
		viewGame.repaint();
	}

	//update for the gameloop
	void onTick() {
		if (game.getStartTick().getValue() == 3) {
			TetrisGame.TickResult result = game.tick();
			if (result == TetrisGame.TickResult.GAME_OVER) {
				gameLoop.stop();
				scoreLoop.stop();
				timeLoop.stop();
				nextLoop.stop();
			} else if (result == TetrisGame.TickResult.LEVEL_UP) {
				gameLoop.setDelay(calculateSpeed(game.getLevel()));
			}
		}
		if (game.getStartTick().getValue() < 3)
			game.getStartTick().increase(1);
		viewGame.repaint();
	}

	//return menu if want
	private void returnMenu() {
		gameLoop.stop();
		scoreLoop.stop();
		timeLoop.stop();
		nextLoop.stop();
		frame.showCard("MENU_PANEL");
		viewMenu.requestFocusInWindow();
	}

	// update game speed by level
	private int calculateSpeed(int level) {
		int newDelay = baseDelayMs - level * 25;
		if (newDelay < MIN_GAME_DELAY_MS) {
			newDelay = MIN_GAME_DELAY_MS;
		}
		return newDelay;
	}

	
	//pause
	public void togglePause() {
		game.reversePause();
		if (game.isPaused()) {
			if (gameLoop != null)
				gameLoop.stop();
			if (scoreLoop != null)
				scoreLoop.stop();
			if (timeLoop != null)
				timeLoop.stop();
			if (nextLoop != null)
				nextLoop.stop();
		} else {
			viewGame.requestFocusInWindow();
			if (gameLoop != null)
				gameLoop.start();
			if (scoreLoop != null)
				scoreLoop.start();
			if (timeLoop != null)
				timeLoop.start();
			if (nextLoop != null)
				nextLoop.start();
		}
		if (viewGame != null)
			viewGame.repaint();
	}

	public void onLeftPressed() {
		if (game.moveLeft())
			viewGame.repaint();
	}

	public void onRightPressed() {
		if (game.moveRight())
			viewGame.repaint();
	}

	public void onDownPressed() {
		if (game.softDrop())
			viewGame.repaint();
	}

	public void onSpacePressed() {
		int before = game.getLevel();
		if (!game.hardDrop())
			return;
		if (game.isGameOver()) {
			gameLoop.stop();
		} else {
			int after = game.getLevel();
			if (after != before) {
				gameLoop.setDelay(calculateSpeed(after));
			}
		}
		viewGame.repaint();
	}

	public void onRotatePressed() {
		if (game.rotateCurrentPiece())
			viewGame.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (game.getStartTick().getValue() == 3) {
			int key = e.getKeyCode();

			switch (key) {
			case KeyEvent.VK_LEFT:
				onLeftPressed();
				break;
			case KeyEvent.VK_RIGHT:
				onRightPressed();
				break;
			case KeyEvent.VK_DOWN:
				onDownPressed();
				break;
			case KeyEvent.VK_UP:
				onRotatePressed();
				break;
			case KeyEvent.VK_SPACE:
				onSpacePressed();
				break;
			case KeyEvent.VK_P:
				togglePause();
				break;
			case KeyEvent.VK_R:
				start();
				break;
			case KeyEvent.VK_M:
				returnMenu();
				break;

			}
		}
		if (viewGame != null) {
			viewGame.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
