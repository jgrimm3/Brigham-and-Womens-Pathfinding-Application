package com.manlyminotaurs.viewControllers;

import com.manlyminotaurs.databases.NodesEditor;
import com.manlyminotaurs.messaging.Request;
import com.manlyminotaurs.nodes.Edge;
import com.manlyminotaurs.nodes.Node;
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

public class deleteEdgeSelectionController implements Initializable{

    @FXML
    Label lblTitle;

    @FXML
    Button btnGetCurrentEdges;

    @FXML
    TableView<Edge> tblEdges;

    @FXML
    Button btnDeleteEdge;

    @FXML
    Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn node1 = new TableColumn("Node 1");
        TableColumn node2 = new TableColumn("Node 2");
        TableColumn active = new TableColumn("Active");

        tblEdges.getColumns().addAll(node1, node2, active);

        node1.setCellValueFactory(new PropertyValueFactory<Edge, String>("startNodeName"));
        node2.setCellValueFactory(new PropertyValueFactory<Edge, String>("endNodeName"));
        active.setCellValueFactory(new PropertyValueFactory<Edge, Integer>("status"));

        NodesEditor edgeHome = new NodesEditor();

        ObservableList<Edge> edgeList = FXCollections.observableArrayList();

        List<Edge> tempList = edgeHome.getEdgeList();

        for(Edge currEdge : tempList) {
            edgeList.add(currEdge);
        }

        tblEdges.setItems(edgeList);
        tblEdges.refresh();
    }

    public void loadEdges(ActionEvent event) {
        /*reqestList = reqUtil.searchRequestBySender("user");
        System.out.println("Requests From User: " + reqestList.size());
        tblOpenRequests.getItems().clear();

        for(Request currReq : reqestList) {
            System.out.println("Type: " + currReq.getRequestType() +" Message: " + msgUtil.getMessageFromList(currReq.getMessageID()).getMessage());
            tblOpenRequests.getItems().add(new userNrActionBarController.requestInfo(currReq.getRequestType(), msgUtil.getMessageFromList(currReq.getMessageID()).getMessage(), currReq.getRequestID()));
        }

        System.out.println("Requests In List: " + finalList.size());

        tblOpenRequests.refresh(); */
    }

}
