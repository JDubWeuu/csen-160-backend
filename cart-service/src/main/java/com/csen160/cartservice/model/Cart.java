package com.csen160.cartservice.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private Long userId;
    private List<CartItem> items;
    private Double totalAmount;

    public Cart() {
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotal();
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void calculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}

