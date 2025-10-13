package com.example.pos_project.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pos_project.R;
import com.example.pos_project.adapter.CartAdapter;
import com.example.pos_project.database.POSDatabase;
import com.example.pos_project.model.CartItem;
import com.example.pos_project.model.Sale;
import com.example.pos_project.model.SaleItem;
import com.example.pos_project.utils.CartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {

    private Toolbar toolbar;
    private RecyclerView rvCartItems;
    private LinearLayout layoutEmptyCart;
    private EditText etCustomerName, etAmountPaid;
    private TextView tvTotalAmount;
    private Button btnClearCart, btnCheckout, btnContinueShopping;
    private RadioGroup rgPaymentMethod;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    
    private POSDatabase database;
    private ExecutorService executor;
    
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        initDatabase();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        loadCartItems();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvCartItems = findViewById(R.id.rv_cart_items);
        layoutEmptyCart = findViewById(R.id.layout_empty_cart);
        etCustomerName = findViewById(R.id.et_customer_name);
        etAmountPaid = findViewById(R.id.et_amount_paid);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnClearCart = findViewById(R.id.btn_clear_cart);
        btnCheckout = findViewById(R.id.btn_checkout);
        btnContinueShopping = findViewById(R.id.btn_continue_shopping);
        rgPaymentMethod = findViewById(R.id.rg_payment_method);
    }

    private void initDatabase() {
        database = POSDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItems, this);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(cartAdapter);
    }

    private void setupClickListeners() {
        btnClearCart.setOnClickListener(v -> clearCart());
        btnCheckout.setOnClickListener(v -> processCheckout());
        btnContinueShopping.setOnClickListener(v -> finish());

        etAmountPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateCheckoutButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCartItems() {
        // Get cart items from CartManager singleton
        List<CartItem> items = CartManager.getInstance().getCartItems();
        cartItems.clear();
        cartItems.addAll(items);
        cartAdapter.notifyDataSetChanged();
        
        updateUI();
        updateTotalAmount();
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            rvCartItems.setVisibility(View.GONE);
            layoutEmptyCart.setVisibility(View.VISIBLE);
            findViewById(R.id.card_checkout).setVisibility(View.GONE);
        } else {
            rvCartItems.setVisibility(View.VISIBLE);
            layoutEmptyCart.setVisibility(View.GONE);
            findViewById(R.id.card_checkout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onIncreaseQuantity(CartItem item, int position) {
        item.setQuantity(item.getQuantity() + 1);
        cartAdapter.notifyItemChanged(position);
        updateTotalAmount();
        CartManager.getInstance().updateCartItem(item);
    }

    @Override
    public void onDecreaseQuantity(CartItem item, int position) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartAdapter.notifyItemChanged(position);
            updateTotalAmount();
            CartManager.getInstance().updateCartItem(item);
        }
    }

    @Override
    public void onRemoveItem(CartItem item, int position) {
        cartItems.remove(position);
        cartAdapter.notifyItemRemoved(position);
        updateTotalAmount();
        updateUI();
        CartManager.getInstance().removeCartItem(item);
    }

    private void clearCart() {
        if (!cartItems.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Clear Cart")
                    .setMessage("Are you sure you want to clear all items from the cart?")
                    .setPositiveButton("Clear", (dialog, which) -> {
                        cartItems.clear();
                        cartAdapter.notifyDataSetChanged();
                        updateTotalAmount();
                        updateUI();
                        CartManager.getInstance().clearCart();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getTotalPrice();
        }
        tvTotalAmount.setText(String.format("$%.2f", totalAmount));
        updateCheckoutButton();
    }

    private void updateCheckoutButton() {
        String amountPaidStr = etAmountPaid.getText().toString().trim();
        boolean hasItems = !cartItems.isEmpty();
        boolean hasValidAmount = false;

        if (!amountPaidStr.isEmpty()) {
            try {
                double amountPaid = Double.parseDouble(amountPaidStr);
                hasValidAmount = amountPaid >= totalAmount;
            } catch (NumberFormatException e) {
                hasValidAmount = false;
            }
        }

        btnCheckout.setEnabled(hasItems && hasValidAmount);
    }

    private void processCheckout() {
        String customerName = etCustomerName.getText().toString().trim();
        String amountPaidStr = etAmountPaid.getText().toString().trim();
        
        if (amountPaidStr.isEmpty()) {
            Toast.makeText(this, "Please enter amount paid", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amountPaid = Double.parseDouble(amountPaidStr);
            
            if (amountPaid < totalAmount) {
                Toast.makeText(this, "Amount paid is less than total", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = getSelectedPaymentMethod();
            String saleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            
            Sale sale = new Sale(saleDate, totalAmount, amountPaid, paymentMethod, 1, customerName);
            
            executor.execute(() -> {
                try {
                    // Insert sale
                    long saleId = database.saleDao().insert(sale);
                    
                    // Insert sale items and update product quantities
                    List<SaleItem> saleItems = new ArrayList<>();
                    for (CartItem cartItem : cartItems) {
                        SaleItem saleItem = new SaleItem((int) saleId, cartItem.getProductId(), 
                                cartItem.getProductName(), cartItem.getUnitPrice(), cartItem.getQuantity());
                        saleItems.add(saleItem);
                        
                        // Reduce product quantity
                        database.productDao().reduceProductQuantity(cartItem.getProductId(), cartItem.getQuantity());
                    }
                    database.saleItemDao().insertAll(saleItems);
                    
                    runOnUiThread(() -> {
                        double change = amountPaid - totalAmount;
                        showCheckoutSuccess(change);
                        clearCartAfterSale();
                    });
                    
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(CartActivity.this, "Error processing sale: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == R.id.rb_cash) return "cash";
        else if (selectedId == R.id.rb_card) return "card";
        else return "digital";
    }

    private void showCheckoutSuccess(double change) {
        String message = String.format("Sale completed successfully!\n\nTotal: $%.2f\nChange: $%.2f", 
                totalAmount, change);
        
        new AlertDialog.Builder(this)
                .setTitle("Sale Complete")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Return to main activity
                    Intent intent = new Intent(this, com.example.pos_project.MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void clearCartAfterSale() {
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
        updateTotalAmount();
        updateUI();
        CartManager.getInstance().clearCart();
        etCustomerName.setText("");
        etAmountPaid.setText("");
        rgPaymentMethod.check(R.id.rb_cash);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}