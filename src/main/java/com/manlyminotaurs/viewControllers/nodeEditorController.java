
package com.manlyminotaurs.viewControllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import com.manlyminotaurs.nodes.Node;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchPoint;
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
    final double NODEXMIN = 0;
    final double NODEXMAX = 5000;
    final double NODEYMIN = 0;
    final double NODEYMAX = 2774;
    final double NEWMAPXMIN = 0;
    final double NEWMAPXMAX = 1250;
    final double NEWMAPYMIN = 0;
    final double NEWMAPYMAX = 693.8;
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
    ComboBox<String> cmboBuilding;
    @FXML
    ComboBox<String> cmboType;
    @FXML
    ComboBox<String> cmboBuildingMod;
    @FXML
    ComboBox<String> cmboTypeMod;
    @FXML
    ComboBox<String> cmboNodeMod;
    @FXML
    ComboBox<String> cmboBuildingDel;
    @FXML
    ComboBox<String> cmboTypeDel;
    @FXML
    ComboBox<String> cmboNodeDel;
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
    JFXTextField txtYCoordMod;
    @FXML
    JFXTextField txtYCoordDel;
    @FXML
    JFXTextField txtShortNameDel;
    @FXML
    JFXTextField txtLongNameDel;
    @FXML
    JFXTextField txtXCoordDel;
    @FXML
    ToggleButton tglGeofence;
    @FXML
    ComboBox<String> cmboFloor;
    @FXML
    ComboBox <String> cmboFloorAdd;
    @FXML
    ComboBox<String> cmboFloorDel;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Button btnLogOut;
    @FXML
    Button btnAddNode;
    @FXML
    Button btnModify;

    final static ObservableList<String> types = FXCollections.observableArrayList("HALL", "ELEV", "REST", "STAI", "DEPT", "LABS", "INFO", "CONF", "EXIT", "RETL", "SERV");
    final static ObservableList<String> floors = FXCollections.observableArrayList("L2", "L1", "1","2","3");
    final static ObservableList<String> buildings = FXCollections.observableArrayList("Shapiro", "Jank", "Somerset");
    final static ObservableList<String> locations = FXCollections.observableArrayList("thePlace", "Jerry's house", "another place", "wong's house", "fdskjfas", "fsdfds", "Dfsd","sfdd","SFd");

    String longName;
    String shortName;
    String type;
    String floor;
    String building;
    String node;
    String xCoord;
    String yCoord;



    //Swap active panes
    public void displayModifyPane(ActionEvent event){   //modify node
     paneAdd.setVisible(false);
     paneDelete.setVisible((false));
     paneAdd.setDisable(true);
     paneDelete.setDisable(true);
     paneModify.setDisable(false);
     paneModify.setVisible(true);

        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        txtLongNameDel.clear();
        txtShortNameDel.clear();
        txtXCoordDel.clear();
        txtYCoordDel.clear();

        BooleanBinding booleanBind = Bindings.or(txtYCoordMod.textProperty().isEmpty(),
                txtXCoordMod.textProperty().isEmpty()).or(txtShortNameMod.textProperty().isEmpty()).or(txtLongNameMod.textProperty().isEmpty());
        btnModify.disableProperty().bind(booleanBind);
  }
    public void displayAddPane(ActionEvent event){   //add Node
        paneDelete.setVisible((false));
        paneDelete.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneAdd.setDisable(false);
        paneAdd.setVisible(true);

        //clear other panes children

        txtLongNameMod.clear();
        txtShortNameMod.clear();
        txtXCoordMod.clear();
        txtYCoordMod.clear();
        txtLongNameDel.clear();
        txtShortNameDel.clear();
        txtXCoordDel.clear();
        txtYCoordDel.clear();
        BooleanBinding booleanBind = Bindings.or(txtYCoord.textProperty().isEmpty(),
                txtXCoord.textProperty().isEmpty()).or(txtShortName.textProperty().isEmpty()).or(txtLongName.textProperty().isEmpty());
        btnAddNode.disableProperty().bind(booleanBind);



        //set up comboboxes
        cmboBuilding.setItems(buildings);

    }

    public void displayDeletePane(ActionEvent event){   //delete Node
        paneAdd.setVisible(false);
        paneAdd.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);

        paneDelete.setDisable(false);
        paneDelete.setVisible((true));

        txtLongNameDel.setEditable(true);
        txtShortNameDel.setEditable(true);


        txtLongName.clear();
        txtShortName.clear();
        txtXCoord.clear();
        txtYCoord.clear();
        txtLongNameMod.clear();
        txtShortNameMod.clear();
        txtXCoordMod.clear();
        txtYCoordMod.clear();

        BooleanBinding booleanBind = Bindings.or(txtYCoordDel.textProperty().isEmpty(),
                txtXCoordDel.textProperty().isEmpty()).or(txtShortNameDel.textProperty().isEmpty()).or(txtLongNameDel.textProperty().isEmpty());
        btnDeleteNode.disableProperty().bind(booleanBind);
    }

    @FXML
    protected void initialize() {

        System.out.println("initializing");
        paneDelete.setVisible((false));
        paneDelete.setDisable(true);
        paneModify.setVisible(false);
        paneModify.setDisable(true);
        paneAdd.setDisable(false);
        paneAdd.setVisible(true);
        btnAddNode.setDisable(true);
        BooleanBinding booleanBind = Bindings.or(txtYCoord.textProperty().isEmpty(),
                txtXCoord.textProperty().isEmpty()).or(txtShortName.textProperty().isEmpty()).or(txtLongName.textProperty().isEmpty());
        btnAddNode.disableProperty().bind(booleanBind);

        cmboBuilding.setItems(buildings);

        scrollPane.setVvalue(0.65);
        scrollPane.setHvalue(0.25);
        path.setStrokeWidth(5);
        //printPoints("L2");
    }
    public void getXandY(MouseEvent event) throws Exception{
        //see which pane is visible and set the corresponding x and y coordinates
        if (paneAdd.isVisible() == true){
            txtXCoord.setText(String.format("%1.3f" , event.getX()));
            txtYCoord.setText(String.format("%1.3f" , event.getY()));
        }
        else if (paneModify.isVisible() == true){
            txtXCoordMod.setText(String.format("%1.3f" , event.getX()));
            txtYCoordMod.setText(String.format("%1.3f" , event.getY()));
        }
        else if(paneDelete.isVisible() == true){
            txtXCoordDel.setText(String.format("%1.3f" ,  event.getX()));
            txtYCoordDel.setText(String.format("%1.3f" , event.getY()));
        }
    }


//logout and return to the home screen
    public void logOut(ActionEvent event) throws Exception{
        try{
            Stage stage;
            Parent root;
            //get reference to the button's stage
            stage=(Stage)btnLogOut.getScene().getWindow();
            //load up Home FXML document
            root=FXMLLoader.load(getClass().getClassLoader().getResource("FXMLs/home.fxml"));

            //create a new scene with root and set the stage
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();}
    }

    //Combo Box selected update next
    //Add node
    public void addSetBuilding(ActionEvent event){
        building = cmboBuilding.getValue().toString();
        cmboFloorAdd.setItems(floors);

    }
    public void addSetFloor(ActionEvent event){
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorAdd.getValue().toString();
        cmboType.setItems(types);

    }
    public void addSetType(ActionEvent event){
        //set type to selected value
        type = cmboType.getValue().toString();
    }

    //modify node
    public void modSetBuilding(ActionEvent event){
        building = cmboBuildingMod.getValue().toString();
        cmboFloorAdd.setItems(floors);

    }
    public void modSetFloor(ActionEvent event){
        //set floor to selected value, use new value to populate Types
        floor = cmboFloor.getValue().toString();
        cmboType.setItems(types);

    }
    public void modSetType(ActionEvent event){
        //set type to selected value
        type = cmboTypeMod.getValue().toString();
        cmboNodeMod.setItems(null);

    }
    public void modSetNode(ActionEvent event){
        //set type to selected value
        type = cmboNodeMod.getValue().toString();

    }

    //delete node
    public void delSetBuilding(ActionEvent event){
        building = cmboBuildingDel.getValue().toString();
        cmboFloorAdd.setItems(floors);

    }
    public void delSetFloor(ActionEvent event){
        //set floor to selected value, use new value to populate Types
        floor = cmboFloorDel.getValue().toString();
        cmboType.setItems(types);

    }
    public void delSetType(ActionEvent event){
        //set type to selected value
        type = cmboTypeDel.getValue().toString();
        cmboNodeMod.setItems(null);

    }
    public void delSetNode(ActionEvent event){
        //set type to selected value
        type = cmboNodeDel.getValue().toString();
    }





    //Add Node
    public void addNode(ActionEvent event){

        longName = txtLongName.getText();
        shortName = txtShortName.getText();
        xCoord = txtXCoord.getText();
        yCoord = txtYCoord.getText();
        building = cmboBuilding.getValue().toString();
        floor = cmboFloorAdd.getValue().toString();
        type = cmboType.getValue().toString();
        //call add node function

        //redraw map

    }


    //modify node
    public void modifyNode(ActionEvent event){
        longName = txtLongNameMod.getText();
        shortName = txtShortNameMod.getText();
        xCoord = txtXCoordMod.getText();
        yCoord = txtYCoordMod.getText();
        building = cmboBuildingMod.getValue().toString();
        floor = cmboFloor.getValue().toString();
        type = cmboTypeMod.getValue().toString();

        //call modify node function

        //redraw map
    }


    //delete ode
    public void deleteNode(ActionEvent event){
        longName = txtLongNameDel.getText();
        shortName = txtShortNameDel.getText();
        xCoord = txtXCoordDel.getText();
        yCoord = txtYCoordDel.getText();
        building = cmboBuildingDel.getValue().toString();
        floor = cmboFloorDel.getValue().toString();
        type = cmboTypeDel.getValue().toString();

        //call delete node function

        //redraw map

        }

}
