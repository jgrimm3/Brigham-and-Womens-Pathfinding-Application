package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.DataModelI;
import com.manlyminotaurs.nodes.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class reactiveTextController implements Initializable{
    ArrayList<String> allNames = new ArrayList<>();
    ArrayList<Node> allNodes = new ArrayList<>(DataModelI.getInstance().retrieveNodes());
    int oldCount = 1;

    @FXML
    TextField inputBox;

    @FXML
    Label outputTxt;

    @FXML
    public void launchThing(ActionEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Node node:allNodes){
            allNames.add(node.getLongName());
        }

        inputBox.textProperty().addListener(observable -> {
            System.out.println("User Typed" + inputBox.getText().length());
            String input = inputBox.getText();
            ArrayList<String> names = new ArrayList<>();
            String text = "";
            //check current names
            if(oldCount < inputBox.getText().length()){
                for(String name: outputTxt.getText().split("\n")){
                    if(name.toLowerCase().contains(input.toLowerCase()  )){
                        System.out.println("Match found: " + name);
                        names.add(name);
                    }
                }
            }else {
                //check all names
                for(String name : allNames){
                    if(name.toLowerCase().contains(input.toLowerCase()  )){
                        System.out.println("Match found: " + name);
                        names.add(name);
                    }
                }

            }

            //merge all names
            for(String name : names){
                text += name + '\n';
            }
            oldCount = outputTxt.getText().length();
            outputTxt.setText(text);
        });
    }
}
