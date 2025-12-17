package com.hust.tetris.pieces;
public class PieceL extends Piece {
	public PieceL(String urlImage, int x ,int y) {
		super(urlImage,x,y);
		this.shape = new int[][] {
			            {1, 0, 0},
			            {1, 1, 1}
			        };
	}
}