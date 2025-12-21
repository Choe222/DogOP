package com.hust.tetris.java.piece;
public class PieceT extends Piece {
	public PieceT(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {
			            {0, 1, 0},
			            {1, 1, 1}
			        };
	}
}