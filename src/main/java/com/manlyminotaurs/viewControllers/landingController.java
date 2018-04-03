package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import com.manlyminotaurs.core.Main;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class landingController {


    private static int curActionPane = 0;

    @FXML
    Button btnLogin;

    @FXML
    Button btnHelp;

    @FXML
    Button btnNurseRequest;
    @FXML
    Button btnDirections;

    @FXML
    TableView tblUserProfile;

    @FXML
    ImageView imgMap;

    @FXML
    AnchorPane apActionBar;


    public void promptLogin(ActionEvent event){
        Main.addPrompt(0); //go to login prompt
    }

    public void nurseRequestAction(ActionEvent event){
        Main.setActionBars(0).setTranslateY(715);


    }
    public void directionAction(ActionEvent event){
        //open direction actionBar
        Main.setActionBars(1).setTranslateY(715); //go to directions
    }

    public void directoryAction(ActionEvent event) {
        // bring up directory action panels in future iterations
    }

    public void helpAction(ActionEvent event) {
        // bring up help screen in future iterations
    }

}
