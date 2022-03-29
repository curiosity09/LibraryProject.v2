package by.tms.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RuntimeExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public String handleError(Model model, RuntimeException exception){
        log.debug(exception.getMessage(), exception);
        model.addAttribute("exceptionMessage", exception.getMessage());
        return "page/errorPage";
    }
}
