package com.csen160.cartservice.controller;

import com.csen160.cartservice.dto.AddItemRequest;
import com.csen160.cartservice.dto.CartResponse;
import com.csen160.cartservice.model.Cart;
import com.csen160.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);
            Cart cart = cartService.getCart(userId);
            return ResponseEntity.ok(new CartResponse(cart));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid user ID in header");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User authentication required");
        }
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            @Valid @RequestBody AddItemRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);
            Cart cart = cartService.addItem(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CartResponse(cart));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid user ID in header");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User authentication required");
        }
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<?> updateItemQuantity(
            @PathVariable String productId,
            @RequestParam Integer quantity,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);
            Cart cart = cartService.updateItemQuantity(userId, productId, quantity);
            return ResponseEntity.ok(new CartResponse(cart));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid user ID in header");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User authentication required");
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable String productId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);
            Cart cart = cartService.removeItem(userId, productId);
            return ResponseEntity.ok(new CartResponse(cart));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid user ID in header");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User authentication required");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        try {
            Long userId = Long.parseLong(userIdHeader);
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid user ID in header");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("User authentication required");
        }
    }
}

