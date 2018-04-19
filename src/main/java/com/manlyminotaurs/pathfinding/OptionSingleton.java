package com.manlyminotaurs.pathfinding;

public class OptionSingleton {
    private static OptionSingleton optionPicker;

    public PathfindingContext pathfindingContext = new PathfindingContext();
    public Boolean handicap;
    //ratio of  map pixel to a real life meter
    public final double meterPerPixel = 0.099914;
    public final double feetPerPixel = 0.3278;
    //average walking speed in meters per second
    public final double walkSpeed = 1.4;
    public final double walkSpeedFt = 4.593176;

    private OptionSingleton(){

    }

    private static class SingletonHelper{
        private static final OptionSingleton optionPicker = new OptionSingleton();
    }

    public static OptionSingleton getOptionPicker(){
        return SingletonHelper.optionPicker;
    }



}
