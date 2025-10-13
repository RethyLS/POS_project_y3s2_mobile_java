package com.example.pos_project.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;
import com.example.pos_project.database.POSDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportsActivity extends AppCompatActivity {

    private TextView tvTodaySales, tvTodayTransactions, tvWeekSales, tvWeekTransactions;
    private RecyclerView rvRecentSales;
    
    private POSDatabase database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        initViews();
        setupToolbar();
        initDatabase();
        loadReportsData();
    }

    private void initViews() {
        tvTodaySales = findViewById(R.id.tv_today_sales);
        tvTodayTransactions = findViewById(R.id.tv_today_transactions);
        tvWeekSales = findViewById(R.id.tv_week_sales);
        tvWeekTransactions = findViewById(R.id.tv_week_transactions);
        rvRecentSales = findViewById(R.id.rv_recent_sales);
        
        rvRecentSales.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initDatabase() {
        database = POSDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
    }

    private void loadReportsData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        // Calculate week start
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        String weekStart = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        
        executor.execute(() -> {
            try {
                // Today's data
                Double todaySales = database.saleDao().getTotalSalesForDate(today);
                int todayTransactions = database.saleDao().getSalesCountForDate(today);
                
                // This week's data (simplified - just count from week start)
                double weekSales = 0.0;
                int weekTransactions = 0;
                
                // Calculate week totals (simple approach)
                for (int i = 0; i < 7; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, i == 0 ? 0 : 1);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                    Double daySales = database.saleDao().getTotalSalesForDate(dateStr);
                    int dayTransactions = database.saleDao().getSalesCountForDate(dateStr);
                    
                    if (daySales != null) weekSales += daySales;
                    weekTransactions += dayTransactions;
                }
                
                final double finalWeekSales = weekSales;
                final int finalWeekTransactions = weekTransactions;
                
                runOnUiThread(() -> {
                    tvTodaySales.setText(String.format("$%.2f", todaySales != null ? todaySales : 0.0));
                    tvTodayTransactions.setText(String.valueOf(todayTransactions));
                    tvWeekSales.setText(String.format("$%.2f", finalWeekSales));
                    tvWeekTransactions.setText(String.valueOf(finalWeekTransactions));
                });
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}