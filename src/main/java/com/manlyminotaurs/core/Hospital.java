package main.java.com.manlyminotaurs.core;

import main.java.com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;

import java.util.ArrayList;

public class Hospital {
    String name, id, address;
    int nodeCount;

    ArrayList allNodes = new ArrayList<Node>(1024);
    ArrayList allUsers = new ArrayList<User>(1024);


    /**
     * Add a user to the collection of Users
     * @param user User to be added
     */
    void addUser(User user){

    }

    //TODO:Add the rest of the params
    //TODO:Edit docs to reflect changes
    /**
     * Edits the indicated user
     * @param user User to be edited
     */
    void editUser(User user){

    }

    /**
     * Removes the User from the collection of users
     * @param user User to be Removed
     */
    void deleteUser(User user){

    }

    /**
     *Add the given Node to the collection of Nodes
     * @param node Node to be added
     */
    void addNode(Node node){

    }
    //TODO:Add the rest of the params
    //TODO:Edit docs to reflect changes
    /**
     * Edits the node indicated
     * @param node Node to be edited
     */
    void editNode(Node node){

    }

    /**
     * Removes the indicated node from the collection of nodes
     * @param node Node to be removed
     */
    void deleteNode(Node node){

    }
}
