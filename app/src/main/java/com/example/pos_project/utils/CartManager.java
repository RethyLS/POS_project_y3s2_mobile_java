package com.example.pos_project.utils;

import com.example.pos_project.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add method to reset the singleton instance (useful for app restart scenarios)
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.clearCart();
            instance = null;
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void addToCart(CartItem item) {
        // Check if product is already in cart
        CartItem existingItem = findCartItem(item.getProductId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            cartItems.add(item);
        }
    }

    public void updateCartItem(CartItem item) {
        CartItem existingItem = findCartItem(item.getProductId());
        if (existingItem != null) {
            existingItem.setQuantity(item.getQuantity());
        }
    }

    public void removeCartItem(CartItem item) {
        cartItems.removeIf(cartItem -> cartItem.getProductId() == item.getProductId());
    }

    public void clearCart() {
        cartItems.clear();
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    private CartItem findCartItem(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                return item;
            }
        }
        return null;
    }
}