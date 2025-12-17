package com.hust.tetris.pieces;
public class PieceI extends Piece{
	public PieceI(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {{1, 1, 1, 1}};
	}
}
