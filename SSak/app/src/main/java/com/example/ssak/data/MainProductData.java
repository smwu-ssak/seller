package com.example.ssak.data;

// Customized by MS

public class MainProductData {

    private int image;
    private String name;
    private int quantity;
    private int originPrice;
    private int salePrice;

    public MainProductData(int image, String name, int quantity, int originPrice, int salePrice) {
        this.image = image;
        this.name = name;
        this.quantity = quantity;
        this.originPrice = originPrice;
        this.salePrice = salePrice;
    }

    public int getImage() { return image; }

    public String getName() { return name; }

    public int getQuantity() { return quantity; }

    public int getOriginPrice() { return originPrice; }

    public int getSalePrice() { return salePrice; }

    public void setImage(int image) { this.image = image; }

    public void setName(String proName) { this.name = proName; }

    public void setOriginPrice(int originPrice) { this.originPrice = originPrice; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setSalePrice(int salePrice) { this.salePrice = salePrice; }

}
