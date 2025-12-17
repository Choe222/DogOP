package com.hust.tetris.pieces;
public class PieceZ extends Piece {
	public PieceZ(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {
	            {1, 1, 0},
	            {0, 1, 1}
	        };
	}
}