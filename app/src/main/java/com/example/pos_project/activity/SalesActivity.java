package com.example.pos_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;
import com.example.pos_project.adapter.CategoryAdapter;
import com.example.pos_project.adapter.SaleProductAdapter;
import com.example.pos_project.activity.CartActivity;
import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.model.CartItem;
import com.example.pos_project.model.Product;
import com.example.pos_project.utils.CartManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalesActivity extends AppCompatActivity implements 
        SaleProductAdapter.OnProductClickListener, CategoryAdapter.OnCategoryClickListener {

    private RecyclerView rvProductsSale, rvCategories;
    private EditText etSearchProducts;
    private Toolbar toolbar;
    private TextView tvCartBadge;
    private FloatingActionButton fabCart, fabSaveTicket;

    private SaleProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    
    private List<Product> productList;
    private List<Product> filteredProductList;
    private List<String> categories;
    
    private POSDatabase database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        initViews();
        setupToolbar();
        initDatabase();
        setupRecyclerViews();
        setupClickListeners();
        setupSearch();
        loadProducts();
        updateCartBadge();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sales");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sales_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_cart) {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
            return true;
        } else if (id == R.id.action_products) {
            Intent productsIntent = new Intent(this, ProductActivity.class);
            startActivity(productsIntent);
            return true;
        } else if (id == R.id.action_users) {
            Intent usersIntent = new Intent(this, UsersActivity.class);
            startActivity(usersIntent);
            return true;
        } else if (id == R.id.action_reports) {
            Intent reportsIntent = new Intent(this, ReportsActivity.class);
            startActivity(reportsIntent);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        rvProductsSale = findViewById(R.id.rv_products_sale);
        rvCategories = findViewById(R.id.rv_categories);
        etSearchProducts = findViewById(R.id.et_search_products);
        toolbar = findViewById(R.id.toolbar);
        tvCartBadge = findViewById(R.id.tv_cart_badge);
        fabCart = findViewById(R.id.fab_cart);
        fabSaveTicket = findViewById(R.id.fab_save_ticket);
    }

    private void initDatabase() {
        database = POSDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
    }

    private void setupRecyclerViews() {
        // Products RecyclerView - Grid Layout
        productList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        productAdapter = new SaleProductAdapter(filteredProductList, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns
        rvProductsSale.setLayoutManager(gridLayoutManager);
        rvProductsSale.setAdapter(productAdapter);
        
        // Categories RecyclerView - Horizontal
        categories = new ArrayList<>();
        categories.add("All");
        categories.add("Food");
        categories.add("Drinks");
        categories.add("Electronics");
        categories.add("Clothing");
        categoryAdapter = new CategoryAdapter(categories, this);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(categoryLayoutManager);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupClickListeners() {
        fabCart.setOnClickListener(v -> {
            Intent cartIntent = new Intent(this, CartActivity.class);
            startActivity(cartIntent);
        });

        fabSaveTicket.setOnClickListener(v -> {
            saveCurrentTicket();
        });
    }

    private void updateCartBadge() {
        int cartItemCount = CartManager.getInstance().getCartItemCount();
        if (cartItemCount > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(cartItemCount));
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
    }

    private void saveCurrentTicket() {
        if (CartManager.getInstance().getCartItems().isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ticket number
        String ticketNumber = "TKT-" + System.currentTimeMillis();
        
        // For now, just show a toast. In the future, you can implement ticket saving to database
        Toast.makeText(this, "Ticket " + ticketNumber + " saved with " + 
                       CartManager.getInstance().getCartItemCount() + " items", 
                       Toast.LENGTH_LONG).show();
        
        // Optionally clear the cart after saving
        // CartManager.getInstance().clearCart();
        // updateCartBadge();
    }

    private void setupSearch() {
        etSearchProducts.addTextChangedListener(new TextWatcher() {
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
        filteredProductList.clear();
        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(query.toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        executor.execute(() -> {
            List<Product> products = database.productDao().getAllActiveProducts();
            runOnUiThread(() -> {
                productList.clear();
                productList.addAll(products);
                filteredProductList.clear();
                filteredProductList.addAll(products);
                productAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Add product to cart using CartManager
        CartItem cartItem = new CartItem(
            product.getId(),
            product.getName(),
            product.getPrice(),
            1
        );
        
        CartManager.getInstance().addToCart(cartItem);
        updateCartBadge(); // Update the cart badge count
        Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category, int position) {
        // Filter products by selected category
        if ("All".equals(category)) {
            filteredProductList.clear();
            filteredProductList.addAll(productList);
        } else {
            filteredProductList.clear();
            for (Product product : productList) {
                if (category.equalsIgnoreCase(product.getCategory())) {
                    filteredProductList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // Update cart badge when returning to this activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}