package com.hust.tetris.java.piece;
public class PieceJ extends Piece {
	public PieceJ(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {
			            {0, 0, 1},
			            {1, 1, 1}
			        };
	}
}