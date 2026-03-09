package minesweeper;

import java.util.Objects;

/**
 * Renders a board to a displayable string.
 */
public final class BoardPrinter {
    private final CoordinateParser coordinateParser;

    public BoardPrinter(CoordinateParser coordinateParser) {
        this.coordinateParser = Objects.requireNonNull(coordinateParser, "CoordinateParser is required.");
    }

    public String render(Board board, boolean showAllMines) {
        Objects.requireNonNull(board, "Board is required.");

        int boardSize = board.size();
        int rowLabelWidth = coordinateParser.rowIndexToLabel(boardSize - 1).length();
        int cellWidth = Math.max(2, String.valueOf(boardSize).length() + 1);

        StringBuilder output = new StringBuilder();
        output.append(String.format("%" + (rowLabelWidth + 1) + "s", ""));
        for (int col = 1; col <= boardSize; col++) {
            output.append(String.format("%" + cellWidth + "d", col));
        }
        output.append(System.lineSeparator());

        for (int row = 0; row < boardSize; row++) {
            output.append(String.format("%" + rowLabelWidth + "s ", coordinateParser.rowIndexToLabel(row)));
            for (int col = 0; col < boardSize; col++) {
                output.append(String.format("%" + cellWidth + "s", board.displayValue(row, col, showAllMines)));
            }
            output.append(System.lineSeparator());
        }
        return output.toString();
    }
}
