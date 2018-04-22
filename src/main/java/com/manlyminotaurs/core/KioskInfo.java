package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.timeout.ResetTask;
import javafx.event.EventHandler;
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

    public static void setCurrentUserID(String currentUserID) {
        KioskInfo.currentUserID = currentUserID;
    }

    public static String currentUserID;

    public static Timer myTimer;
    public static Node myLocation;
    public static int myDelay = 3000;
    public static Stage myStage;

    public static EventHandler<MouseEvent> myHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(myTimer != null){
                myTimer.cancel();
            }
            myTimer = new Timer();
            myTimer.schedule(new ResetTask(myStage), myDelay);
        }
    };
}
