package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;


public class requestDashboardController {
    @FXML
    TableView tblOpenRequests;
    @FXML
    Button btnCompleteRequest;
    @FXML
    Button btnDeleteRequest;
    @FXML
    TableView tblClosedRequests;
    @FXML
    Label lblRequestDetails;

}