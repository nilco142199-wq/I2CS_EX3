package server;

public class Pacman implements IPacman {
    private Pixel2D _pos;
    private int _dir = -1;

    // קבועים לכיוונים
    private static final int UP = 0;
    private static final int LEFT = 1;
    private static final int DOWN = 2;
    private static final int RIGHT = 3;

    public Pacman(Index2D startPos) {
        _pos = new Index2D(startPos.getX(), startPos.getY());
    }

    @Override
    public Pixel2D getPos() {
        return _pos;
    }

    @Override
    public void setDirection(int dir) {
        _dir = dir;
    }

    @Override
    public void move(Map2D map) {
        if (_dir < 0) return;

        int x = _pos.getX();
        int y = _pos.getY();
        int nx = x, ny = y;

        if (_dir == UP) ny += 1;
        else if (_dir == DOWN) ny -= 1;
        else if (_dir == LEFT) nx -= 1;
        else if (_dir == RIGHT) nx += 1;
        if (nx >= 0 && nx < map.getWidth() && ny >= 0 && ny < map.getHeight()
                && map.getPixel(nx, ny) != 1) {
            _pos = new Index2D(nx, ny);
        }
    }

    @Override
    public void draw() {
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.filledCircle(_pos.getX() + 0.5, _pos.getY() + 0.5, 0.4);
    }
}
