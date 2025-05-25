package org.example.firstspringapp.controller;

import org.example.firstspringapp.model.Order;
import org.example.firstspringapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FileController {
    private final OrderService orderService;

    @Autowired
    public FileController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/files/{orderId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(order.getFileData());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + order.getFileName() + "\"")
                .body(resource);
    }
}