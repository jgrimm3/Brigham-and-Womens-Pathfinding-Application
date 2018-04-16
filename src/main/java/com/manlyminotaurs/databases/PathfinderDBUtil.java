package com.manlyminotaurs.databases;

public class PathfinderDBUtil {

    private static int pathFinderIDCounter = 0;

    public static void setPathFinderIDCounter(int logIDCounter) {
        PathfinderDBUtil.pathFinderIDCounter = pathFinderIDCounter;
    }

    private String generateLogID(){
        pathFinderIDCounter++;
        return Integer.toString(pathFinderIDCounter);
    }


    //retrieve pathfindings

    //Add pathfinding

    //remove pathfinding

    //get pathfinding by IDs
}
