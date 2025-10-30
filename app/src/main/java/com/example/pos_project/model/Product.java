package com.example.pos_project.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @SerializedName("image")
    private String image;
    
    @SerializedName("store_id")
    private int storeId;
    
    @SerializedName("category_id")
    private int categoryId;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("sku")
    private String sku;
    
    @SerializedName("slug")
    private String slug;
    
    @SerializedName("barcode")
    private String barcode;
    
    @SerializedName("price")
    private double price;
    
    @SerializedName("cost_price")
    private double costPrice;
    
    @SerializedName("tax_rate")
    private double taxRate;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("quantity")
    private int quantity;
    
    @SerializedName("discount")
    private double discount;
    
    @SerializedName("quantity_alert")
    private int quantityAlert;
    
    @SerializedName("store")
    @Ignore // Ignore for Room database
    private Store store;
    
    @SerializedName("category")
    @Ignore // Ignore for Room database
    private Category category;
    
    // For local database storage
    private String categoryName;
    private String storeName;
    private boolean isActive = true;

    // Constructors
    public Product() {}

    @Ignore
    public Product(String name, String description, double price, int quantity, String barcode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.barcode = barcode;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getCostPrice() { return costPrice; }
    public void setCostPrice(double costPrice) { this.costPrice = costPrice; }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate = taxRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public int getQuantityAlert() { return quantityAlert; }
    public void setQuantityAlert(int quantityAlert) { this.quantityAlert = quantityAlert; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { 
        this.category = category;
        // Also update the categoryName for local storage
        if (category != null) {
            this.categoryName = category.getName();
        }
    }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}