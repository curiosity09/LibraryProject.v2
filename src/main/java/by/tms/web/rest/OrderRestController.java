package by.tms.web.rest;

import by.tms.model.dto.BookDto;
import by.tms.model.dto.OrderDto;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.AccountService;
import by.tms.model.service.BookService;
import by.tms.model.service.OrderService;
import by.tms.web.rest.dto.CreateOrderRequest;
import by.tms.web.rest.dto.PageResponse;
import by.tms.web.rest.util.AccountUtils;
import by.tms.web.rest.util.PagingUtils;
import by.tms.web.rest.util.RentalPeriodUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderRestController {

    private final OrderService orderService;
    private final BookService bookService;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    public PageResponse<OrderDto> findAll(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<OrderDto> content = sanitize(orderService.findAll(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized)));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(orderService.getCountPages()));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user')")
    public PageResponse<OrderDto> myOrders(Authentication authentication, @RequestParam(defaultValue = "0") int page) {
        return pageForUsername(authentication.getName(), page);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    public PageResponse<OrderDto> ordersByUsername(@PathVariable String username,
                                                   @RequestParam(defaultValue = "0") int page) {
        return pageForUsername(username, page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@Valid @RequestBody CreateOrderRequest request, Authentication authentication) {
        AccountDto user = accountService.findAccountByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        List<BookDto> selected = new ArrayList<>();
        for (Long bookId : request.bookIds()) {
            bookService.findById(bookId)
                    .filter(book -> book.getQuantity() > 0)
                    .ifPresent(selected::add);
        }
        if (selected.isEmpty()) {
            throw new IllegalStateException("No available books were selected");
        }

        OrderDto order = OrderDto.builder()
                .bookList(selected)
                .user(user)
                .rentalTime(LocalDateTime.now())
                .rentalPeriod(RentalPeriodUtils.dueDate(request.rentalPeriod()))
                .build();
        Long id = orderService.save(order);
        order.setId(id);

        for (BookDto book : selected) {
            book.setQuantity(book.getQuantity() - 1);
            bookService.update(book);
        }
        log.debug("Order {} created for {}", id, user.getUsername());
        AccountUtils.sanitize(order.getUser());
        return order;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('librarian','admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishOrder(@PathVariable Long id) {
        orderService.delete(OrderDto.builder().id(id).build());
        log.debug("Order {} finished", id);
    }

    private PageResponse<OrderDto> pageForUsername(String username, int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<OrderDto> content = sanitize(
                orderService.findOrderByUsername(username, PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized)));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(orderService.getCountPages(username)));
    }

    private List<OrderDto> sanitize(List<OrderDto> orders) {
        orders.forEach(order -> AccountUtils.sanitize(order.getUser()));
        return orders;
    }
}
