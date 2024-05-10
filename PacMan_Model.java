package pacman_game;

import javafx.geometry.Point2D;

public class PacMan_Model extends PacMan_Ghost {
    @Override
    Point2D sendGhostHome() {
        return null;
    }

    private PacMan_Ghost ghost;
    public enum CellValue {
        EMPTY, SMALLDOT, BIGDOT, WALL, GHOST1HOME, GHOST2HOME, GHOST3HOME, GHOST4HOME, PACMANHOME
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    /**
     *  kaller fram start new game metoden for å starte nytt spill når applikasjonen lastes
     */
    public PacMan_Model() {
        this.startNewGame();
    }

    public void startNewGame() {
        PacMan_Ghost.gameOver = false;
        PacMan_Ghost.youWon = false;
        PacMan_Ghost.ghostEatingMode = false;
        dotCount = 0;
        rowCount = 0;
        columnCount = 0;
        this.score = 0;
        this.level = 1;
        this.initializeLevel(PacMan_Controller.getLevelFile(0));
    }

    /**
     * vi har 3 liv for nytt spill, når du blir fanget en gang av spøkelser, 
     * ring denne metoden og endre livstelleren og start spillet på nytt
     */
    public void life() {
        PacMan_Ghost.gameOver = false;
        PacMan_Ghost.ghostEatingMode = false;
        rowCount = 0;
        this.score = 0;
        columnCount = 0;
        this.initializeLevel(PacMan_Controller.getLevelFile(0));
    }

    /**
     * her vinner vi spillet og kan spille neste nivå av spillet
     */
    public void startNextLevel() {
        if (this.isLevelComplete()) {
            this.level++;
            rowCount = 0;
            columnCount = 0;
            youWon = true;
            ghostEatingMode = false;
            try {
                this.initializeLevel(PacMan_Controller.getLevelFile(level - 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                /**
                 * hvis det ikke er flere nivåer igjen i nivåarrayet, avsluttes spillet
                 */
                youWon = true;
                gameOver = true;
                level--;
            }
        }
    }

    /**
     * Flytt PacMan basert på retningen angitt av brukeren
     * den sist angitte retningen for at PacMan skal bevege seg i
     */
    public void movePacman(Direction direction) {
        Point2D potentialPacmanVelocity = changeVelocity(direction);
        Point2D potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
        potentialPacmanLocation = setGoingOffscreenNewLocation(potentialPacmanLocation);
        if (direction.equals(lastDirection)) {
            try {
                if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                    pacmanVelocity = changeVelocity(Direction.NONE);
                    setLastDirection(Direction.NONE);
                } else {
                    pacmanVelocity = potentialPacmanVelocity;
                    pacmanLocation = potentialPacmanLocation;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                potentialPacmanVelocity = changeVelocity(lastDirection);
                potentialPacmanLocation = pacmanLocation.add(potentialPacmanVelocity);
                if (grid[(int) potentialPacmanLocation.getX()][(int) potentialPacmanLocation.getY()] == CellValue.WALL) {
                    pacmanVelocity = changeVelocity(Direction.NONE);
                    setLastDirection(Direction.NONE);
                } else {
                    pacmanVelocity = changeVelocity(lastDirection);
                    pacmanLocation = pacmanLocation.add(pacmanVelocity);
                }
            } else {
                pacmanVelocity = potentialPacmanVelocity;
                pacmanLocation = potentialPacmanLocation;
                setLastDirection(direction);
            }
        }
    }

    /**
     * Move ghosts to follow PacMan as established in moveGhost()
     */
    public void moveGhosts() {
        try {
            Point2D[] ghost1Data = moveGhost(ghost1Velocity, ghost1Location);
            Point2D[] ghost2Data = moveGhost(ghost2Velocity, ghost2Location);
            Point2D[] ghost3Data = moveGhost(ghost3Velocity, ghost3Location);
            Point2D[] ghost4Data = moveGhost(ghost4Velocity, ghost4Location);
            ghost1Velocity = ghost1Data[0];
            ghost1Location = ghost1Data[1];
            ghost2Velocity = ghost2Data[0];
            ghost2Location = ghost2Data[1];
            ghost3Velocity = ghost3Data[0];
            ghost3Location = ghost3Data[1];
            ghost4Velocity = ghost4Data[0];
            ghost4Location = ghost4Data[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void step(Direction direction) {
        this.movePacman(direction);
        CellValue pacmanLocationCellValue = grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()];
        if (pacmanLocationCellValue == CellValue.SMALLDOT) {
            grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
            dotCount--;
            score += 10;
        }
        if (pacmanLocationCellValue == CellValue.BIGDOT) {
            grid[(int) pacmanLocation.getX()][(int) pacmanLocation.getY()] = CellValue.EMPTY;
            dotCount--;
            score += 50;
            ghostEatingMode = true;
            PacMan_Controller.setGhostEatingModeCounter();
        }
        if (ghostEatingMode) {
            if (pacmanLocation.equals(ghost1Location)) {
                ghost = new PacMan_Ghost1();
                ghost.sendGhostHome();
                score += 100;
            }
            if (pacmanLocation.equals(ghost2Location)) {
                ghost = new PacMan_Ghost2();
                ghost.sendGhostHome();
                score += 100;
            }
            if (pacmanLocation.equals(ghost3Location)) {
                ghost = new PacMan_Ghost3();
                ghost.sendGhostHome();
                score += 100;
            }
            if (pacmanLocation.equals(ghost4Location)) {
                ghost = new PacMan_Ghost4();
                ghost.sendGhostHome();
                score += 100;
            }
        } else {
            if (pacmanLocation.equals(ghost1Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost2Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost3Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost4Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
        }
        this.moveGhosts();
        if (ghostEatingMode) {
            if (pacmanLocation.equals(ghost1Location)) {
                ghost = new PacMan_Ghost1();
                ghost.sendGhostHome();
                score += 100;
            }
            if (pacmanLocation.equals(ghost2Location)) {
                ghost = new PacMan_Ghost2();
                ghost.sendGhostHome();
                score += 100;
            }

            if (pacmanLocation.equals(ghost3Location)) {
                ghost = new PacMan_Ghost3();
                ghost.sendGhostHome();
                score += 100;
            }
            if (pacmanLocation.equals(ghost4Location)) {
                ghost = new PacMan_Ghost4();
                ghost.sendGhostHome();
                score += 100;
            }
        } else {
            if (pacmanLocation.equals(ghost1Location)) {

                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost2Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost3Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
            if (pacmanLocation.equals(ghost4Location)) {
                gameOver = true;
                pacmanVelocity = new Point2D(0, 0);
            }
        }
        if (this.isLevelComplete()) {
            pacmanVelocity = new Point2D(0, 0);
            startNextLevel();
        }
    }

    public static boolean isGhostEatingMode() {
        return ghostEatingMode;
    }

    public static void setGhostEatingMode(boolean ghostEatingModeBool) {
        ghostEatingMode = ghostEatingModeBool;
    }

    public static boolean isYouWon() {
        return youWon;
    }

    public boolean isLevelComplete() {
        return this.dotCount == 0;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public CellValue[][] getGrid() {
        return grid;
    }

    public CellValue getCellValue(int row, int column) {
        assert row >= 0 && row < this.grid.length && column >= 0 && column < this.grid[0].length;
        return this.grid[row][column];
    }

    public static Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
        currentDirection = direction;
    }

    public static Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction direction) {
        lastDirection = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int points) {
        this.score += points;
    }

    public Point2D getGhost2Location() {
        return ghost2Location;
    }

    public Point2D getGhost3Location() {
        return ghost3Location;
    }

    public Point2D getGhost4Location() {
        return ghost4Location;
    }

    public void setGhost2Location(Point2D ghost2Location) {
        this.ghost2Location = ghost2Location;
    }

    public Point2D getPacmanVelocity() {
        return pacmanVelocity;
    }

    public void setPacmanVelocity(Point2D velocity) {
        this.pacmanVelocity = velocity;
    }

    public Point2D getGhost1Velocity() {
        return ghost1Velocity;
    }

    public void setGhost1Velocity(Point2D ghost1Velocity) {
        this.ghost1Velocity = ghost1Velocity;
    }

    public Point2D getGhost2Velocity() {
        return ghost2Velocity;
    }

    public void setGhost2Velocity(Point2D ghost2Velocity) {
        this.ghost2Velocity = ghost2Velocity;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDotCount() {
        return dotCount;
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Point2D getPacmanLocation() {
        return pacmanLocation;
    }

    public void setPacmanLocation(Point2D pacmanLocation) {
        this.pacmanLocation = pacmanLocation;
    }

    public Point2D getGhost1Location() {
        return ghost1Location;
    }

    public void setGhost1Location(Point2D ghost1Location) {
        this.ghost1Location = ghost1Location;
    }


}
