package org.example.firstspringapp.controller;

import org.example.firstspringapp.model.Order;
import org.example.firstspringapp.model.User;
import org.example.firstspringapp.service.OrderService;
import org.example.firstspringapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("order", new Order());
        return "create-order";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("description") String description,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              Principal principal) throws IOException {
        Long userId = getUserIdFromPrincipal(principal);
        orderService.createOrder(userId, description, file);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            model.addAttribute("order", order);
            return "edit-order";
        }
        return "redirect:/orders";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Long id,
                              @RequestParam("description") String description,
                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        orderService.updateOrder(id, description, file);
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return user.getId();
    }
}