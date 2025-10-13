package com.example.pos_project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;
import com.example.pos_project.adapter.ProductAdapter;
import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView rvProducts;
    private EditText etSearch;
    private Button btnAddProduct;
    
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;
    
    private POSDatabase database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initViews();
        setupToolbar();
        initDatabase();
        setupRecyclerView();
        setupClickListeners();
        setupSearch();
        loadProducts();
    }

    private void initViews() {
        rvProducts = findViewById(R.id.rv_products);
        etSearch = findViewById(R.id.et_search);
        btnAddProduct = findViewById(R.id.btn_add_product);
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

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(filteredList, this);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
            startActivity(intent);
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        executor.execute(() -> {
            List<Product> products = database.productDao().getAllActiveProducts();
            runOnUiThread(() -> {
                productList.clear();
                productList.addAll(products);
                filteredList.clear();
                filteredList.addAll(products);
                adapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onEditClick(Product product) {
        Intent intent = new Intent(ProductActivity.this, AddEditProductActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete " + product.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProduct(Product product) {
        executor.execute(() -> {
            product.setActive(false);
            database.productDao().update(product);
            runOnUiThread(() -> {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                loadProducts();
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Refresh products when returning from add/edit
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}