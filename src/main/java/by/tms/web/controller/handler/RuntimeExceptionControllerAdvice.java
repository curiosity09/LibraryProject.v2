package by.tms.web.controller.handler;

import by.tms.web.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RuntimeExceptionControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public String handleError(Model model, RuntimeException exception) {
        log.debug(exception.getMessage(), exception);
        model.addAttribute(PageUtil.EXCEPTION_MESSAGE, exception.getMessage());
        return PageUtil.ERROR_PAGE_PREFIX;
    }
}
