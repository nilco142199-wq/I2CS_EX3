import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
    private int[][] _map;
    private boolean _cyclicFlag = true;

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

    /**
     * Initializes a w*h map with all cells set to value v.
     */
    @Override
    public void init(int w, int h, int v) {
        this._map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                this._map[x][y] = v;
            }
        }

    }

    /**
     * Initializes map from a 2D array copy.
     */
    @Override
    public void init(int[][] arr) {
        this._map = arr;
        if (_map == null) {
            throw new RuntimeException("Array is Null");
        }
        int w = arr.length;
        int h = arr[0].length;
        this._map = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                this._map[x][y] = arr[x][y];
            }
        }
    }

    /**
     * Returns a deep copy of the map.
     */
    @Override
    public int[][] getMap() {
        int w = this.getWidth();
        int h = this.getHeight();
        int[][] ans = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                ans[x][y] = this._map[x][y];
            }
        }
        return ans;
    }

    /**
     * Returns map width.
     */
    public int getWidth() {
        return this._map.length;
    }

    /**
     * Returns map height.
     */
    @Override
    public int getHeight() {
        return this._map[0].length;
    }


    @Override
    /** Returns pixel value at (x, y), or -1 if out of bounds. */
    public int getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            return -1;
        }

        return this._map[x][y];
    }

    /**
     * Returns pixel value at Pixel2D object.
     */
    @Override
    /////// add your code below ///////
    public int getPixel(Pixel2D p) {
        if (p == null) {
            return 0;
        }
        return this.getPixel(p.getX(), p.getY());
    }

    @Override
    /** Sets the value of a pixel at (x, y). Throws exception if out of bounds. */
    public void setPixel(int x, int y, int v) {
        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            throw new IndexOutOfBoundsException("The pixel is out of bound");
        }
        this._map[x][y] = v;
    }

    @Override
    /** Sets the value of a pixel at Pixel2D object. */
    public void setPixel(Pixel2D p, int v) {
        if (p == null) {
            return;
        }
        this.setPixel(p.getX(), p.getY(), v);
    }

    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/Flood_fill
     */
    public int fill(Pixel2D xy, int new_v) {
        if (xy == null) return 0;

        int oldColor = getPixel(xy);
        if (oldColor == new_v) return 0;

        int w = getWidth();
        int h = getHeight();
        int countFill = 0;

        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.add(new int[]{xy.getX(), xy.getY()});

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0];
            int y = curr[1];

            if (x < 0 || x >= w || y < 0 || y >= h) continue;
            if (getPixel(x, y) != oldColor) continue;

            setPixel(x, y, new_v);
            countFill++;

            queue.add(new int[]{x + 1, y});
            queue.add(new int[]{x - 1, y});
            queue.add(new int[]{x, y + 1});
            queue.add(new int[]{x, y - 1});
        }

        return countFill;
    }

    @Override
    /**
     * Compute the shortest valid path between p1 and p2.
     * A valid path is a path that does not pass through pixels with the obstacle color.
     * The path is composed of consecutive neighboring pixels (up, down, left, right).
     * If this map is cyclic, the path may wrap around the map edges.
     * If no valid path exists between p1 and p2, this method returns null.
     *
     * @param p1 the start pixel.
     * @param p2 the destination pixel.
     * @param obsColor the color representing an obstacle.
     * @return the shortest path as an array of consecutive pixels, or null if no path exists.
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        if (p1 == null || p2 == null) return null;

        int w = getWidth();
        int h = getHeight();

        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null;

        boolean[][] visited = new boolean[w][h];
        Pixel2D[][] parent = new Pixel2D[w][h];

        java.util.Queue<Pixel2D> queue = new java.util.LinkedList<>();
        queue.add(p1);
        visited[p1.getX()][p1.getY()] = true;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            Pixel2D p = queue.poll();
            int x = p.getX();
            int y = p.getY();

            if (x == p2.getX() && y == p2.getY()) {
                java.util.List<Pixel2D> path = new java.util.ArrayList<>();
                Pixel2D cur = p2;
                while (cur != null) {
                    path.add(0, cur);
                    cur = parent[cur.getX()][cur.getY()];
                }
                return path.toArray(new Pixel2D[0]);
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isCyclic()) {
                    nx = (nx + w) % w;
                    ny = (ny + h) % h;
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                }

                if (visited[nx][ny]) continue;
                if (getPixel(nx, ny) == obsColor) continue;

                visited[nx][ny] = true;
                parent[nx][ny] = p;
                queue.add(new Index2D(nx, ny));
            }
        }
        return null;
    }

    /**
     * Checks if a Pixel2D object is inside the map bounds.
     */
    @Override
    public boolean isInside(Pixel2D p) {
        if (p == null) {
            return false;
        }
        int x = p.getX();
        int y = p.getY();
        if (x < 0 || y < 0 || x >= this.getWidth() || y >= this.getHeight()) {
            return false;
        }
        return true;
    }

    @Override
/**
 * @return true iff this map is defined as cyclic.
 */
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        _cyclicFlag = cy;
    }

    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        int w = getWidth();
        int h = getHeight();
        Map2D distanceMap = new Map(w, h, -1);

        if (start == null || getPixel(start) == obsColor) return distanceMap;

        boolean[][] visited = new boolean[w][h];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{start.getX(), start.getY()});
        distanceMap.setPixel(start, 0);
        visited[start.getX()][start.getY()] = true;

        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};

        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int x = p[0];
            int y = p[1];
            int curDist = distanceMap.getPixel(x, y);

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];

                if (isCyclic()) {
                    nx = (nx + w) % w;
                    ny = (ny + h) % h;
                } else {
                    if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                }

                if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
                    visited[nx][ny] = true;
                    distanceMap.setPixel(nx, ny, curDist + 1);
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return distanceMap;
    }
}