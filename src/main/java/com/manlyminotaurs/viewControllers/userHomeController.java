package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import com.manlyminotaurs.core.Main;
import javafx.scene.layout.Pane;

public class userHomeController {
    @FXML
    Button btnMakeRequest;
    @FXML
    Button btnDirections;
    @FXML
    TableView tblProfileInfo;
    @FXML
    Button btnLogout;

    /**
     * Log a user out of their kiosk session and return to the common landing screen
     * @param event btnLogout pressed
     */
    public void logOut(ActionEvent event){
        //logout and go back to landing page

        Main.setScreen(1); //go to landing screen
    }

    /**
     * Start a service request
     * @param event btnMakeRequest pressed
     */
    public void nurseRequestAction(ActionEvent event){
        Pane action = Main.setActionBars(0);
        action.setTranslateY(735);
        action.setTranslateX(85);


    }

    /**
     * Open directions actionBar item
     * @param event btnDirections pressed
     */
    public void directionAction(ActionEvent event){
        //open direction actionBar
        Pane action = Main.setActionBars(1);
        action.setTranslateY(755);
        action.setTranslateX(85); //go to directionss

    }
}

