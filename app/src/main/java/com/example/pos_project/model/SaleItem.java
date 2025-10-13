package com.example.pos_project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sale_items")
public class SaleItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int saleId;
    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    // Constructors
    public SaleItem() {}

    public SaleItem(int saleId, int productId, String productName, 
                    double unitPrice, int quantity) {
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice * quantity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}