package com.csen160.cartservice.service;

import com.csen160.cartservice.dto.AddItemRequest;
import com.csen160.cartservice.model.Cart;
import com.csen160.cartservice.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    // In-memory storage: userId -> Cart
    private static final Map<Long, Cart> cartMap = new ConcurrentHashMap<>();

    public Cart getCart(Long userId) {
        return cartMap.computeIfAbsent(userId, k -> new Cart(userId));
    }

    public Cart addItem(Long userId, AddItemRequest request) {
        Cart cart = getCart(userId);
        
        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity if item already exists
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            // Add new item
            CartItem newItem = new CartItem(
                    request.getProductId(),
                    request.getProductName(),
                    request.getQuantity(),
                    request.getPrice()
            );
            cart.getItems().add(newItem);
        }

        cart.calculateTotal();
        return cart;
    }

    public Cart updateItemQuantity(Long userId, String productId, Integer quantity) {
        Cart cart = getCart(userId);
        
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        cart.calculateTotal();
        return cart;
    }

    public Cart removeItem(Long userId, String productId) {
        Cart cart = getCart(userId);
        
        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            throw new RuntimeException("Item not found in cart");
        }

        cart.calculateTotal();
        return cart;
    }

    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cart.calculateTotal();
    }

    public List<CartItem> getCartItems(Long userId) {
        Cart cart = getCart(userId);
        return cart.getItems();
    }
}

