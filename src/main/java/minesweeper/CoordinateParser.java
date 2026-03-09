package minesweeper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses player coordinate input such as "A1" or "AA10".
 */
public final class CoordinateParser {
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("^([A-Za-z]+)\\s*(\\d+)$");

    public Optional<Coordinate> parse(String rawInput, int boardSize) {
        if (rawInput == null || boardSize <= 0) {
            return Optional.empty();
        }

        Matcher matcher = COORDINATE_PATTERN.matcher(rawInput.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        int row = rowLabelToIndex(matcher.group(1));
        int col;
        try {
            col = Integer.parseInt(matcher.group(2)) - 1;
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }

        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
            return Optional.empty();
        }

        return Optional.of(new Coordinate(row, col));
    }

    public String rowIndexToLabel(int rowIndex) {
        if (rowIndex < 0) {
            throw new IllegalArgumentException("Row index must be non-negative.");
        }

        StringBuilder label = new StringBuilder();
        int value = rowIndex;
        while (value >= 0) {
            int remainder = value % 26;
            label.append((char) ('A' + remainder));
            value = (value / 26) - 1;
        }
        return label.reverse().toString();
    }

    private int rowLabelToIndex(String rowLabel) {
        if (rowLabel == null || rowLabel.isBlank()) {
            return -1;
        }

        int value = 0;
        for (int i = 0; i < rowLabel.length(); i++) {
            char current = Character.toUpperCase(rowLabel.charAt(i));
            if (current < 'A' || current > 'Z') {
                return -1;
            }
            value = value * 26 + (current - 'A' + 1);
        }
        return value - 1;
    }
}
