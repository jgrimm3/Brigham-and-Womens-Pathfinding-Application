package com.manlyminotaurs.viewControllers;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProxyImage {

    private ImageView imageView;
    private Image image;
    private String fileName;

    public ProxyImage(ImageView imageView, String fileName){
        this.imageView = imageView;
        this.fileName = fileName;
    }

    public void display() {
        imageView.setImage(new Image("/MapImages/" +fileName));
    }

    public void displayIcon() {
        imageView.setImage(new Image("/Icons/" +fileName));
    }

    public void display2() {
        imageView.setImage(new Image("/QR/"+fileName));
    }

}