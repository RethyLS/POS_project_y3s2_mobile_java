package com.example.pos_project.utils;

import android.content.Context;

import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.model.Product;
import com.example.pos_project.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    
    public static void initializeDatabase(Context context) {
        POSDatabase database = POSDatabase.getInstance(context);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        executor.execute(() -> {
            // Check if data already exists
            if (database.userDao().getAdminCount() == 0) {
                // Add default admin user
                User admin = new User("admin", "admin123", "admin", "System Administrator");
                database.userDao().insert(admin);
                
                // Add sample cashier
                User cashier = new User("cashier", "cash123", "cashier", "John Doe");
                database.userDao().insert(cashier);
            }
            
            // Add sample products if none exist
            if (database.productDao().getAllActiveProducts().isEmpty()) {
                // Sample products for different categories
                Product[] sampleProducts = {
                    new Product("Coca Cola 330ml", "Refreshing cola drink", 1.50, 50, "Beverages"),
                    new Product("Bread - White Loaf", "Fresh white bread", 2.25, 25, "Bakery"),
                    new Product("Milk - 1L", "Fresh whole milk", 3.00, 30, "Dairy"),
                    new Product("Apples - Red", "Fresh red apples per kg", 4.50, 20, "Fruits"),
                    new Product("Chicken Breast", "Fresh chicken breast per kg", 8.99, 15, "Meat"),
                    new Product("Rice - 5kg", "Premium jasmine rice", 12.50, 10, "Grains"),
                    new Product("Shampoo - 400ml", "Moisturizing shampoo", 6.75, 40, "Personal Care"),
                    new Product("Notebook - A4", "Ruled notebook 100 pages", 3.25, 60, "Stationery"),
                    new Product("Chocolate Bar", "Dark chocolate 70% cocoa", 2.80, 35, "Confectionery"),
                    new Product("Orange Juice - 1L", "100% pure orange juice", 4.25, 20, "Beverages")
                };
                
                for (Product product : sampleProducts) {
                    database.productDao().insert(product);
                }
            }
        });
        
        executor.shutdown();
    }
}