package com.example.pos_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.pos_project.activity.CartActivity;
import com.example.pos_project.activity.ProductActivity;
import com.example.pos_project.activity.ReportsActivity;
import com.example.pos_project.activity.SalesActivity;
import com.example.pos_project.activity.UsersActivity;
import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.utils.CartManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private CardView cardSales, cardProducts, cardUsers, cardReports;
    private TextView tvTotalSales, tvTransactions, tvProductsCount;
    private POSDatabase database;
    private ExecutorService executor;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        initDatabase();
        setupClickListeners();
        loadDashboardData();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_cart) {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        cardSales = findViewById(R.id.card_sales);
        cardProducts = findViewById(R.id.card_products);
        cardUsers = findViewById(R.id.card_users);
        cardReports = findViewById(R.id.card_reports);
        
        tvTotalSales = findViewById(R.id.tv_total_sales);
        tvTransactions = findViewById(R.id.tv_transactions);
        tvProductsCount = findViewById(R.id.tv_products_count);
    }

    private void initDatabase() {
        database = POSDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
        
        // Initialize database with sample data
        com.example.pos_project.utils.DatabaseInitializer.initializeDatabase(this);
    }

    private void setupClickListeners() {
        cardSales.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SalesActivity.class);
            startActivity(intent);
        });

        cardProducts.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        cardUsers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(intent);
        });

        cardReports.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
            startActivity(intent);
        });
    }

    private void loadDashboardData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        executor.execute(() -> {
            try {
                Double totalSales = database.saleDao().getTotalSalesForDate(today);
                int transactionCount = database.saleDao().getSalesCountForDate(today);
                int productCount = database.productDao().getAllActiveProducts().size();
                
                runOnUiThread(() -> {
                    tvTotalSales.setText(String.format("$%.2f", totalSales != null ? totalSales : 0.0));
                    tvTransactions.setText(String.valueOf(transactionCount));
                    tvProductsCount.setText(String.valueOf(productCount));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData(); // Refresh data when returning to dashboard
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}