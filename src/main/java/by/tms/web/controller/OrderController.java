package by.tms.web.controller;

import by.tms.model.dto.BookDto;
import by.tms.model.dto.OrderDto;
import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.BookService;
import by.tms.model.service.OrderService;
import by.tms.web.util.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final BookService bookService;

    @GetMapping("/librarian/allOrderPage")
    public String allOrderPage(Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages());
        model.addAttribute(ALL_ORDERS_ATTRIBUTE, orderService.findAll(LIMIT_TEN, Integer.parseInt(offset)));
        return LIBRARIAN_PREFIX + ALL_ORDERS_PAGE;
    }

    @GetMapping("/user/shoppingCart")
    public String shoppingCart() {
        return USER_PREFIX + SHOPPING_CART_SUFFIX;
    }

    @PostMapping("/addOrder")
    public String addOrder(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto user,
                           @SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart,
                           @RequestParam(RENTAL_PERIOD_PARAMETER) String period,
                           Model model) {
        if (Objects.nonNull(user) && !shoppingCart.getShoppingList().isEmpty()) {
            List<BookDto> selectedBook = shoppingCart.getShoppingList();
            OrderDto orderDto = OrderDto.builder()
                    .bookList(selectedBook)
                    .user(user)
                    .rentalTime(LocalDateTime.now())
                    .rentalPeriod(setRentalPeriod(period))
                    .build();
            if (Objects.nonNull(orderService.save(orderDto))) {
                log.debug(LoggerUtil.ENTITY_WAS_SAVED_IN_CONTROLLER, orderDto);
                for (BookDto book : selectedBook) {
                    book.setQuantity(book.getQuantity() - ONE_BOOK);
                    bookService.update(book);
                    log.debug(LoggerUtil.ENTITY_WAS_UPDATED_IN_CONTROLLER, book);
                }
                shoppingCart.getShoppingList().clear();
                model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
                return REDIRECT + USER_PAGE;
            }
            return REDIRECT + ERROR_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/user/showUserOrder")
    public String showUserOrder(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account,
                                Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages(account.getUsername()));
        model.addAttribute(USER_ORDER_ATTRIBUTE, orderService.findOrderByUsername(account.getUsername(), LIMIT_TEN, Integer.parseInt(offset)));
        return USER_PREFIX + USER_ORDER_PAGE;
    }

    @GetMapping("/user/delBookFromOrder/{id}")
    public String deleteBookFromOrder(Model model,
                                      @PathVariable(ID_PATH_VARIABLE) String id,
                                      @SessionAttribute(SHOPPING_CART_ATTRIBUTE) ShoppingCart shoppingCart) {
        Optional<BookDto> bookById = bookService.findById(Long.parseLong(id));
        log.debug(LoggerUtil.ENTITY_WAS_FOUND_IN_CONTROLLER_BY, bookById, id);
        bookById.ifPresent(shoppingCart.getShoppingList()::remove);
        model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
        return REDIRECT + SHOPPING_CART_PAGE;
    }

    @GetMapping("/librarian/findUserOrder")
    public String findUserOrder(Model model,
                                @RequestParam(USERNAME_PARAMETER) String username,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = VALUE_ZERO) String offset) {
        if (Objects.nonNull(username)) {
            model.addAttribute(USERNAME_PARAMETER, username);
            model.addAttribute(COUNT_PAGES_ATTRIBUTE, orderService.getCountPages(username));
            model.addAttribute(USER_ORDER_ATTRIBUTE, orderService.findOrderByUsername(username,
                    LIMIT_TEN, Integer.parseInt(offset)));
            return LIBRARIAN_PREFIX + USER_ORDER_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/librarian/finishOrder/{id}")
    public String finishOrder(@PathVariable(ID_PATH_VARIABLE) String id,
                              @RequestHeader(value = REFERER_HEADER, required = false) final String referer) {
        OrderDto orderDto = OrderDto.builder().id(Long.parseLong(id)).build();
        orderService.delete(orderDto);
        log.debug(LoggerUtil.ENTITY_WAS_DELETED_IN_CONTROLLER, orderDto);
        return REDIRECT + referer;
    }
}
