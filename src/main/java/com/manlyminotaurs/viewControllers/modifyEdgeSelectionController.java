package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.core.Main;
import com.manlyminotaurs.databases.NodesEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class modifyEdgeSelectionController implements Initializable{

    Edge endEdge;
    String endEdgeID;
    NodesEditor edgeHome = new NodesEditor();

    @FXML
    Label lblTitle;

    @FXML
    Button btnGetCurrentEdges;

    @FXML
    TableView<tableEdge> tblEdges;

    @FXML
    Button btnDisableEdge;

    @FXML
    Button btnEnableEdge;

    @FXML
    Button btnCancel;

    @FXML
    Label lblEdgeID;

   public class tableEdge {
       String startNodeName;
       String endNodeName;
       int status;
       String edgeID;
    tableEdge(String start, String end, int status, String edgeID){
    this.startNodeName = start;
    this.endNodeName = end;
    this.status = status;
    this.edgeID = edgeID;
    }
public String getStartNodeName(){return this.startNodeName;}
public String getEndNodeName(){return this.endNodeName;}
public int getStatus(){return this.status;}
public String getEdgeID(){return this.edgeID;}
   }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn node1 = new TableColumn("Node 1");
        TableColumn node2 = new TableColumn("Node 2");
        TableColumn active = new TableColumn("Active");
        TableColumn edgeID = new TableColumn("ID");



        node1.setCellValueFactory(new PropertyValueFactory<tableEdge, String>("startNodeName"));
        node2.setCellValueFactory(new PropertyValueFactory<tableEdge, String>("endNodeName"));
        active.setCellValueFactory(new PropertyValueFactory<tableEdge, Integer>("status"));
        edgeID.setCellValueFactory(new PropertyValueFactory<tableEdge, String>("edgeID"));




        ObservableList<tableEdge> edgeList = FXCollections.observableArrayList();



        List<Edge> tempList = edgeHome.getEdgeList();

        for(Edge currEdge : tempList) {

            edgeList.add(new tableEdge (currEdge.getStartNodeName(), currEdge.getEndNodeName(), currEdge.getStatus(), currEdge.getEdgeID()));
        }

        tblEdges.setItems(edgeList);
        tblEdges.getColumns().addAll(node1, node2, active,edgeID);
        tblEdges.refresh();
    }


    public void loadEdges(ActionEvent event) {


        ObservableList<tableEdge> edgeList = FXCollections.observableArrayList();



        List<Edge> tempList = edgeHome.getEdgeList();

        for(Edge currEdge : tempList) {

            edgeList.add(new tableEdge (currEdge.getStartNodeName(), currEdge.getEndNodeName(), currEdge.getStatus(), currEdge.getEdgeID()));
        }

        tblEdges.setItems(edgeList);

        tblEdges.refresh();
    }

    public void disableEdge(ActionEvent event) {
       Edge edgeDis =  edgeHome.getEdgeFromList(tblEdges.getSelectionModel().getSelectedItem().edgeID);
       edgeHome.modifyEdgeStatus(edgeDis, 0);
       lblEdgeID.setText("Edge to Modify" + edgeDis.getEdgeID());
    }
    public void enableEdge(ActionEvent event) {
        Edge edgeEn =  edgeHome.getEdgeFromList(tblEdges.getSelectionModel().getSelectedItem().edgeID);
        edgeHome.modifyEdgeStatus(edgeEn, 1);
        lblEdgeID.setText("Edge to Modify" + edgeEn.getEdgeID());
    }
    public void cancel(ActionEvent event) {

        Main.removePrompt(10);

    }
}
