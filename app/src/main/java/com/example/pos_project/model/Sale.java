package com.example.pos_project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales")
public class Sale {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String saleDate;
    private double totalAmount;
    private double paidAmount;
    private double changeAmount;
    private String paymentMethod; // "cash", "card", "digital"
    private int userId; // ID of the user who made the sale
    private String customerName;

    // Constructors
    public Sale() {}

    public Sale(String saleDate, double totalAmount, double paidAmount, 
                String paymentMethod, int userId, String customerName) {
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.changeAmount = paidAmount - totalAmount;
        this.paymentMethod = paymentMethod;
        this.userId = userId;
        this.customerName = customerName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSaleDate() { return saleDate; }
    public void setSaleDate(String saleDate) { this.saleDate = saleDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }

    public double getChangeAmount() { return changeAmount; }
    public void setChangeAmount(double changeAmount) { this.changeAmount = changeAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}