package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;

import javax.swing.text.html.ImageView;
import java.awt.event.ActionEvent;


public class createRequestController {
    @FXML
    ScrollPane scrollPane;

    @FXML
    ImageView mapImg;

    @FXML
    Pane pane;

    @FXML
    Path path;

    @FXML
    Pane paneAdd;

    @FXML
    ComboBox cmboBuilding;

    @FXML
    Button btnSubmitRequest;

    @FXML
    ComboBox cmboType;

    @FXML
    ComboBox cmboNode;

    @FXML
    CheckBox chkHighPriority;
    @FXML
    CheckBox chkMedPriority;
    @FXML
    CheckBox chkLowPriority;
    @FXML
    Button btnLogout;

    String requestType;
    String message;
    int priority;

    public void submitRequest(javafx.event.ActionEvent event){

    }



}