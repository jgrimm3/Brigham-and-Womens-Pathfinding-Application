package com.manlyminotaurs.core;

import com.manlyminotaurs.nodes.Node;
import com.manlyminotaurs.users.User;

import java.util.ArrayList;

public class Hospital {
    String name, id, address;
    int nodeCount;

    ArrayList allNodes = new ArrayList<Node>(1024);
    ArrayList allUsers = new ArrayList<User>(1024);
}
