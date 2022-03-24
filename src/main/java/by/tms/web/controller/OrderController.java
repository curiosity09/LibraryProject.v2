package by.tms.web.controller;

import by.tms.model.dto.AuthorDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return orderService.findAllOrder(10,0);
    }

    @GetMapping("/allOrderPage")
    public String allOrderPage(Model model) {
        model.addAttribute("allOrders", orderService.findAllOrder(10,0));
        return "page/librarian/allOrders";
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart() {
        return "page/user/shoppingCart";
    }

    @PostMapping("/addOrder")
    public String addOrder(@SessionAttribute("account") AccountDto user,
                           @SessionAttribute("shoppingCart") ShoppingCart shoppingCart,
                           @RequestParam("rentalPeriod") String period,
                           Model model) {
        if (Objects.nonNull(user) && !shoppingCart.getShoppingList().isEmpty()) {
            List<BookDto> selectedBook = shoppingCart.getShoppingList();
            Optional<AccountDto> userByUsername = accountService.findUserByUsername(user.getUsername());
            OrderDto orderDto = OrderDto.builder()
                    .bookList(selectedBook)
                    .user(userByUsername.get())
                    .rentalTime(LocalDateTime.now())
                    .rentalPeriod(setRentalPeriod(period))
                    .build();
            if (Objects.nonNull(orderService.addOrder(orderDto))) {
                for (BookDto book : selectedBook) {
                    book.setQuantity(book.getQuantity() - 1);
                    bookService.updateBook(book);
                }
                shoppingCart.getShoppingList().clear();
                model.addAttribute("shoppingCart", shoppingCart);
                return "redirect:/userPage";
            }
            return "redirect:/errorPage";
        }
        return "redirect:/errorPage";
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
    public String showUserOrder(@SessionAttribute("account") AccountDto account, Model model) {
        model.addAttribute("userOrder", orderService.findOrderByUsername(account.getUsername(),10,0));
        return "/page/user/userOrder";
    }

    @GetMapping("/delBookFromOrder/{id}")
    public String deleteBookFromOrder(Model model,
                                      @PathVariable String id,
                                      @SessionAttribute("shoppingCart") ShoppingCart shoppingCart) {
        Optional<BookDto> bookById = bookService.findBookById(Long.parseLong(id));
        bookById.ifPresent(shoppingCart.getShoppingList()::remove);
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/shoppingCart";
    }

    @PostMapping("/findUserOrder")
    public String findUserOrder(Model model, @RequestParam("username") String username) {
        if (Objects.nonNull(username)) {
            model.addAttribute("userOrders", orderService.findOrderByUsername(username,10,0));
            return "page/librarian/userOrder";
        }
        return "redirect:/errorPage";
    }

    @GetMapping("/finishOrder/{id}")
    public String finishOrder(@PathVariable String id) {
        orderService.deleteOrder(OrderDto.builder().id(Long.parseLong(id)).build());
        return "redirect:/librarianPage";
    }
}
