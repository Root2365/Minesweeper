package minesweeper;

import java.util.Set;

/**
 * Strategy abstraction for mine placement.
 */
public interface MinePlacer {
    Set<Coordinate> placeMines(int boardSize, int mineCount);
}
