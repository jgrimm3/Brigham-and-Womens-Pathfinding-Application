package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import com.manlyminotaurs.core.Main;

import java.util.ArrayList;
import java.util.List;

public class landingController {
 public  List<Pane> actionBars = new ArrayList<Pane>();


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

    public  void setActionBar(int indxAction){
       apActionBar.getChildren().remove(actionBars.get(curActionPane));
        apActionBar.getChildren().add(actionBars.get(indxAction));
        curActionPane = indxAction;
    }

    public void promptLogin(ActionEvent event){
        Main.addPrompt(0); //go to login prompt
    }

    public void nurseRequestAction(ActionEvent event){
       // setActionBar(0);

    }

}
