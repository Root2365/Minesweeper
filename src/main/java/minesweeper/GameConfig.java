package minesweeper;

/**
 * Validation and limits for game configuration.
 */
public final class GameConfig {
    public static final int MAX_GRID_SIZE = 500;
    private static final double MAX_MINE_RATIO = 0.35;

    private GameConfig() {
    }

    public static boolean isValidGridSize(int gridSize) {
        return gridSize >= 1 && gridSize <= MAX_GRID_SIZE;
    }

    public static boolean isValidMineCount(int gridSize, int mineCount) {
        return isValidGridSize(gridSize) && mineCount >= 0 && mineCount <= maxMineCount(gridSize);
    }

    public static int maxMineCount(int gridSize) {
        if (!isValidGridSize(gridSize)) {
            throw new IllegalArgumentException("Grid size must be between 1 and " + MAX_GRID_SIZE + ".");
        }
        return (int) Math.floor(gridSize * gridSize * MAX_MINE_RATIO);
    }
}
