package server;

public interface IGhost {

    int INIT = 0;
    int PLAY = 1;
    int PAUSE = 2;

    int getType();
    Pixel2D getPos();

    /** movement logic (chase / flee / random) */
    void nextMove(Map2D map, Pixel2D pacmanPos);

    /** how much time the ghost is still eatable */
    double remainTimeAsEatable(int tick);

    void setEatableTimer(double time);

    boolean isEdible();

    int getStatus();

    void draw();
}
