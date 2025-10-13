package com.example.pos_project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pos_project.model.SaleItem;

import java.util.List;

@Dao
public interface SaleItemDao {
    @Insert
    void insert(SaleItem saleItem);

    @Insert
    void insertAll(List<SaleItem> saleItems);

    @Update
    void update(SaleItem saleItem);

    @Delete
    void delete(SaleItem saleItem);

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    List<SaleItem> getSaleItemsBySaleId(int saleId);

    @Query("SELECT * FROM sale_items WHERE id = :id")
    SaleItem getSaleItemById(int id);
}