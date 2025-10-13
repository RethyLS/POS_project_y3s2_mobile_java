package com.example.pos_project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pos_project.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE isActive = 1")
    List<User> getAllActiveUsers();

    @Query("SELECT * FROM users WHERE username = :username AND password = :password AND isActive = 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

    @Query("SELECT COUNT(*) FROM users WHERE role = 'admin' AND isActive = 1")
    int getAdminCount();
}