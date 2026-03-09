package minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameConfigTest {

    @Test
    void shouldValidateGridSize() {
        assertTrue(GameConfig.isValidGridSize(1));
        assertTrue(GameConfig.isValidGridSize(500));
        assertFalse(GameConfig.isValidGridSize(0));
        assertFalse(GameConfig.isValidGridSize(501));
    }

    @Test
    void shouldValidateMineCountAgainstAllowedRatio() {
        assertTrue(GameConfig.isValidMineCount(4, 5));
        assertFalse(GameConfig.isValidMineCount(4, 6));
        assertFalse(GameConfig.isValidMineCount(0, 0));
    }

    @Test
    void shouldCalculateMaxMineCountUsingThirtyFivePercentCap() {
        assertEquals(5, GameConfig.maxMineCount(4));
        assertEquals(3, GameConfig.maxMineCount(3));
    }

    @Test
    void shouldRejectMaxMineCountCalculationForInvalidGridSize() {
        assertThrows(IllegalArgumentException.class, () -> GameConfig.maxMineCount(0));
    }
}
