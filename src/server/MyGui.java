package server;

import java.awt.Font;

public class MyGui {
    private MyPacmanGame _game;

    public MyGui(MyPacmanGame game) {
        this._game = game;

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, _game.getGameBoard().length);
        StdDraw.setYscale(0, _game.getGameBoard()[0].length);
        StdDraw.enableDoubleBuffering();
    }

    public void draw() {
        StdDraw.clear(StdDraw.BLACK);

        int[][] board = _game.getGameBoard();
        int w = board.length;
        int h = board[0].length;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = board[x][y];
                if (pixel == 1) {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.filledSquare(x + 0.5, y + 0.5, 0.45);
                } else if (pixel == 2) {
                    StdDraw.setPenColor(StdDraw.PINK);
                    StdDraw.filledCircle(x + 0.5, y + 0.5, 0.1);
                } else if (pixel == 11) {
                    StdDraw.setPenColor(StdDraw.GREEN);
                    StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
                }
            }
        }

        _game.getPacmanObj().draw();

        for (IGhost g : _game.getGhostsList()) g.draw();

        if (!_game.isRunning()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setFont(new Font("Arial", Font.BOLD, 48));

            if (_game.isVictory()) {
                StdDraw.text(w / 2.0, h / 2.0, "VICTORY!");
            } else {
                StdDraw.text(w / 2.0, h / 2.0, "GAME OVER!");
            }
        }

        StdDraw.show();
    }
}
