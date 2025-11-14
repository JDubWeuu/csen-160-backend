package com.csen160.cartservice.service;

import com.csen160.cartservice.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final String ORDER_CREATED_TOPIC = "order-created";

    @Autowired
    private CartService cartService;

    @KafkaListener(topics = ORDER_CREATED_TOPIC, groupId = "cart-service")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("Received order created event: orderId={}, userId={}, totalAmount={}", 
                event.getOrderId(), event.getUserId(), event.getTotalAmount());
        
        try {
            // Clear the cart for the user who created the order
            cartService.clearCart(event.getUserId());
            logger.info("Cart cleared for user: userId={} after order creation: orderId={}", 
                    event.getUserId(), event.getOrderId());
        } catch (Exception e) {
            logger.error("Failed to clear cart for user: userId={} after order creation: orderId={}, error: {}", 
                    event.getUserId(), event.getOrderId(), e.getMessage());
        }
    }
}

