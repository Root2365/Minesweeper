package minesweeper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Random mine placement implementation.
 */
public final class RandomMinePlacer implements MinePlacer {
    private final Random random;

    public RandomMinePlacer(Random random) {
        this.random = Objects.requireNonNull(random, "Random instance is required.");
    }

    @Override
    public Set<Coordinate> placeMines(int boardSize, int mineCount) {
        if (boardSize <= 0) {
            throw new IllegalArgumentException("Board size must be greater than 0.");
        }
        if (mineCount < 0 || mineCount > boardSize * boardSize) {
            throw new IllegalArgumentException("Mine count must be between 0 and boardSize^2.");
        }

        Set<Coordinate> mines = new HashSet<>();
        while (mines.size() < mineCount) {
            mines.add(new Coordinate(random.nextInt(boardSize), random.nextInt(boardSize)));
        }
        return mines;
    }
}
