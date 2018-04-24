package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.timeout.ResetTask;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Timer;

public class KioskInfo {
    public static Node getMyLocation() {
        return myLocation;
    }

    public static void setMyLocation(Node myLocation) {
        KioskInfo.myLocation = myLocation;
    }

    public static String getCurrentUserID() {
        return currentUserID;
    }

    public static String currentUserID;

    public static Timer myTimer;
    public static Node myLocation;
    public static int myDelay = 15000;
    public static Stage myStage;

    public static EventHandler<InputEvent> myHandler = new EventHandler<InputEvent>() {
        @Override
        public void handle(InputEvent event) {
            if(myTimer != null){
                myTimer.cancel();
            }
            myTimer = new Timer();
            myTimer.schedule(new ResetTask(myStage), myDelay);
        }
    };
}
