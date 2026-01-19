package server;

public interface IPacman {
    Pixel2D getPos();
    void setDirection(int dir);
    void move(Map2D map);
    void draw();
}
