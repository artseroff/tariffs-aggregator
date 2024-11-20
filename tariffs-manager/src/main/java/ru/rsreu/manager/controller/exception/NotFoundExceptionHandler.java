package ru.rsreu.manager.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.rsreu.manager.service.exception.EntityNotFoundException;

@ControllerAdvice
class NotFoundExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ModelAndView defaultErrorHandler(EntityNotFoundException e) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorText", e.getMessage());
        mav.setViewName("notFoundPage");
        return mav;
    }
}
