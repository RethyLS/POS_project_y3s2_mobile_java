package com.example.pos_project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pos_project.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products WHERE isActive = 1")
    List<Product> getAllActiveProducts();

    @Query("SELECT * FROM products WHERE id = :id")
    Product getProductById(int id);

    @Query("SELECT * FROM products WHERE barcode = :barcode AND isActive = 1")
    Product getProductByBarcode(String barcode);

    @Query("SELECT * FROM products WHERE categoryName = :category AND isActive = 1")
    List<Product> getProductsByCategory(String category);

    @Query("SELECT DISTINCT categoryName FROM products WHERE isActive = 1")
    List<String> getAllCategories();

    @Query("UPDATE products SET quantity = quantity - :soldQuantity WHERE id = :productId")
    void reduceProductQuantity(int productId, int soldQuantity);

    @Query("DELETE FROM products")
    void deleteAll();

    @Query("DELETE FROM products WHERE id = :productId")
    void deleteById(int productId);

    @Insert
    void insertAll(Product... products);
}