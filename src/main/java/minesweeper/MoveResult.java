package minesweeper;

import java.util.Objects;

/**
 * Value object representing reveal outcome and adjacent mine count when relevant.
 */
public record MoveResult(MoveType type, int adjacentMines) {
    public MoveResult {
        Objects.requireNonNull(type, "Move type is required.");
    }

    public static MoveResult safeReveal(int adjacentMines) {
        return new MoveResult(MoveType.SAFE_REVEAL, adjacentMines);
    }

    public static MoveResult hitMine() {
        return new MoveResult(MoveType.HIT_MINE, -1);
    }

    public static MoveResult alreadyRevealed(int adjacentMines) {
        return new MoveResult(MoveType.ALREADY_REVEALED, adjacentMines);
    }
}
