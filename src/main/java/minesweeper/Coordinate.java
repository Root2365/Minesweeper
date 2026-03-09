package minesweeper;

/**
 * Zero-based row and column pair for board access.
 */
public record Coordinate(int row, int col) {
    public Coordinate {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Coordinate values must be non-negative.");
        }
    }
}
