package pacman_game;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public abstract class PacMan_Ghost {

    @FXML
    int rowCount;
    @FXML
    int columnCount;
    PacMan_Model.CellValue[][] grid;
    int score, level, dotCount;
    static boolean gameOver;
    static boolean youWon;
    static boolean ghostEatingMode;
    Point2D pacmanLocation, pacmanVelocity, ghost1Location, ghost1Velocity, ghost2Location, ghost2Velocity, ghost3Location, ghost3Velocity, ghost4Location, ghost4Velocity;
    static PacMan_Model.Direction lastDirection;
    static PacMan_Model.Direction currentDirection;
    abstract Point2D sendGhostHome();

    public void initializeLevel(String fileName) {
        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try (Scanner lineScanner = new Scanner(line)) {
				while (lineScanner.hasNext()) {
				    lineScanner.next();
				    columnCount++;
				}
			}
            rowCount++;
        }
        columnCount = columnCount / rowCount;
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        grid = new PacMan_Model.CellValue[rowCount][columnCount];
        int row = 0;
        int pacmanRow = 0;
        int pacmanColumn = 0;
        int ghost1Row = 0;
        int ghost1Column = 0;
        int ghost2Row = 0;
        int ghost2Column = 0;
        while (scanner2.hasNextLine()) {
            int column = 0;
            String line = scanner2.nextLine();
            try (Scanner lineScanner = new Scanner(line)) {
				while (lineScanner.hasNext()) {
				    String value = lineScanner.next();
				    PacMan_Model.CellValue thisValue;
				    if (value.equals("W")) {
				        thisValue = PacMan_Model.CellValue.WALL;
				    } else if (value.equals("S")) {
				        thisValue = PacMan_Model.CellValue.SMALLDOT;
				        dotCount++;
				    } else if (value.equals("B")) {
				        thisValue = PacMan_Model.CellValue.BIGDOT;
				        dotCount++;
				    } else if (value.equals("1")) {
				        thisValue = PacMan_Model.CellValue.GHOST1HOME;
				        ghost1Row = row;
				        ghost1Column = column;
				    } else if (value.equals("2")) {
				        thisValue = PacMan_Model.CellValue.GHOST2HOME;
				        ghost2Row = row;
				        ghost2Column = column;
				    } else if (value.equals("3")) {
				        thisValue = PacMan_Model.CellValue.GHOST3HOME;
				        ghost2Row = row;
				        ghost2Column = column;
				    } else if (value.equals("4")) {
				        thisValue = PacMan_Model.CellValue.GHOST4HOME;
				        ghost2Row = row;
				        ghost2Column = column;
				    } else if (value.equals("P")) {
				        thisValue = PacMan_Model.CellValue.PACMANHOME;
				        pacmanRow = row;
				        pacmanColumn = column;
				    } else
				    {
				        thisValue = PacMan_Model.CellValue.EMPTY;
				    }
				    grid[row][column] = thisValue;
				    column++;
				}
			}
            row++;
        }
        pacmanLocation = new Point2D(pacmanRow, pacmanColumn);
        pacmanVelocity = new Point2D(0, 0);
        ghost1Location = new Point2D(ghost1Row, ghost1Column);
        ghost1Velocity = new Point2D(-1, 0);
        ghost2Location = new Point2D(ghost2Row, ghost2Column);
        ghost2Velocity = new Point2D(-1, 0);
        ghost3Location = new Point2D(ghost2Row, ghost2Column);
        ghost3Velocity = new Point2D(-1, 0);
        ghost4Location = new Point2D(ghost2Row, ghost2Column);
        ghost4Velocity = new Point2D(-1, 0);
        currentDirection = PacMan_Model.Direction.NONE;
        lastDirection = PacMan_Model.Direction.NONE;
    }

    public Point2D setGoingOffscreenNewLocation(Point2D objectLocation) {
        if (objectLocation.getY() >= columnCount) {
            objectLocation = new Point2D(objectLocation.getX(), 0);
        }
        if (objectLocation.getY() < 0) {
            objectLocation = new Point2D(objectLocation.getX(), columnCount - 1);
        }
        return objectLocation;
    }

    public PacMan_Model.Direction intToDirection(int x) {
        if (x == 0) {
            return PacMan_Model.Direction.LEFT;
        } else if (x == 1) {
            return PacMan_Model.Direction.RIGHT;
        } else if (x == 2) {
            return PacMan_Model.Direction.UP;
        } else {
            return PacMan_Model.Direction.DOWN;
        }
    }

    public Point2D changeVelocity(PacMan_Model.Direction direction) {
        if (direction == PacMan_Model.Direction.LEFT) {
            return new Point2D(0, -1);
        } else if (direction == PacMan_Model.Direction.RIGHT) {
            return new Point2D(0, 1);
        } else if (direction == PacMan_Model.Direction.UP) {
            return new Point2D(-1, 0);
        } else if (direction == PacMan_Model.Direction.DOWN) {
            return new Point2D(1, 0);
        } else {
            return new Point2D(0, 0);
        }
    }

    public Point2D[] moveGhost(Point2D velocity, Point2D location) {
        Random generator = new Random();
        if (!ghostEatingMode) {
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(PacMan_Model.Direction.UP);
                } else {
                    velocity = changeVelocity(PacMan_Model.Direction.DOWN);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(PacMan_Model.Direction.LEFT);
                } else {
                    velocity = changeVelocity(PacMan_Model.Direction.RIGHT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else {
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        if (ghostEatingMode) {
            if (location.getY() == pacmanLocation.getY()) {
                if (location.getX() > pacmanLocation.getX()) {
                    velocity = changeVelocity(PacMan_Model.Direction.DOWN);
                } else {
                    velocity = changeVelocity(PacMan_Model.Direction.UP);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else if (location.getX() == pacmanLocation.getX()) {
                if (location.getY() > pacmanLocation.getY()) {
                    velocity = changeVelocity(PacMan_Model.Direction.RIGHT);
                } else {
                    velocity = changeVelocity(PacMan_Model.Direction.LEFT);
                }
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            } else {
                Point2D potentialLocation = location.add(velocity);
                potentialLocation = setGoingOffscreenNewLocation(potentialLocation);
                while (grid[(int) potentialLocation.getX()][(int) potentialLocation.getY()] == PacMan_Model.CellValue.WALL) {
                    int randomNum = generator.nextInt(4);
                    PacMan_Model.Direction direction = intToDirection(randomNum);
                    velocity = changeVelocity(direction);
                    potentialLocation = location.add(velocity);
                }
                location = potentialLocation;
            }
        }
        Point2D[] data = {velocity, location};
        return data;
    }
}
