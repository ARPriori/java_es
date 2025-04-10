package it.priori;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class PrimaryController {

    @FXML
    private ProgressBar bar_horse1, bar_horse2, bar_horse3, bar_horse4, bar_horse5;
    @FXML
    private Button startButton;
    @FXML
    private Label winnerLabel;

    private Race race;
    private final List<ProgressBar> bars = new ArrayList<>();
    private boolean raceFinished = false;

    @FXML
    public void initialize() {
        bars.add(bar_horse1);
        bars.add(bar_horse2);
        bars.add(bar_horse3);
        bars.add(bar_horse4);
        bars.add(bar_horse5);

        resetBars();
        race = new Race(this);
    }

    @FXML
    private void startRace() {
        resetBars();
        winnerLabel.setText("");
        raceFinished = false;
        race = new Race(this);
        race.start();
        startButton.setText("Race in progress...");
        startButton.setDisable(true);
    }

    public synchronized void updateProgress(int horseId, int progress) {
        Platform.runLater(() -> bars.get(horseId).setProgress(progress / 100.0));
    }

    public synchronized void checkWinner(int horseId) {
        if (!raceFinished) {
            raceFinished = true;
            Platform.runLater(() -> {
                winnerLabel.setText("🏆 Horse " + (horseId + 1) + " wins!");
                startButton.setText("Restart Race");
                startButton.setDisable(false);
            });
        }
    }

    private void resetBars() {
        for (ProgressBar bar : bars) {
            bar.setProgress(0);
        }
    }
}
