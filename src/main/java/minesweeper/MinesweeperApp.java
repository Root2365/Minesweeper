package minesweeper;

/**
 * Command-line entry point for the Minesweeper game.
 */
public final class MinesweeperApp {
    private MinesweeperApp() {
    }

    public static void main(String[] args) {
        ConsoleMinesweeper.createDefault().run();
    }
}
