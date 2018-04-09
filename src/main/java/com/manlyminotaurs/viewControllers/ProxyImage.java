package com.manlyminotaurs.viewControllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProxyImage {

    private ImageView imageView;
    private Image image;
    private String fileName;

    public ProxyImage(ImageView imageView, String fileName){
        this.imageView = imageView;
        this.fileName = fileName;
    }

    public void display() {
        File file = new File(ProxyImage.class.getResource("/MapImages/"+fileName).getFile());
        image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

}
