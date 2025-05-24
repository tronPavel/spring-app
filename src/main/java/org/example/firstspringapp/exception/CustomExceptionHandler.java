package org.example.firstspringapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleNumberFormatException(NumberFormatException ex) {
        logger.error("NumberFormatException occurred: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error/400");
        modelAndView.addObject("message", "Invalid number format. Please provide a valid number.");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("message", "Internal server error. Please try again later.");
        return modelAndView;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NoHandlerFoundException ex) {
        logger.error("Page not found: {}", ex.getRequestURL());
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("message", "Page not found: " + ex.getRequestURL());
        return modelAndView;
    }

}