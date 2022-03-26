package by.tms.web.controller;

import by.tms.model.dto.BookDto;
import by.tms.model.dto.OrderDto;
import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.AccountService;
import by.tms.model.service.BookService;
import by.tms.model.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;
    private final AccountService accountService;

    @Autowired
    public OrderController(OrderService orderService, BookService bookService, AccountService accountService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.accountService = accountService;
    }

    @ModelAttribute("allOrders")
    public List<OrderDto> allOrders() {
        return orderService.findAllOrder(LIMIT_TEN, OFFSET_ZERO);
    }

    @GetMapping("/allOrderPage")
    public String allOrderPage(Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages());
        model.addAttribute("allOrders", orderService.findAllOrder(LIMIT_TEN, Integer.parseInt(offset)));
        return LIBRARIAN_PREFIX + "allOrders";
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart() {
        return USER_PREFIX + SHOPPING_CART_PAGE;
    }

    @PostMapping("/addOrder")
    public String addOrder(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto user,
                           @SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart,
                           @RequestParam(RENTAL_PERIOD_PARAMETER) String period,
                           Model model) {
        if (Objects.nonNull(user) && !shoppingCart.getShoppingList().isEmpty()) {
            List<BookDto> selectedBook = shoppingCart.getShoppingList();
            Optional<AccountDto> userByUsername = accountService.findAccountByUsername(user.getUsername());
            OrderDto orderDto = OrderDto.builder()
                    .bookList(selectedBook)
                    .user(userByUsername.get())
                    .rentalTime(LocalDateTime.now())
                    .rentalPeriod(setRentalPeriod(period))
                    .build();
            if (Objects.nonNull(orderService.addOrder(orderDto))) {
                for (BookDto book : selectedBook) {
                    book.setQuantity(book.getQuantity() - ONE_BOOK);
                    bookService.updateBook(book);
                }
                shoppingCart.getShoppingList().clear();
                model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
                return REDIRECT + USER_PAGE;
            }
            return REDIRECT + ERROR_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    private LocalDateTime setRentalPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        int rentalPeriod = Integer.parseInt(period);
        if (rentalPeriod == 1) {
            long seconds = LocalDateTime.now().compareTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0))) > 0 ?
                    ChronoUnit.SECONDS.between(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(19, 0)), now)
                    : ChronoUnit.SECONDS.between(LocalTime.now(), LocalTime.of(19, 0));
            return now.plusSeconds(Math.abs(seconds));
        } else {
            return now.plusDays(rentalPeriod);
        }
    }

    @GetMapping("/showUserOrder")
    public String showUserOrder(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account,
                                Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages(account.getUsername()));
        model.addAttribute("userOrder", orderService.findOrderByUsername(account.getUsername(), LIMIT_TEN, Integer.parseInt(offset)));
        return USER_PREFIX + "userOrder";
    }

    @GetMapping("/delBookFromOrder/{id}")
    public String deleteBookFromOrder(Model model,
                                      @PathVariable String id,
                                      @SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart) {
        Optional<BookDto> bookById = bookService.findBookById(Long.parseLong(id));
        bookById.ifPresent(shoppingCart.getShoppingList()::remove);
        model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
        return REDIRECT + "/shoppingCart";
    }

    @GetMapping("/findUserOrder/")
    public String findUserOrder(Model model,
                                @RequestParam(USERNAME_PARAMETER) String username,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        if (Objects.nonNull(username)) {
            model.addAttribute(USERNAME_PARAMETER, username);
            model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages(username));
            model.addAttribute("userOrders", orderService.findOrderByUsername(username, LIMIT_TEN, Integer.parseInt(offset)));
            return LIBRARIAN_PREFIX + "userOrder";
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/finishOrder/{id}")
    public String finishOrder(@PathVariable String id,
                              @RequestHeader(value = REFERER_HEADER, required = false) final String referer) {
        orderService.deleteOrder(OrderDto.builder().id(Long.parseLong(id)).build());
        return REDIRECT + referer;
    }
}
