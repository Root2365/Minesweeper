package minesweeper;

/**
 * Mutable cell state for the board.
 */
final class Cell {
    private boolean mine;
    private boolean revealed;
    private int adjacentMineCount;

    boolean hasMine() {
        return mine;
    }

    void placeMine() {
        this.mine = true;
    }

    boolean isRevealed() {
        return revealed;
    }

    void reveal() {
        this.revealed = true;
    }

    int adjacentMineCount() {
        return adjacentMineCount;
    }

    void setAdjacentMineCount(int adjacentMineCount) {
        if (adjacentMineCount < 0 || adjacentMineCount > 8) {
            throw new IllegalArgumentException("Adjacent mine count must be between 0 and 8.");
        }
        this.adjacentMineCount = adjacentMineCount;
    }
}
