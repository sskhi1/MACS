package com.example.basicstorewebsite;

public class Product {
    private String id;
    private String name;
    private String imageFile;
    private double price;

    public Product(String id, String name, String imageFile, double price) {
        this.id = id;
        this.name = name;
        this.imageFile = imageFile;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageFile() {
        return imageFile;
    }

    public double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
