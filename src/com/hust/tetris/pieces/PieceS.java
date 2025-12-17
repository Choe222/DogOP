package com.hust.tetris.pieces;
public class PieceS extends Piece {
	public PieceS(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {
	            {0, 1, 1},
	            {1, 1, 0}
	        };
	}
}