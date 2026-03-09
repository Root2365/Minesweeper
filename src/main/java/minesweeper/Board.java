package minesweeper;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Set;

/**
 * Owns board state and reveal behavior.
 */
public final class Board {
    private final int size;
    private final Cell[][] cells;
    private final int safeCellCount;
    private int revealedSafeCellCount;

    public Board(int size, int mineCount, MinePlacer minePlacer) {
        if (!GameConfig.isValidGridSize(size)) {
            throw new IllegalArgumentException("Grid size must be between 1 and " + GameConfig.MAX_GRID_SIZE + ".");
        }
        if (!GameConfig.isValidMineCount(size, mineCount)) {
            int maxMineCount = GameConfig.maxMineCount(size);
            throw new IllegalArgumentException(
                    "Mine count must be between 0 and " + maxMineCount + " for a " + size + "x" + size + " grid."
            );
        }

        this.size = size;
        this.cells = createEmptyCells(size);
        this.safeCellCount = (size * size) - mineCount;
        placeMines(mineCount, minePlacer);
        calculateAdjacentMineCounts();
    }

    public int size() {
        return size;
    }

    public MoveResult reveal(Coordinate coordinate) {
        validateCoordinate(coordinate);

        Cell selectedCell = cellAt(coordinate);
        if (selectedCell.isRevealed()) {
            return MoveResult.alreadyRevealed(selectedCell.adjacentMineCount());
        }
        if (selectedCell.hasMine()) {
            return MoveResult.hitMine();
        }

        int adjacentMines = selectedCell.adjacentMineCount();
        floodReveal(coordinate);
        return MoveResult.safeReveal(adjacentMines);
    }

    public boolean hasWon() {
        return revealedSafeCellCount == safeCellCount;
    }

    public boolean isRevealed(Coordinate coordinate) {
        validateCoordinate(coordinate);
        return cellAt(coordinate).isRevealed();
    }

    public String displayValue(int row, int col, boolean showAllMines) {
        Coordinate coordinate = new Coordinate(row, col);
        validateCoordinate(coordinate);

        Cell cell = cellAt(coordinate);
        if (cell.isRevealed()) {
            return cell.hasMine() ? "*" : Integer.toString(cell.adjacentMineCount());
        }
        if (showAllMines && cell.hasMine()) {
            return "*";
        }
        return "_";
    }

    private Cell[][] createEmptyCells(int boardSize) {
        Cell[][] newCells = new Cell[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                newCells[row][col] = new Cell();
            }
        }
        return newCells;
    }

    private void placeMines(int mineCount, MinePlacer minePlacer) {
        Objects.requireNonNull(minePlacer, "MinePlacer is required.");

        Set<Coordinate> mineCoordinates = minePlacer.placeMines(size, mineCount);
        if (mineCoordinates.size() != mineCount) {
            throw new IllegalArgumentException(
                    "MinePlacer must return exactly " + mineCount + " unique mine coordinates."
            );
        }

        for (Coordinate coordinate : mineCoordinates) {
            validateCoordinate(coordinate);
            cellAt(coordinate).placeMine();
        }
    }

    private void calculateAdjacentMineCounts() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell currentCell = cells[row][col];
                if (currentCell.hasMine()) {
                    continue;
                }
                currentCell.setAdjacentMineCount(countAdjacentMines(row, col));
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int mines = 0;
        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                if (rowDelta == 0 && colDelta == 0) {
                    continue;
                }
                int candidateRow = row + rowDelta;
                int candidateCol = col + colDelta;
                if (isWithinBounds(candidateRow, candidateCol) && cells[candidateRow][candidateCol].hasMine()) {
                    mines++;
                }
            }
        }
        return mines;
    }

    private void floodReveal(Coordinate start) {
        ArrayDeque<Coordinate> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Coordinate current = queue.removeFirst();
            if (!isWithinBounds(current.row(), current.col())) {
                continue;
            }

            Cell currentCell = cellAt(current);
            if (currentCell.isRevealed() || currentCell.hasMine()) {
                continue;
            }

            currentCell.reveal();
            revealedSafeCellCount++;

            if (currentCell.adjacentMineCount() != 0) {
                continue;
            }

            for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
                for (int colDelta = -1; colDelta <= 1; colDelta++) {
                    if (rowDelta == 0 && colDelta == 0) {
                        continue;
                    }
                    int nextRow = current.row() + rowDelta;
                    int nextCol = current.col() + colDelta;
                    if (isWithinBounds(nextRow, nextCol)) {
                        queue.addLast(new Coordinate(nextRow, nextCol));
                    }
                }
            }
        }
    }

    private void validateCoordinate(Coordinate coordinate) {
        Objects.requireNonNull(coordinate, "Coordinate is required.");
        if (!isWithinBounds(coordinate.row(), coordinate.col())) {
            throw new IllegalArgumentException(
                    "Coordinate is out of bounds for board size " + size + ": (" + coordinate.row() + ", " + coordinate.col() + ")."
            );
        }
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private Cell cellAt(Coordinate coordinate) {
        return cells[coordinate.row()][coordinate.col()];
    }
}
