import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;
import java.util.ArrayDeque;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
    private int _count;
    private int _lastDir = PacmanGame.ERR;
    private int _sameDirStreak = 0;

    private int[][] _visit;
    private int _vw = -1, _vh = -1;

    public Ex3Algo() {_count=0;}

    @Override
    /**
     *  Add a short description for the algorithm as a String.
     */
    public String getInfo() {
        return "Food first PacMan that is wall aware ghost avoidance (BFS) and with anti-loop visiting penalty.";
    }

    @Override
    /**
     * This ia the main method - that you should design, implement and test.
     */
    public int move(PacmanGame game) {
        int code = 0;
        int[][] board = game.getGame(code);
        int w = board.length;
        int h = board[0].length;
        if (_visit == null || _vw != w || _vh != h) {
            _visit = new int[w][h];
            _vw = w; _vh = h;
            _lastDir = PacmanGame.ERR;
            _sameDirStreak = 0;
        }

        int blue = Game.getIntColor(Color.BLUE, code);
        int pink = Game.getIntColor(Color.PINK, code);

        boolean cyclic = game.isCyclic();
        int[] pxy = parsePosToXY(game.getPos(code).toString(), w, h);
        if (pxy == null) return randomDir();
        int px = pxy[0], py = pxy[1];

        _visit[px][py]++;

        GhostCL[] ghosts = game.getGhosts(code);

        int dir = chooseDir(board, w, h, px, py, blue, pink, ghosts, code, cyclic);

        if (dir == _lastDir) _sameDirStreak++;
        else _sameDirStreak = 0;

        _lastDir = dir;
        _count++;
        return dir;
    }

    private int chooseDir(int[][] board, int w, int h, int px, int py, int wall, int food,
                          GhostCL[] ghosts, int code, boolean cyclic) {

        int[]   dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};

        int bestDir = PacmanGame.ERR;
        double bestScore = -1e18;

        for (int d : dirs) {
            int[] nxy = step(px, py, d, w, h, cyclic);
            if (nxy == null) continue;
            int nx = nxy[0], ny = nxy[1];
            if (board[nx][ny] == wall) continue;

            int[][] distFromNext = bfsDistances(board, w, h, nx, ny, wall, cyclic);

            int foodDist = nearestFoodDist(board, w, h, food, distFromNext);
            int ghostDist = nearestGhostDist(w, h, ghosts, code, distFromNext);

            double score = 0.0;

            if (board[nx][ny] == food) score += 100000.0;

            if (foodDist >= 0) score += 5000.0 / (1.0 + foodDist);
            else score -= 2000.0;


            score -= 30.0 * _visit[nx][ny];

            if (_sameDirStreak >= 10 && d == _lastDir) score -= 500.0;

            if (ghostDist >= 0) {
                if (ghostDist <= 2) score -= 200000.0;
                else if (ghostDist == 3) score -= 100000.0;
                else if (ghostDist == 4) score -= 15000.0;
                else score -= 2000.0 / (1.0 + ghostDist);
            }

            if (score > bestScore) {
                bestScore = score;
                bestDir = d;
            }
        }

        if (bestDir == PacmanGame.ERR) return randomDir();
        return bestDir;
    }

    private static int nearestFoodDist(int[][] board, int w, int h, int food, int[][] dist) {
        int best = Integer.MAX_VALUE;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (board[x][y] == food) {
                    int d = dist[x][y];
                    if (d >= 0 && d < best) best = d;
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    private static int nearestGhostDist(int w, int h, GhostCL[] ghosts, int code, int[][] dist) {
        if (ghosts == null || ghosts.length == 0) return -1;
        int best = Integer.MAX_VALUE;

        for (GhostCL g : ghosts) {
            if (g == null) continue;
            if (g.remainTimeAsEatable(code) > 0) continue;

            int[] gp = parsePosToXY(g.getPos(code).toString(), w, h);
            if (gp == null) continue;

            int d = dist[gp[0]][gp[1]];
            if (d >= 0 && d < best) best = d;
        }

        return best == Integer.MAX_VALUE ? -1 : best;
    }

    private static int[][] bfsDistances(int[][] board, int w, int h, int sx, int sy, int wall, boolean cyclic) {
        int[][] dist = new int[w][h];
        for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) dist[x][y] = -1;

        ArrayDeque<int[]> q = new ArrayDeque<>();
        dist[sx][sy] = 0;
        q.add(new int[]{sx, sy});

        int[][] steps = {{0,1},{0,-1},{-1,0},{1,0}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];

            for (int[] s : steps) {
                int nx = x + s[0], ny = y + s[1];

                if (cyclic) { nx = (nx + w) % w; ny = (ny + h) % h; }
                if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
                if (board[nx][ny] == wall) continue;
                if (dist[nx][ny] != -1) continue;

                dist[nx][ny] = dist[x][y] + 1;
                q.add(new int[]{nx, ny});
            }
        }
        return dist;
    }

    private static int[] step(int x, int y, int dir, int w, int h, boolean cyclic) {
        int nx = x, ny = y;
        if (dir == Game.UP) ny += 1;
        else if (dir == Game.DOWN) ny -= 1;
        else if (dir == Game.LEFT) nx -= 1;
        else if (dir == Game.RIGHT) nx += 1;
        else return null;

        if (cyclic) {
            nx = (nx + w) % w;
            ny = (ny + h) % h;
        }
        if (nx < 0 || nx >= w || ny < 0 || ny >= h) return null;
        return new int[]{nx, ny};
    }

    private static int[] parsePosToXY(String pos, int w, int h) {
        if (pos == null) return null;
        String s = pos.replace("(", "").replace(")", "").trim();
        String[] parts = s.split(",");
        if (parts.length < 2) return null;
        try {
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            if (x < 0 || x >= w || y < 0 || y >= h) return null;
            return new int[]{x, y};
        } catch (Exception e) {
            return null;
        }
    }

    private static int randomDir() {
        int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
        int ind = (int)(Math.random()*dirs.length);
        return dirs[ind];
    }
}