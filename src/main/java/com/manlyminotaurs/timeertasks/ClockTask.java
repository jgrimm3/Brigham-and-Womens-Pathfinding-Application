package com.manlyminotaurs.timeertasks;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

import static com.manlyminotaurs.viewControllers.idleMapController.clocktime;

public class ClockTask extends TimerTask {

    public ClockTask() {

    }

    @Override
    public void run() {
        Platform.runLater(
                () -> {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm");
                    clocktime.set(timeFmt.format(now));

                }
        );
    }
}
