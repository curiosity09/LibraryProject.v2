package by.tms.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RuntimeExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public String handleError(Model model, RuntimeException exception){
        //Logger.error("text here", exception);
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "page/errorPage";
    }
}
