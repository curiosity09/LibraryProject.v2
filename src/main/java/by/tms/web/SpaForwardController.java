package by.tms.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Serves the React single page application for every client side route.
 * Deep links such as {@code /librarian/books/1/edit} are forwarded to {@code index.html}
 * so that the SPA router can handle them. The REST API lives under {@code /api} and static
 * assets are served by the default resource handler.
 */
@Controller
public class SpaForwardController {

    @GetMapping({
            "/",
            "/login",
            "/register",
            "/books",
            "/books/**",
            "/authors",
            "/authors/**",
            "/cart",
            "/orders",
            "/profile",
            "/profile/**",
            "/librarian",
            "/librarian/**",
            "/admin",
            "/admin/**",
            "/error",
            "/banned"
    })
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
