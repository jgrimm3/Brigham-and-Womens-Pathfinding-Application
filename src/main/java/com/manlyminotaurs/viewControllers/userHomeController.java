package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import com.manlyminotaurs.core.Main;

public class userHomeController {
    @FXML
    Button btnMakeRequest;
    @FXML
    Button btnDirections;
    @FXML
    TableView tblProfileInfo;
    @FXML
    Button btnLogout;

    public void logOut(ActionEvent event){
        //logout and go back to landing page

        Main.setScreen(1); //go to landing screen
    }

    public void nurseRequestAction(ActionEvent event){
        Main.setActionBars(0).setTranslateY(715);


    }
    public void directionAction(ActionEvent event){
        //open direction actionBar
        Main.setActionBars(1).setTranslateY(715); //go to directionss

    }
}

