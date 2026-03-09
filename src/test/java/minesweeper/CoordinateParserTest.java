package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class CoordinateParserTest {
    private final CoordinateParser parser = new CoordinateParser();

    @Test
    void shouldParseSingleLetterCoordinate() {
        Optional<Coordinate> coordinate = parser.parse("A1", 4);

        assertTrue(coordinate.isPresent());
        assertEquals(new Coordinate(0, 0), coordinate.get());
    }

    @Test
    void shouldParseMultiLetterCoordinate() {
        Optional<Coordinate> coordinate = parser.parse("AA10", 30);

        assertTrue(coordinate.isPresent());
        assertEquals(new Coordinate(26, 9), coordinate.get());
    }

    @Test
    void shouldRejectCoordinatesOutsideBoard() {
        assertTrue(parser.parse("D1", 3).isEmpty());
        assertTrue(parser.parse("A4", 3).isEmpty());
    }

    @Test
    void shouldRejectMalformedCoordinates() {
        assertTrue(parser.parse("1A", 4).isEmpty());
        assertTrue(parser.parse("A0", 4).isEmpty());
        assertTrue(parser.parse(" ", 4).isEmpty());
    }

    @Test
    void shouldConvertRowIndexToLabel() {
        assertEquals("A", parser.rowIndexToLabel(0));
        assertEquals("Z", parser.rowIndexToLabel(25));
        assertEquals("AA", parser.rowIndexToLabel(26));
    }
}
