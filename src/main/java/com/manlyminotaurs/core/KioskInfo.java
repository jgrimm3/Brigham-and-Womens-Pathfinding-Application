package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.timeertasks.ResetTask;
import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.prefs.Preferences;

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
    public static Stage myStage;

    private static Preferences pref = Preferences.userRoot().node(Main.class.getName());

    public static EventHandler<InputEvent> myHandler = new EventHandler<InputEvent>() {
        @Override
        public void handle(InputEvent event) {
            if(myTimer != null){
                myTimer.cancel();
            }
            myTimer = new Timer();
            myTimer.schedule(new ResetTask(myStage), pref.getInt("DelayTime", 15000));
        }
    };
}
