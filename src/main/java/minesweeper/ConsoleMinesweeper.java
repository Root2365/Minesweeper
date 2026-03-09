package minesweeper;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * CLI game runner that orchestrates one or more Minesweeper rounds.
 */
public final class ConsoleMinesweeper {
    private final Scanner scanner;
    private final PrintStream out;
    private final Supplier<MinePlacer> minePlacerSupplier;
    private final CoordinateParser coordinateParser;
    private final BoardPrinter boardPrinter;

    ConsoleMinesweeper(
            Scanner scanner,
            PrintStream out,
            Supplier<MinePlacer> minePlacerSupplier,
            CoordinateParser coordinateParser,
            BoardPrinter boardPrinter
    ) {
        this.scanner = Objects.requireNonNull(scanner, "Scanner is required.");
        this.out = Objects.requireNonNull(out, "Print stream is required.");
        this.minePlacerSupplier = Objects.requireNonNull(minePlacerSupplier, "Mine placer supplier is required.");
        this.coordinateParser = Objects.requireNonNull(coordinateParser, "CoordinateParser is required.");
        this.boardPrinter = Objects.requireNonNull(boardPrinter, "BoardPrinter is required.");
    }

    public static ConsoleMinesweeper createDefault() {
        CoordinateParser coordinateParser = new CoordinateParser();
        return new ConsoleMinesweeper(
                new Scanner(System.in),
                System.out,
                () -> new RandomMinePlacer(new Random()),
                coordinateParser,
                new BoardPrinter(coordinateParser)
        );
    }

    public void run() {
        while (true) {
            playSingleRound();
            out.print("Press Enter to play again...");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            out.println();
        }
    }

    void playSingleRound() {
        out.println("Welcome to Minesweeper!");
        out.println();

        int gridSize = promptGridSize();
        int maxMineCount = GameConfig.maxMineCount(gridSize);
        int mineCount = promptMineCount(maxMineCount);

        Board board = new Board(gridSize, mineCount, minePlacerSupplier.get());

        out.println();
        out.println("Here is your minefield:");
        out.print(boardPrinter.render(board, false));
        out.println();

        while (true) {
            Coordinate coordinate = promptCoordinate(gridSize);
            MoveResult result = board.reveal(coordinate);

            if (result.type() == MoveType.HIT_MINE) {
                out.println("Oh no, you detonated a mine! Game over.");
                out.println();
                out.println("Here is your updated minefield:");
                out.print(boardPrinter.render(board, true));
                out.println();
                return;
            }

            if (result.type() == MoveType.ALREADY_REVEALED) {
                out.println("This square is already uncovered.");
            } else {
                out.printf("This square contains %d adjacent mines.%n", result.adjacentMines());
            }
            out.println();
            out.println("Here is your updated minefield:");
            out.print(boardPrinter.render(board, false));

            if (board.hasWon()) {
                out.println();
                out.println("Congratulations, you have won the game!");
                out.println();
                return;
            }
            out.println();
        }
    }

    private int promptGridSize() {
        while (true) {
            out.println("Enter the size of the grid (e.g. 4 for a 4x4 grid): ");
            String input = scanner.nextLine().trim();
            try {
                int size = Integer.parseInt(input);
                if (GameConfig.isValidGridSize(size)) {
                    return size;
                }
            } catch (NumberFormatException ignored) {
            }
            out.printf("Please enter a whole number between 1 and %d.%n%n", GameConfig.MAX_GRID_SIZE);
        }
    }

    private int promptMineCount(int maxMineCount) {
        while (true) {
            out.println("Enter the number of mines to place on the grid (maximum is 35% of the total squares): ");
            String input = scanner.nextLine().trim();
            try {
                int mineCount = Integer.parseInt(input);
                if (mineCount >= 0 && mineCount <= maxMineCount) {
                    return mineCount;
                }
            } catch (NumberFormatException ignored) {
            }
            out.printf("Please enter a whole number between 0 and %d.%n%n", maxMineCount);
        }
    }

    private Coordinate promptCoordinate(int boardSize) {
        while (true) {
            out.print("Select a square to reveal (e.g. A1): ");
            String rawInput = scanner.nextLine().trim();
            Optional<Coordinate> parsedCoordinate = coordinateParser.parse(rawInput, boardSize);
            if (parsedCoordinate.isPresent()) {
                return parsedCoordinate.get();
            }
            out.println("Invalid square. Please use a valid coordinate inside the grid, such as A1.");
            out.println();
        }
    }
}
