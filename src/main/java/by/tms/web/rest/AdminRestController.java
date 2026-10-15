package by.tms.web.rest;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.dto.user.UserDataDto;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Level;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.service.AccountService;
import by.tms.web.rest.dto.EmployeeRequest;
import by.tms.web.rest.dto.PageResponse;
import by.tms.web.rest.util.AccountUtils;
import by.tms.web.rest.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Admin area: account listings, blocking, deletion and employee creation.
 * The whole {@code /api/admin/**} tree is restricted to the {@code admin} authority in {@code SecurityConfig}.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminRestController {

    private static final String LIBRARIAN_ROLE = "librarian";

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public PageResponse<AccountDto> users(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<AccountDto> content = AccountUtils.sanitize(
                accountService.findAllUsers(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized)));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(accountService.getCountPages(User.class)));
    }

    @GetMapping("/admins")
    public PageResponse<AccountDto> admins(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<AccountDto> content = AccountUtils.sanitize(
                accountService.findAllAdmins(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized)));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(accountService.getCountPages(Admin.class)));
    }

    @GetMapping("/librarians")
    public PageResponse<AccountDto> librarians(@RequestParam(defaultValue = "0") int page) {
        int normalized = PagingUtils.normalizePage(page);
        List<AccountDto> content = AccountUtils.sanitize(
                accountService.findAllLibrarians(PagingUtils.PAGE_SIZE, PagingUtils.offset(normalized)));
        return PageResponse.of(content, normalized, PagingUtils.PAGE_SIZE,
                PagingUtils.totalPages(accountService.getCountPages(Librarian.class)));
    }

    @GetMapping("/debtors")
    public List<AccountDto> debtors() {
        return AccountUtils.sanitize(accountService.findAllDebtors());
    }

    @PostMapping("/users/{username}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable String username) {
        AccountDto account = accountService.findAccountByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + username));
        accountService.blockUser(account);
        log.debug("Account {} blocked", username);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        AccountDto account = accountService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + id));
        if (account.getOrdersAmount() != null && account.getOrdersAmount() > 0) {
            throw new IllegalStateException("Cannot delete an account that still has orders");
        }
        accountService.delete(account);
        log.debug("Account {} deleted", id);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto addEmployee(@Valid @RequestBody EmployeeRequest request) {
        if (accountService.findAccountByUsername(request.username()).isPresent()) {
            throw new IllegalStateException("Username is already taken");
        }
        AccountDto employee = AccountDto.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .level(request.level() == null ? Level.SECONDARY : Level.valueOf(request.level()))
                .userData(UserDataDto.builder()
                        .name(request.name())
                        .surname(request.surname())
                        .phoneNumber(request.phoneNumber())
                        .emailAddress(request.emailAddress())
                        .build())
                .build();

        if (LIBRARIAN_ROLE.equals(request.role())) {
            accountService.saveLibrarian(employee);
        } else {
            accountService.saveAdmin(employee);
        }
        log.debug("Employee {} created with role {}", request.username(), request.role());

        return AccountUtils.sanitize(accountService.findAccountByUsername(request.username())
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + request.username())));
    }
}
