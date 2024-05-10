package pacman_game;

import javafx.geometry.Point2D;

public class PacMan_Ghost3 extends PacMan_Ghost {
    @Override
    Point2D sendGhostHome() {
        for (int row = 0; row < this.rowCount; row++) {
            for (int column = 0; column < this.columnCount; column++) {
                if (grid[row][column] == PacMan_Model.CellValue.GHOST3HOME) {
                    ghost3Location = new Point2D(row, column);
                }
            }
        }
        return ghost3Velocity = new Point2D(-1, 0);
    }
}
