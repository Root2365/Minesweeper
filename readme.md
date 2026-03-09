# Minesweeper (Java CLI)

## Brief design explanation
This project is implemented with separate classes to keep responsibilities clear and maintainable:
- `MinesweeperApp`: minimal application entry point.
- `ConsoleMinesweeper`: command-line flow and user interaction.
- `Board`: core game rules and state transitions (reveal logic, win checks).
- `Cell`: per-cell state (mine, revealed, adjacent mine count).
- `Coordinate` / `CoordinateParser`: coordinate model and input parsing (`A1`, `AA10`).
- `BoardPrinter`: rendering board output for CLI.
- `MinePlacer` (interface) and `RandomMinePlacer` (implementation): mine placement strategy.
- `MoveResult` / `MoveType`: explicit move outcomes.
- `GameConfig`: validation rules for grid size and mine constraints.

This structure supports clean code, testability, and evolution without tightly coupling UI and game logic.

## Assumptions
- The board is square (`N x N`).
- Mine count is capped at `floor(35% of N*N)` to align with the problem statement.
- Input coordinates use row letters + column number (example: `B3`, `AA10`).
- Revealing a zero-adjacent cell triggers flood reveal of connected safe cells.
- The player wins when all non-mine cells are revealed.

## Environment required
- Operating systems: Windows, Linux, or macOS
- Java: JDK 17 or newer
- Build/test tool: Maven 3.9 or newer

## Detailed instructions to run the application
### Windows (PowerShell)
1. Open PowerShell.
2. Go to the project directory:
   `cd "c:\Projects\Simple assignment projects\MineSweeper"`
3. Build:
   `mvn clean package`
4. Run:
   `java -cp target\classes minesweeper.MinesweeperApp`

### Linux/macOS (bash/zsh)
1. Open terminal.
2. Go to the project directory.
3. Build:
   `mvn clean package`
4. Run:
   `java -cp target/classes minesweeper.MinesweeperApp`

## Detailed instructions to run tests
From the project root:
- `mvn test`

Testing approach:
- Unit tests and end-to-end tests are both included.
- Tests were written to validate behavior incrementally (TDD-friendly workflow), especially around parsing, board logic, and CLI round outcomes.

Current tests cover key behaviors including:
- config validation (`GameConfigTest`)
- coordinate parsing (`CoordinateParserTest`)
- reveal/game outcomes (`BoardTest`)
- end-to-end CLI round flows (`ConsoleMinesweeperE2ETest`)

## Production-quality and readability notes
- The code is organized for maintainability (small focused classes and methods).
- Game logic is unit tested independently from CLI concerns.
- Dependency on abstractions (`MinePlacer`) supports extensibility and follows SOLID principles.
- The project is ready to run locally with standard Java + Maven commands and can be evolved without major restructuring.

## Client requirement checklist
- Include unit/end-to-end tests: met (`BoardTest`, `CoordinateParserTest`, `GameConfigTest`, `ConsoleMinesweeperE2ETest`).
- Keep functions/classes small: met (separated responsibilities across focused classes).
- Demonstrate clean code, OOP, SOLID: met (domain modeling, clear boundaries, strategy abstraction via `MinePlacer`).
- Submit production-ready code with documentation: met (documented architecture/assumptions, environment requirements, and detailed run/test instructions in this README).
