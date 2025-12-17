package com.hust.tetris.pieces;
public class Piece {
    protected int[][] shape;
    private String urlImage;

    // Position in board
    protected int x;
    protected int y;

    //protected int tileSize = 24;

    public Piece(String urlImage, int x, int y) {
        this.urlImage = urlImage;
        this.x = x;
        this.y = y;
    }

    public int[][] getShape() {
        return shape;
    }

    public String getURLImage() {
        return urlImage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void movePiece(int dx, int dy) {
        x += dx;
        y += dy;       
    }

    public void rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated[c][rows - 1 - r] = shape[r][c];
            }
        }
        shape = rotated;
    }
}
