package minesweeper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class ConsoleMinesweeperE2ETest {

    @Test
    void shouldPlayToWinForDeterministicInputSequence() {
        String input = String.join(
                System.lineSeparator(),
                "2",
                "1",
                "A2",
                "B1",
                "B2"
        ) + System.lineSeparator();

        String output = executeSingleRound(input, () -> fixedMinePlacer(Set.of(new Coordinate(0, 0))));

        assertTrue(output.contains("Welcome to Minesweeper!"));
        assertTrue(output.contains("Here is your minefield:"));
        assertTrue(output.contains("Congratulations, you have won the game!"));
    }

    @Test
    void shouldEndRoundWithGameOverWhenMineIsSelected() {
        String input = String.join(
                System.lineSeparator(),
                "2",
                "1",
                "A1"
        ) + System.lineSeparator();

        String output = executeSingleRound(input, () -> fixedMinePlacer(Set.of(new Coordinate(0, 0))));

        assertTrue(output.contains("Oh no, you detonated a mine! Game over."));
        assertTrue(output.contains("Here is your updated minefield:"));
    }

    private static String executeSingleRound(String rawInput, Supplier<MinePlacer> minePlacerSupplier) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(rawInput.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
             PrintStream out = new PrintStream(outputStream, true, StandardCharsets.UTF_8)) {

            CoordinateParser parser = new CoordinateParser();
            BoardPrinter printer = new BoardPrinter(parser);
            ConsoleMinesweeper game = new ConsoleMinesweeper(scanner, out, minePlacerSupplier, parser, printer);
            game.playSingleRound();
            return outputStream.toString(StandardCharsets.UTF_8);
        }
    }

    private static MinePlacer fixedMinePlacer(Set<Coordinate> coordinates) {
        return (boardSize, mineCount) -> coordinates;
    }
}
