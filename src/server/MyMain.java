package server;

public class MyMain {
    public static void main(String[] args) {
        Map2D map = new Map(15, 15, 0);
        setupMap(map);

        MyPacmanGame game = new MyPacmanGame(map);

        MyGui gui = new MyGui(game);
        Ex3Algo algo = new Ex3Algo();

        while (true) {
            if (game.isRunning()) {
                int nextDir = algo.move(game);
                game.getPacmanObj().setDirection(nextDir);
                game.step();

                Pixel2D pacPos = game.getPacmanObj().getPos();
                int pixel = map.getPixel(pacPos);

                if (pixel == 2 || pixel == 11) {
                    map.setPixel(pacPos, 0);
                    if (pixel == 11) {
                        for (IGhost g : game.getGhostsList()) {
                            g.setEatableTimer(5.0);
                        }
                    }
                }
            }

            gui.draw();
            StdDraw.pause(100);
        }
    }
    private static void setupMap(Map2D map) {
        int w = map.getWidth();
        int h = map.getHeight();
        for (int i = 0; i < w; i++) {
            map.setPixel(i, 0, 1);
            map.setPixel(i, h - 1, 1);
            map.setPixel(0, i, 1);
            map.setPixel(w - 1, i, 1);
        }
        for (int i = 3; i <= 11; i++) {
            map.setPixel(i, 3, 1);
            map.setPixel(i, 11, 1);
        }
        for (int j = 5; j <= 9; j++) {
            map.setPixel(3, j, 1);
            map.setPixel(11, j, 1);
        }
        map.setPixel(4,4,11);
        map.setPixel(10,4,11);
        map.setPixel(4,10,11);
        map.setPixel(10,10,11);
        for (int x = 1; x < w - 1; x++) {
            for (int y = 1; y < h - 1; y++) {
                if (map.getPixel(x, y) == 0) {
                    map.setPixel(x, y, 2);
                }
            }
        }
    }
}
