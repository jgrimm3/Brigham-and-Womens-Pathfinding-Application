package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import com.manlyminotaurs.core.Main;
import javafx.scene.layout.Pane;

public class adminHomeController {
    @FXML
    Button btnProfile;

    @FXML
    ImageView imgMap;

    @FXML
    Button btnEditNodes;

    @FXML
    Button btnMngReq;

    @FXML
    Button btnLogout;

    public void logOut(ActionEvent event){
        //logout and go back to landing page

        Main.setScreen(1); //go to landing screen
    }

    public void editNodesAction(ActionEvent event){
        Pane action = Main.setActionBars(2);
        action.setTranslateY(715);
        action.setTranslateX(85);

    }
    public void manageRequestsAction(ActionEvent event){
        //open direction actionBar
        Pane action = Main.setActionBars(3);
        action.setTranslateY(715);
        action.setTranslateX(85);
    }
}


