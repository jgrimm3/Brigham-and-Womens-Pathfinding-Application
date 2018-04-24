package com.manlyminotaurs.timeout;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.TimerTask;

public class ResetTask extends TimerTask {
    Stage myStage;

    public ResetTask(Stage myStage) {
        this.myStage = myStage;
    }

    @Override
    public void run() {
        if(myStage != null){
            Platform.runLater(
                    () -> {
                        Parent root;
                        //load up Home FXML document;
                        try {
                            root = FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/idleMap.fxml"));
                            //create a new scene with root and set the stage
                            Scene scene = new Scene(root);
                            myStage.setScene(scene);
                            myStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }else{
            System.out.println("Stage Not Set");
        }
    }
}
