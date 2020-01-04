package com.example.ssak.data;

// Customized by SY

public class MainProductData {

    private String proName;
    private int quantity;
    private String image;
    private int originPrice;
    private int salePrice;
    private int idProduct;

    public String getImage() { return image; }

    public String getName() { return proName; }

    public int getQuantity() { return quantity; }

    public int getOriginPrice() { return originPrice; }

    public int getSalePrice() { return salePrice; }

    public int getIdProduct() { return idProduct; }

    public void setImage(String image) { this.image = image; }

    public void setName(String proName) { this.proName = proName; }

    public void setOriginPrice(int originPrice) { this.originPrice = originPrice; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void setSalePrice(int salePrice) { this.salePrice = salePrice; }

    public void setIdProduct(int idProduct) { this.idProduct = idProduct; }

}
