package com.example.pos_project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pos_project.model.Sale;

import java.util.List;

@Dao
public interface SaleDao {
    @Insert
    long insert(Sale sale);

    @Update
    void update(Sale sale);

    @Delete
    void delete(Sale sale);

    @Query("SELECT * FROM sales ORDER BY saleDate DESC")
    List<Sale> getAllSales();

    @Query("SELECT * FROM sales WHERE id = :id")
    Sale getSaleById(int id);

    @Query("SELECT * FROM sales WHERE saleDate LIKE :date || '%' ORDER BY saleDate DESC")
    List<Sale> getSalesByDate(String date);

    @Query("SELECT SUM(totalAmount) FROM sales WHERE saleDate LIKE :date || '%'")
    Double getTotalSalesForDate(String date);

    @Query("SELECT COUNT(*) FROM sales WHERE saleDate LIKE :date || '%'")
    int getSalesCountForDate(String date);
}