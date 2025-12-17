package com.hust.tetris.pieces;
public class PieceO extends Piece {
	public PieceO(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {{1, 1},{1, 1}};
	}
}
