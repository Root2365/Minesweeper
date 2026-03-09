package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void shouldReturnHitMineWhenMineIsRevealed() {
        Board board = new Board(3, 1, fixedMinePlacer(Set.of(new Coordinate(1, 1))));

        MoveResult result = board.reveal(new Coordinate(1, 1));

        assertEquals(MoveType.HIT_MINE, result.type());
        assertFalse(board.hasWon());
    }

    @Test
    void shouldRevealConnectedSafeAreaWhenSelectedCellHasZeroAdjacentMines() {
        Board board = new Board(4, 1, fixedMinePlacer(Set.of(new Coordinate(0, 0))));

        MoveResult result = board.reveal(new Coordinate(3, 3));

        assertEquals(MoveType.SAFE_REVEAL, result.type());
        assertEquals(0, result.adjacentMines());
        assertTrue(board.isRevealed(new Coordinate(0, 1)));
        assertTrue(board.isRevealed(new Coordinate(1, 1)));
        assertTrue(board.hasWon());
    }

    @Test
    void shouldReturnAlreadyRevealedWhenSelectingSameSafeCellTwice() {
        Board board = new Board(
                3,
                2,
                fixedMinePlacer(Set.of(new Coordinate(0, 0), new Coordinate(2, 2)))
        );

        MoveResult firstReveal = board.reveal(new Coordinate(1, 1));
        MoveResult secondReveal = board.reveal(new Coordinate(1, 1));

        assertEquals(MoveType.SAFE_REVEAL, firstReveal.type());
        assertEquals(2, firstReveal.adjacentMines());
        assertEquals(MoveType.ALREADY_REVEALED, secondReveal.type());
        assertEquals(2, secondReveal.adjacentMines());
    }

    @Test
    void shouldFailWhenMinePlacerDoesNotReturnRequestedMineCount() {
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> new Board(3, 1, fixedMinePlacer(Set.of()))
        );

        assertTrue(error.getMessage().contains("exactly 1"));
    }

    private static MinePlacer fixedMinePlacer(Set<Coordinate> coordinates) {
        return (boardSize, mineCount) -> coordinates;
    }
}
