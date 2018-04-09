
package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.manlyminotaurs.databases.NodesDBUtil;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class nodeEditorController {

    @FXML
    Path path;
    @FXML
    Button btnMenuAdd;
    @FXML
    Button btnModifyNode;
    @FXML
    Button btnDeleteNode;
    @FXML
    Pane paneAdd;
    @FXML
    Pane paneModify;
    @FXML
    Pane paneDelete;
    @FXML
    ComboBox cmboBuilding;
    @FXML
    ComboBox cmboType;
    @FXML
    ComboBox cmboBuildingMod;
    @FXML
    ComboBox cmboTypeMod;
    @FXML
    ComboBox cmboNodeMod;
    @FXML
    ComboBox cmboBuildingDel;
    @FXML
    ComboBox cmboTypeDel;
    @FXML
    ComboBox cmboNodeDel;
    @FXML
    JFXTextField txtShortName;
    @FXML
    JFXTextField txtLongName;
    @FXML
    JFXTextField txtXCoord;
    @FXML
    JFXTextField txtYCoord;
    @FXML
    JFXTextField txtShortNameMod;
    @FXML
    JFXTextField txtLongNameMod;
    @FXML
    JFXTextField txtXCoordMod;
    @FXML
    JFXTextField txtYCoordDel;
    @FXML
    JFXTextField txtShortNameDel;
    @FXML
    JFXTextField txtLongNameDel;
    @FXML
    JFXTextField txtXCoordDel;
    @FXML
    JFXToggleButton tglGeofence;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Button btnLogOut;

    String newShortName;
    String newLongName;
    String modShortName;
    String modLongName;



    //Swap active panes
    public void displayModifyPane(ActionEvent event){   //modify node
     paneAdd.setVisible(false);
     paneDelete.setVisible((false));
     paneAdd.setDisable(true);
     paneDelete.setDisable(true);
     paneModify.setDisable(false);
     paneModify.setVisible(true);

  }
    public void displayAddPane(ActionEvent event){   //add Node
        paneDelete.setVisible((false));
        paneDelete.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneAdd.setDisable(false);
        paneAdd.setVisible(true);

    }

    public void displayDeletePane(ActionEvent event){   //delete Node
        paneAdd.setVisible(false);
        paneAdd.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneDelete.setDisable(false);
        paneDelete.setVisible((true));
    }

    @FXML
    protected void initialize() {
        System.out.println("initializing");
        scrollPane.setVvalue(0.65);
        scrollPane.setHvalue(0.25);
        path.setStrokeWidth(5);
        //printPoints("L2");
    }

//logout and return to the home screen
    public void logOut(ActionEvent event) {


    }

}