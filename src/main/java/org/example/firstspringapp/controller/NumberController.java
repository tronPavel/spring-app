package org.example.firstspringapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberController {
    private static final Logger logger = LoggerFactory.getLogger(NumberController.class);
    private static final int N = 5;

    @GetMapping("/multiply")
    public int multiply(@RequestParam("number") String numberStr) {
        logger.info("Received number: {}", numberStr);
        try {
            int number = Integer.parseInt(numberStr);
            int result = number * N;
            logger.info("Returning result: {}", result);
            return result;
        } catch (NumberFormatException ex) {
            logger.error("Invalid number format: {}", numberStr);
            throw ex; // Будет обработано в CustomExceptionHandler
        }
    }
}