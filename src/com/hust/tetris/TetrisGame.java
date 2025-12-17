package com.hust.tetris;

import java.util.ArrayList;
import java.util.Random;

import com.hust.tetris.pieces.*;
import com.hust.utils.MyInteger;

/**
 * Game logic controller: holds Board, current/next piece, score.
 */

public class TetrisGame {
	
	private Board board;
	public Piece currentPiece;
	private Piece nextPiece;
	
	private MyInteger inScore;
	private MyInteger score;
	private MyInteger lines;
	private MyInteger powScore;
	
	private MyInteger timeIncrease;		//for drawing the time left the score can get more than 1x
	private MyInteger tickNextPiece;	//for nhap nhay cai hinh next Piece
	private MyInteger startTick;		//for displaying the den giao thong at first
	
	private Random rand = new Random();		//Random for color and piece.
	private char[] tetrominoTypes = { 'O', 'I', 'S', 'Z', 'T', 'L', 'J' };
	private String[] urlImages = { "./red.png", "./orange.png", "./cyan.png", "./green.png", "./purple.png" };

	private int spawnX;		//postion of new piece
	private int spawnY;

	private int linePerSpeedup = 2;		//how many line will be update the next level
	private int nextSpeedLine = 2;		//next lines for updating level = nextSpeedLine + linePerSpeedup
	private int level = 0;

	private boolean gameOver = false;
	private boolean paused = false;

	public TetrisGame() {
		this.board = new Board(20, 11);		// Board 20x11
		
		this.score = new MyInteger(0);
		this.lines = new MyInteger(0);
		this.inScore = new MyInteger(0);
		this.timeIncrease = new MyInteger(0);
		this.powScore = new MyInteger(0);
		this.tickNextPiece = new MyInteger(0);
		this.startTick = new MyInteger(0);
		
		this.spawnX = 5;
		this.spawnY = 0;
	}

	public MyInteger getStartTick() {
		return this.startTick;
	}

	public MyInteger getPowScore() {
		return this.powScore;
	}

	public MyInteger getTickNextPiece() {
		return tickNextPiece;
	}

	public Random getRand() {
		return rand;
	}

	public MyInteger getTimeIncrease() {
		return timeIncrease;
	}

	public char[] getTetrominoTypes() {
		return tetrominoTypes;
	}

	public String[] getUrlImages() {
		return urlImages;
	}

	public int getSpawnX() {
		return spawnX;
	}

	public int getSpawnY() {
		return spawnY;
	}

	public int getLinePerSpeedup() {
		return linePerSpeedup;
	}

	public int getNextSpeedLine() {
		return nextSpeedLine;
	}

	public MyInteger getTime() {
		return this.timeIncrease;
	}

	public MyInteger getInScore() {
		return this.inScore;
	}

	public int getLines() {
		return lines.getValue();
	}

	public Board getBoard() {
		return board;
	}

	public Piece getCurrentPiece() {
		return currentPiece;
	}

	public Piece getNextPiece() {
		return nextPiece;
	}

	public MyInteger getScore() {
		return score;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setStart() {
		gameOver = false;
	}

	public void reversePause() {
		paused = !paused;
	}

	public void setPause() {
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	// for the Controller
	public enum TickResult {
		NO_CHANGE, CHANGE, LEVEL_UP, GAME_OVER
	}

	/**
	 * Method này dùng để điều hướng cho controller hoạt động. 
	 * Nếu gameover thì dừng gameloop, còn không thì kiểm tra xem có rơi được piece nữa không và check levelup.
	 * */
	
	public TickResult tick() {
		
		if (this.startTick.getValue() == 3) {		//For den giao thong at first
			if (gameOver)
				return TickResult.GAME_OVER;
			if (board.canMove(currentPiece, 0, 1)) {
				currentPiece.movePiece(0, 1);
			} else {
				boolean canLock = board.lockPiece(currentPiece, inScore, lines, timeIncrease, powScore);
				boolean leveledUp = checkSpeedUp();
				if (canLock) {
					gameOver = true;
					return TickResult.GAME_OVER;
				} else {
					spawnNewPiece();
					tickNextPiece.setValue(0);
				}
				if (leveledUp) {
					return TickResult.LEVEL_UP;
				}
			}
		}

		return TickResult.NO_CHANGE;	// return to update gameloop

	}

	/**
	 * Tạo ra một khối ngẫu nhiên
	 * @return Piece
	 * */
	
	public Piece createRandomPiece() {
		int typeIndex = rand.nextInt(tetrominoTypes.length);
		char type = tetrominoTypes[typeIndex];

		int urlImgIndex = rand.nextInt(urlImages.length);
		String urlImg = urlImages[urlImgIndex];

		if (type == 'O') {
			return new PieceO(urlImg, spawnX, spawnY);
		} else if (type == 'I') {
			return new PieceI(urlImg, spawnX, spawnY);
		} else if (type == 'S') {
			return new PieceS(urlImg, spawnX, spawnY);
		} else if (type == 'Z') {
			return new PieceZ(urlImg, spawnX, spawnY);
		} else if (type == 'T') {
			return new PieceT(urlImg, spawnX, spawnY);
		} else if (type == 'L') {
			return new PieceL(urlImg, spawnX, spawnY);
		} else if (type == 'J') {
			return new PieceJ(urlImg, spawnX, spawnY);
		}
		return new PieceO(urlImg, spawnX, spawnY);
	}

	/**
	 * Spawn new currentPiece and check the gameOver if the new spawned piece is collision
	 * 
	 * */
	
	public void spawnNewPiece() {
		if (nextPiece == null) {
			nextPiece = createRandomPiece();
		}
		currentPiece = nextPiece;
		currentPiece.setPosition(spawnX, spawnY);
		nextPiece = createRandomPiece();

		if (!board.canMove(currentPiece, 0, 0)) {	//Đứng yên còn collision thì cúc
			gameOver = true;
		}
	}

	/**
	 * Check speedup to update level
	 * */
	
	public boolean checkSpeedUp() {
		int currentLine = lines.getValue();
		boolean up = false;
		while (currentLine >= nextSpeedLine) {
			level = level + 1;
			nextSpeedLine += linePerSpeedup;
			up = true;
		}
		return up;
	}

	/**
	 * move the piece to the left
	 * @return true if the piece can move and otherwise
	 * */
	
	public boolean moveLeft() {
		if (gameOver || paused)
			return false;

		if (board.canMove(currentPiece, -1, 0)) {
			currentPiece.movePiece(-1, 0);
			return true;
		}
		return false;
	}

	/**
	 * move the piece to the Right
	 * @return true if the piece can move and otherwise
	 * */
	
	public boolean moveRight() {
		if (gameOver || paused)
			return false;

		if (board.canMove(currentPiece, 1, 0)) {
			currentPiece.movePiece(1, 0);
			return true;
		}
		return false;
	}

	/**
	 * make the piece move down more quickly
	 * @return true if the piece can move and otherwise
	 * */
	
	public boolean softDrop() {
		if (gameOver || paused)
			return false;

		if (board.canMove(currentPiece, 0, 1)) {
			currentPiece.movePiece(0, 1);
			return true;
		}
		return false;
	}

	/**
	 * make the piece lock. The tickNextPiece will reset to the zero for the next Piece. 
	 * @return true if the piece can move and otherwise
	 * */
	
	public boolean hardDrop() {
		if (gameOver || paused)
			return false;

		while (board.canMove(currentPiece, 0, 1)) {
			currentPiece.movePiece(0, 1);
		}
		boolean touchedTop = board.lockPiece(currentPiece, inScore, lines, timeIncrease, powScore);
		checkSpeedUp();
		if (touchedTop) {
			gameOver = true;
		} else {
			spawnNewPiece();
			tickNextPiece.setValue(0);
		}

		return true;
	}

	/**
	 * Rotate piece
	 * @return true if the piece can move and otherwise
	 * */
	
	public boolean rotateCurrentPiece() {
		if (gameOver || paused)
			return false;
		MyInteger alpha = new MyInteger(0);
		boolean result = board.canRotate(currentPiece, alpha);
		if (result) {
			if (alpha.getValue() != 0) {
				currentPiece.movePiece(alpha.getValue(), 0);
			}
			currentPiece.rotate();
			return true;
		}
		return false;
	}

	/**
	 * Reset the game from the beginning
	 * */
	
	public void reset(int initLevel) {
		board.clear();
		score.setValue(0);
		lines.setValue(0);
		
		inScore.setValue(0);
		timeIncrease.setValue(0);
		tickNextPiece.setValue(0);
		
		level = initLevel;
		nextSpeedLine = linePerSpeedup;
		gameOver = false;
		paused = false;
		currentPiece = null;
		nextPiece = null;
		spawnNewPiece();

	}

}
