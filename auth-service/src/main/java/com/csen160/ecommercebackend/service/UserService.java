package com.csen160.ecommercebackend.service;

import com.csen160.ecommercebackend.dto.RegisterRequest;
import com.csen160.ecommercebackend.dto.UserRegisteredEvent;
import com.csen160.ecommercebackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // In-memory storage
    private static final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> usernameToIdMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> emailToIdMap = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long id = usernameToIdMap.get(username);
        if (id == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User user = userMap.get(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    public User registerUser(RegisterRequest registerRequest) {
        if (usernameToIdMap.containsKey(registerRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (emailToIdMap.containsKey(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        long newId = idCounter.incrementAndGet();
        User user = new User(
                newId,
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                registerRequest.getFirstName(),
                registerRequest.getLastName()
        );

        // Save user in memory
        userMap.put(user.getId(), user);
        usernameToIdMap.put(user.getUsername(), user.getId());
        emailToIdMap.put(user.getEmail(), user.getId());

        // Publish user registered event to Kafka
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
        kafkaProducerService.publishUserRegisteredEvent(event);

        return user;
    }
}