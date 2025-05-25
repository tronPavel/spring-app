package org.example.firstspringapp.service;

import org.example.firstspringapp.model.Order;
import org.example.firstspringapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Long userId, String description, MultipartFile file) throws IOException {
        Order order = new Order();
        order.setUserId(userId);
        order.setDescription(description);
        order.setCreatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            order.setFileData(file.getBytes());
            order.setFileName(file.getOriginalFilename());
        }

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateOrder(Long id, String description, MultipartFile file) throws IOException {
        Order order = getOrderById(id);
        if (order != null) {
            order.setDescription(description);
            if (file != null && !file.isEmpty()) {
                order.setFileData(file.getBytes());
                order.setFileName(file.getOriginalFilename());
            }
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}