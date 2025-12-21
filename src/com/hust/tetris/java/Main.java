package com.hust.tetris.java;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisFrame::new);
    }
}
