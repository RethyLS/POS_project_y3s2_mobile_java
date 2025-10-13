package com.example.pos_project.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pos_project.R;
import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.model.Product;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditProductActivity extends AppCompatActivity {

    private EditText etProductName, etCategory, etPrice, etQuantity, etBarcode, etDescription;
    private Button btnSave, btnCancel;
    private Toolbar toolbar;
    
    private POSDatabase database;
    private ExecutorService executor;
    private int productId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        initViews();
        setupToolbar();
        initDatabase();
        checkEditMode();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etProductName = findViewById(R.id.et_product_name);
        etCategory = findViewById(R.id.et_category);
        etPrice = findViewById(R.id.et_price);
        etQuantity = findViewById(R.id.et_quantity);
        etBarcode = findViewById(R.id.et_barcode);
        etDescription = findViewById(R.id.et_description);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Product");
        }
        
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initDatabase() {
        database = POSDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
    }

    private void checkEditMode() {
        productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            isEditMode = true;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Product");
            }
            loadProductData();
        }
    }

    private void loadProductData() {
        executor.execute(() -> {
            Product product = database.productDao().getProductById(productId);
            if (product != null) {
                runOnUiThread(() -> {
                    etProductName.setText(product.getName());
                    etCategory.setText(product.getCategory());
                    etPrice.setText(String.valueOf(product.getPrice()));
                    etQuantity.setText(String.valueOf(product.getQuantity()));
                    etBarcode.setText(product.getBarcode());
                    etDescription.setText(product.getDescription());
                });
            }
        });
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> saveProduct());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String barcode = etBarcode.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etProductName.setError("Product name is required");
            return;
        }
        if (category.isEmpty()) {
            etCategory.setError("Category is required");
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Price is required");
            return;
        }
        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (price < 0) {
                etPrice.setError("Price must be positive");
                return;
            }
            if (quantity < 0) {
                etQuantity.setError("Quantity must be positive");
                return;
            }

            executor.execute(() -> {
                try {
                    if (isEditMode) {
                        Product product = database.productDao().getProductById(productId);
                        if (product != null) {
                            product.setName(name);
                            product.setCategory(category);
                            product.setPrice(price);
                            product.setQuantity(quantity);
                            product.setBarcode(barcode.isEmpty() ? null : barcode);
                            product.setDescription(description.isEmpty() ? null : description);
                            database.productDao().update(product);
                        }
                    } else {
                        Product product = new Product(name, description, price, quantity, category);
                        product.setBarcode(barcode.isEmpty() ? null : barcode);
                        database.productDao().insert(product);
                    }

                    runOnUiThread(() -> {
                        Toast.makeText(this, 
                            isEditMode ? "Product updated successfully" : "Product added successfully", 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error saving product: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    });
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity format", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}