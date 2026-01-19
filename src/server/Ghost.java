package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost implements IGhost {

    private Pixel2D _pos;
    private int _id;
    private double _edibleTimer = 0;
    private int _status = PLAY;
    private Random rand = new Random();

    public Ghost(Pixel2D startPos, int id) {
        this._pos = new Index2D(startPos);
        this._id = id % 4;
    }

    @Override
    public Pixel2D getPos() { return _pos; }

    @Override
    public int getType() { return _id; }

    @Override
    public void nextMove(Map2D map, Pixel2D pacmanPos) {
        List<Integer> dirs = legalDirs(map);
        if (dirs.isEmpty()) return;
        int dir = dirs.get(rand.nextInt(dirs.size()));
        move(dir);
    }

    private List<Integer> legalDirs(Map2D map) {
        List<Integer> ans = new ArrayList<>();
        for (int d = 0; d < 4; d++) {
            Pixel2D next = nextPixel(d);
            if (map.isInside(next) && map.getPixel(next.getX(), next.getY()) != 1) {
                ans.add(d);
            }
        }
        return ans;
    }

    private Pixel2D nextPixel(int dir) {
        int x = _pos.getX();
        int y = _pos.getY();
        switch (dir) {
            case 0: y++; break;
            case 1: x--; break;
            case 2: y--; break;
            case 3: x++; break;
        }
        return new Index2D(x, y);
    }

    private void move(int dir) { _pos = nextPixel(dir); }

    @Override
    public double remainTimeAsEatable(int tick) { return _edibleTimer; }

    @Override
    public void setEatableTimer(double time) {
        _edibleTimer = Math.max(_edibleTimer, time);
    }

    public void updateTimer(double delta) {
        _edibleTimer = Math.max(0, _edibleTimer - delta);
    }

    @Override
    public boolean isEdible() { return _edibleTimer > 0; }

    @Override
    public int getStatus() { return _status; }

    @Override
    public void draw() {
        if (isEdible()) {
            StdDraw.setPenColor(StdDraw.CYAN);
            StdDraw.filledCircle(_pos.getX() + 0.5, _pos.getY() + 0.5, 0.4);
        } else {
            String img = "src/images/g" + _id + ".png";
            StdDraw.picture(_pos.getX() + 0.5, _pos.getY() + 0.5, img, 1, 1);
        }
    }
}
