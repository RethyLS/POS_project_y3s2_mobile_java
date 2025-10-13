package com.example.pos_project.model;

public class CartItem {
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    public CartItem(int productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice * quantity;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { 
        this.unitPrice = unitPrice;
        updateTotalPrice();
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity = quantity;
        updateTotalPrice();
    }

    public double getTotalPrice() { return totalPrice; }

    private void updateTotalPrice() {
        this.totalPrice = this.unitPrice * this.quantity;
    }
}