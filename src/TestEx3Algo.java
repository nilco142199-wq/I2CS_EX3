import exe.ex3.game.Game;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TestEx3Algo {

    @Test
    void testGetInfo() {
        Ex3Algo algo = new Ex3Algo();
        assertNotNull(algo.getInfo());
        assertFalse(algo.getInfo().isEmpty());
    }

    @Test
    void testParsePosValid() throws Exception {
        Method m = Ex3Algo.class.getDeclaredMethod(
                "parsePosToXY", String.class, int.class, int.class
        );
        m.setAccessible(true);

        int[] res = (int[]) m.invoke(null, "(3,4)", 10, 10);
        assertArrayEquals(new int[]{3,4}, res);
    }

    @Test
    void testParsePosInvalid() throws Exception {
        Method m = Ex3Algo.class.getDeclaredMethod(
                "parsePosToXY", String.class, int.class, int.class
        );
        m.setAccessible(true);

        Object res = m.invoke(null, "(20,4)", 5, 5);
        assertNull(res);
    }

    @Test
    void testStepNormal() throws Exception {
        Method m = Ex3Algo.class.getDeclaredMethod(
                "step", int.class, int.class, int.class, int.class, int.class, boolean.class
        );
        m.setAccessible(true);

        int[] res = (int[]) m.invoke(null, 2, 2, Game.UP, 5, 5, false);
        assertArrayEquals(new int[]{2,3}, res);
    }

    @Test
    void testStepCyclic() throws Exception {
        Method m = Ex3Algo.class.getDeclaredMethod(
                "step", int.class, int.class, int.class, int.class, int.class, boolean.class
        );
        m.setAccessible(true);

        int[] res = (int[]) m.invoke(null, 0, 0, Game.LEFT, 5, 5, true);
        assertArrayEquals(new int[]{4,0}, res);
    }

    @Test
    void testBfsDistances() throws Exception {
        Method m = Ex3Algo.class.getDeclaredMethod(
                "bfsDistances",
                int[][].class, int.class, int.class,
                int.class, int.class, int.class, boolean.class
        );
        m.setAccessible(true);

        int WALL = Game.getIntColor(Color.BLUE, 0);

        int[][] board = {
                {0, 0, 0},
                {0, WALL, 0},
                {0, 0, 0}
        };

        int[][] dist = (int[][]) m.invoke(null, board, 3, 3, 0, 0, WALL, false);

        assertEquals(0, dist[0][0]);
        assertEquals(1, dist[1][0]);
        assertEquals(-1, dist[1][1]);
    }
}
