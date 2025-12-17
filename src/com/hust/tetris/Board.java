package com.hust.tetris;

import com.hust.tetris.pieces.Piece;
import com.hust.utils.MyInteger;

/**
 * This class handles the colllision, locking the piece and calculating the score.
 */
public class Board {
    private int rows;
    private int cols;
    private String[][] boardImage;
    private int tileSize = 24;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.boardImage = new String[rows][cols];
    }

    public int getRowSize() {
        return rows;
    }

    public int getColSize() {
        return cols;
    }

    public int getTileSize() {
        return tileSize;
    }

    public String[][] getBoardImage() {
        return boardImage;
    }

    public void clear() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boardImage[r][c] = null;
            }
        }
    }

    /**
     * This method can be called to check the movement of piece.
     * Check the collision by the coordinate in the board map (not coordinate by pixel).
     * @return True if piece can move and otherwise.
     */
    public boolean canMove(Piece p, int dx, int dy) {
        int[][] shape = p.getShape();
        int baseCol = p.getX() + dx;
        int baseRow = p.getY() + dy;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 1) continue;

                int col = baseCol + c;
                int row = baseRow + r;

                if (col < 0 || col >= cols || row >= rows) {
                    return false;
                }

                if (row < 0) {
                    continue;
                }

                if (boardImage[row][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method can be called to check rotation of the piece.
     * Check the collision by the coordinate in the board map.
     * @param Piece p: the piece will be check for rotating.
     * @param MyInteger alpha: alpha is how many step the Piece p will move to it can rotate without collision with wall.
     * @return True if piece can rotate and otherwise.
     */

    public boolean canRotate(Piece p, MyInteger alpha) {	
    	int[][] shape = p.getShape();

        int rowsS = shape.length;
        int colsS = shape[0].length;

        int[][] rotated = new int[colsS][rowsS];
        for (int r = 0; r < rowsS; r++) {
            for (int c = 0; c < colsS; c++) {
                rotated[c][rowsS - 1 - r] = shape[r][c];
            }
        }

        int baseCol = p.getX();
        int baseRow = p.getY();
        int dRight = 11;
        int dLeft = -11;
        
        for (int r = 0; r < rotated.length; r++) {
            for (int c = 0; c < rotated[r].length; c++) {
                if (rotated[r][c] != 1) continue;

                int col = baseCol + c;
                int row = baseRow + r;

                if (-col > 0) {
                	if(dLeft < -col) 
                		dLeft = -col;
                	continue;
                }
                if (col - cols >= 0) {
                	if(dRight > cols - col - 1)
                		dRight = cols - col - 1;
                	continue;
                }
                if(row >= rows) {
                	return false;
                }

                if (boardImage[row][col] != null) {
                	return false;
                }
            }
        }
        
        int temp = dRight != 11 ? dRight : dLeft != -11 ? dLeft : 0;
        
        if(temp != 0) {
            for (int r = 0; r < rotated.length; r++) {
                for (int c = 0; c < rotated[r].length; c++) {
                    if (rotated[r][c] != 1) continue;

                    int col = baseCol + c + temp;
                    int row = baseRow + r;
                    if (boardImage[row][col] != null || row >= rows) {
                    	return false;
                    }
                }
            }
        }
        alpha.setValue(temp);
        
        return true;
    }
    
    /**
     * This method can be called to check locking the piece and update the increased score achieved, the nums of lines, timeIncrease(the time left for keeping the pow Score).
     * @return True if can lock and otherwise. 
     */
    public boolean lockPiece(Piece p, MyInteger InScore, MyInteger lines, MyInteger timeIncrease, MyInteger powScore) {
        int[][] shape = p.getShape();
        int baseCol = p.getX();
        int baseRow = p.getY();

        boolean touchedTop = false;

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 1) continue;

                int col = baseCol + c;
                int row = baseRow + r;

                if (row < 0) {
                    touchedTop = true;
                    continue;
                }
                if (row >= rows || col < 0 || col >= cols) {
                    touchedTop = true;
                    continue;
                }

                boardImage[row][col] = p.getURLImage();
            }
        }

        int linesCleared = 0;
        for (int r = rows - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < cols; c++) {
                if (boardImage[r][c] == null) {
                    full = false;
                    break;
                }
            }

            if (full) {
                linesCleared++;

                for (int rr = r; rr > 0; rr--) {
                    for (int c = 0; c < cols; c++) {
                        boardImage[rr][c] = boardImage[rr - 1][c];
                    }
                }
                for (int c = 0; c < cols; c++) {
                    boardImage[0][c] = null;
                }
                r++; 
                if (linesCleared == 4) {
                    break;
                }
            }
        }
        
        boolean changeTime = false;
        
        if (linesCleared > 0) {
        	if(changeTime == false) {
        		changeTime = true;
            	powScore.increase(1);
        		timeIncrease.setValue(30);
        	}
        	lines.setValue(lines.getValue()+linesCleared);
        	InScore.setValue(InScore.getValue() + linesCleared * 203 * powScore.getValue());
        }

        return touchedTop;
    }
    
}
