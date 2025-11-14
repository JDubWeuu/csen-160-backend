package com.csen160.cartservice.dto;

import com.csen160.cartservice.model.Cart;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private Long userId;
    private List<CartItemResponse> items;
    private Double totalAmount;

    public CartResponse() {
    }

    public CartResponse(Cart cart) {
        this.userId = cart.getUserId();
        this.items = cart.getItems().stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
        this.totalAmount = cart.getTotalAmount();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}

