package com.manlyminotaurs.timeertasks;

public class Memento {
    String state;

    public Memento(String state){
        this.state = state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}
