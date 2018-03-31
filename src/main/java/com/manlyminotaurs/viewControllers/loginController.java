package com.manlyminotaurs.viewControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.manlyminotaurs.core.Main;

public class loginController {
    @FXML
    Button btnLogin;

    @FXML
    TextField txtUser;

    @FXML
    PasswordField txtPassword;

    public void login(ActionEvent event){


        txtUser.clear();
        txtPassword.clear();
        //if admin account

        Main.setScreen(4); //go to landing screen
    }

}
