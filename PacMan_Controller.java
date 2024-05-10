package pacman_game;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;

import java.util.*;


public class PacMan_Controller implements EventHandler<KeyEvent> {
    final private static double FRAMES_PER_SECOND = 5.0;
    /**
     * Setter spillnivåer i .txt-filer og leser dem en etter en når nivået går opp.
     */
    private static final String[] levelFiles = {"src/pacman_levels/level1.txt", "src/pacman_levels/level2.txt", "src/pacman_levels/level3.txt"};
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    /**
     * Bruker imageView for å vise antall liv, game over-meldingen og du vant-meldingen.
     */
    @FXML
    private ImageView imageView;
    @FXML
    private PacMan_View pacManView;
    /**
     * life_count 0 har 3 liv
     * life_count 1 har 2 liv
     * life_count 2 har 1 liv
     * life_count 3 har ikke flere liv
     */
    private int life_count = 0;
    private PacMan_Model pacManModel;
    private Timer timer;
    private static int ghostEatingModeCounter;
    private boolean paused;

    public PacMan_Controller() {
        /**
         * paused false betyr at spillet ikke har startet ennå
         * true betyr at spillet har startet
         * Brukes når spillet ikke starter automatisk, men starter når en pil opp, ned, venstre eller høyre trykkes på.
         */
        this.paused = false;
    }

    /**
     * Initialiserer og oppdaterer modellen og visningen fra den første txt-filen og starter timeren.
     */
    public void initialize() throws InterruptedException {
        this.pacManModel = new PacMan_Model();
        this.update_directions(PacMan_Model.Direction.NONE);
        ghostEatingModeCounter = 25;
        life_count = 0;
        this.startTimer();
        /**
         Når spillet starter, har det tre livscount for å spille spillet.
         */
        if (life_count == 0) {
            Image image = new Image("pacman_images/three-life.png");
            imageView.setImage(image);
        }
    }
    /**
     * Pacman_Model for å oppdatere basert på timeren
     */
    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            update_directions(PacMan_Model.getCurrentDirection());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        long frameTimeInMilliseconds = (long) (1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
        life_count = 0;
    }
    /**
     * Oppdaterer visningen, oppdaterer poeng og nivå, viser livstelleren, viser Game Over/You Won-meldinger.
     */
    private void update_directions(PacMan_Model.Direction direction) throws InterruptedException {
        this.pacManModel.step(direction);
        try {
            this.pacManView.update(pacManModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Oppdater spillnivået og poengsummen herfra
         */
        this.levelLabel.setText(String.format("Level: %d", this.pacManModel.getLevel()));
        this.scoreLabel.setText(String.format("Score: %d", this.pacManModel.getScore()));
        /**
         * Sjekk om spillet er over eller ikke for å fortsette spillet eller vise meldinger og livsteller..................
         */
        if (PacMan_Model.isGameOver()) {
            pacManModel.life();
            life_count++;
            if (life_count == 2) {
                Image image = new Image("pacman_images/one-life.png");
                imageView.setImage(image);
            }
            if (life_count == 1) {
                Image image = new Image("pacman_images/two-life.png");
                imageView.setImage(image);
            }
            if (life_count == 0) {
                Image image = new Image("pacman_images/three-life.png");
                imageView.setImage(image);
            }
            if (life_count == 3) {
                Image image = new Image("pacman_images/game-over.png");
                imageView.setImage(image);
                pause();
            }
        }
        if (PacMan_Model.isYouWon()) {
            Image image = new Image("pacman_images/you-won.png");
            imageView.setImage(image);
        }
        /**
         * ghostEatingMode / tell ned ghostEatingModeCounter for å tilbakestille ghostEatingMode til false
         * når telleren er 0
         */
        if (PacMan_Model.isGhostEatingMode()) {
            ghostEatingModeCounter--;
        }
        if (ghostEatingModeCounter == 0 && PacMan_Model.isGhostEatingMode()) {
            PacMan_Model.setGhostEatingMode(false);
        }
    }

    /**
    *  denne metoden håndterer tastaturhandlinger når tastene trykkes
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        boolean keyRecognized = true;
        KeyCode code = keyEvent.getCode();
        PacMan_Model.Direction direction = PacMan_Model.Direction.NONE;
        /**
         * når venstre piltast trykkes, går pac man til venstre
         */
        if (code == KeyCode.LEFT) {
            direction = PacMan_Model.Direction.LEFT;
            /**
             * når høyre piltast trykkes, går pac man til høyre
             */
        } else if (code == KeyCode.RIGHT) {
            direction = PacMan_Model.Direction.RIGHT;
            /**
             * når opp piltast trykkes, går pac man oppover
             */
        } else if (code == KeyCode.UP) {
            direction = PacMan_Model.Direction.UP;
            /**
             * når ned piltast trykkes, går pac man nedover
             */
        } else if (code == KeyCode.DOWN) {
            direction = PacMan_Model.Direction.DOWN;
            /**
             * når s-tasten trykkes, kan vi starte et nytt spill
             * du kan endre den ved å erstatte denne S, du kan sette inn en annen bokstav
             */
        } else if (code == KeyCode.S) {
            /**
             * når s-tasten trykkes, sett tre livsteller
             */
            Image image = new Image("pacman_images/three-life.png");
            imageView.setImage(image);
            pause();
            this.pacManModel.startNewGame();
            paused = false;
            this.startTimer();
        } else {
            keyRecognized = false;
        }
        if (keyRecognized) {
            keyEvent.consume();
            pacManModel.setCurrentDirection(direction);
        }
    }
    public static void setGhostEatingModeCounter() {
        ghostEatingModeCounter = 25;
    }

    public static int getGhostEatingModeCounter() {
        return ghostEatingModeCounter;
    }

    public static String getLevelFile(int x) {
        return levelFiles[x];
    }

    public boolean getPaused() {
        return paused;
    }
    public void pause() {
        this.timer.cancel();
        this.paused = true;
    }

    public double getBoardWidth() {
        return PacMan_View.CELL_WIDTH * this.pacManView.getColumnCount();
    }

    public double getBoardHeight() {
        return PacMan_View.CELL_WIDTH * this.pacManView.getRowCount();
    }
}
