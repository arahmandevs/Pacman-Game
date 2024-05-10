package pacman_game;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pacman_game.PacMan_Model.CellValue;

public class PacMan_View extends Group {

    public final static double CELL_WIDTH = 20.0;
    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
    private Image pacmanRightImage,pacmanUpImage,pacmanDownImage,pacmanLeftImage;
    private Image ghost1Image,ghost2Image,ghost3Image,ghost4Image;
    private Image blueGhostImage,wallImage,bigDotImage,smallDotImage;
    /**
     * Initialiserer verdiene til bildevariablene fra filer
     */
    public PacMan_View() {
        this.ghost1Image = new Image(getClass().getResourceAsStream("/pacman_images/ghost1.gif"));
        this.ghost2Image = new Image(getClass().getResourceAsStream("/pacman_images/ghost2.gif"));
        this.ghost3Image = new Image(getClass().getResourceAsStream("/pacman_images/ghost3.gif"));
        this.ghost4Image = new Image(getClass().getResourceAsStream("/pacman_images/ghost4.gif"));
        this.blueGhostImage = new Image(getClass().getResourceAsStream("/pacman_images/blueghost.gif"));

        this.pacmanRightImage = new Image(getClass().getResourceAsStream("/pacman_images/pacmanRight.gif"));
        this.pacmanUpImage = new Image(getClass().getResourceAsStream("/pacman_images/pacmanUp.gif"));
        this.pacmanDownImage = new Image(getClass().getResourceAsStream("/pacman_images/pacmanDown.gif"));
        this.pacmanLeftImage = new Image(getClass().getResourceAsStream("/pacman_images/pacmanLeft.gif"));

        this.wallImage = new Image(getClass().getResourceAsStream("/pacman_images/wall.png"));
        this.bigDotImage = new Image(getClass().getResourceAsStream("/pacman_images/whitedot.png"));
        this.smallDotImage = new Image(getClass().getResourceAsStream("/pacman_images/smalldot.png"));
    }

    /**
     * Konstruerer et tomt rutenett av ImageView-er
     */
    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    /** Oppdaterer visningen for å gjenspeile tilstanden til modellen
     */
    public void update(PacMan_Model model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        for (int row = 0; row < this.rowCount; row++){
            for (int column = 0; column < this.columnCount; column++){
                CellValue value = model.getCellValue(row, column);
                if (value == CellValue.WALL) {
                    this.cellViews[row][column].setImage(this.wallImage);
                }
                else if (value == CellValue.BIGDOT) {
                    this.cellViews[row][column].setImage(this.bigDotImage);
                }
                else if (value == CellValue.SMALLDOT) {
                    this.cellViews[row][column].setImage(this.smallDotImage);
                }
                else {
                    this.cellViews[row][column].setImage(null);
                }
                if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && (PacMan_Model.getLastDirection() == PacMan_Model.Direction.RIGHT || PacMan_Model.getLastDirection() == PacMan_Model.Direction.NONE)) {
                    this.cellViews[row][column].setImage(this.pacmanRightImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacMan_Model.getLastDirection() == PacMan_Model.Direction.LEFT) {
                    this.cellViews[row][column].setImage(this.pacmanLeftImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacMan_Model.getLastDirection() == PacMan_Model.Direction.UP) {
                    this.cellViews[row][column].setImage(this.pacmanUpImage);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacMan_Model.getLastDirection() == PacMan_Model.Direction.DOWN) {
                    this.cellViews[row][column].setImage(this.pacmanDownImage);
                }
                if (PacMan_Model.isGhostEatingMode() && (PacMan_Controller.getGhostEatingModeCounter() == 6 || PacMan_Controller.getGhostEatingModeCounter() == 4 || PacMan_Controller.getGhostEatingModeCounter() == 2)) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                    if (row == model.getGhost3Location().getX() && column == model.getGhost3Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost3Image);
                    }
                    if (row == model.getGhost4Location().getX() && column == model.getGhost4Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost4Image);
                    }
                }
                else if (PacMan_Model.isGhostEatingMode()) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhostImage);
                    }
                }
                else {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1Image);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2Image);
                    }
                    if (row == model.getGhost3Location().getX() && column == model.getGhost3Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost3Image);
                    }
                    if (row == model.getGhost4Location().getX() && column == model.getGhost4Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost4Image);
                    }
                }
            }
        }
    }
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }
    public int getRowCount() {
        return this.rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }
    public int getColumnCount() {
        return this.columnCount;
    }
}
