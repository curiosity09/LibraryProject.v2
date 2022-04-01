package by.tms.web.controller;

import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SessionAttributes({ACCOUNT_ATTRIBUTE, SHOPPING_CART_ATTRIBUTE})
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String loginPage() {
        return INDEX_PAGE_SUFFIX;
    }

    @GetMapping(REGISTER_PAGE)
    public String registerPage(Model model) {
        model.addAttribute(ACCOUNT_ATTRIBUTE, AccountDto.builder().build());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, AccountDto account, HttpServletRequest request) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(account.getUsername());
            if (accountByUsername.isEmpty()) {
                String rawPassword = account.getPassword();
                account.setPassword(passwordEncoder.encode(rawPassword));
                accountService.saveUser(account);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(account.getUsername(), rawPassword);
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                return REDIRECT + "/success";
            }
            return REDIRECT + REGISTER_PAGE;
        }
        return REDIRECT + REGISTER_PAGE;
    }

    @GetMapping("/success")
    public String success(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User account,
            Model model) {
        Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(account.getUsername());
        if (accountByUsername.isPresent()) {
            AccountDto accountDto = accountByUsername.get();
            if (!accountDto.isBanned()) {
                model.addAttribute(ACCOUNT_ATTRIBUTE, accountDto);
                switch (accountDto.getRole()) {
                    case "user":
                        ShoppingCart shoppingCart = new ShoppingCart(new ArrayList<>());
                        model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
                        return REDIRECT + USER_PAGE;
                    case "librarian":
                        return REDIRECT + LIBRARIAN_PAGE;
                    case "admin":
                        return REDIRECT + ADMIN_PAGE;
                    default:
                        return REDIRECT + ERROR_PAGE;
                }
            }
            return REDIRECT + BAN_PAGE;
        }
        return REDIRECT + ERROR_PAGE;
    }

    @GetMapping("/user/userPage")
    public String userPage() {
        return USER_PREFIX + USER_PAGE_SUFFIX;
    }

    @GetMapping("/librarian/librarianPage")
    public String librarianPage() {
        return LIBRARIAN_PREFIX + LIBRARIAN_PAGE_SUFFIX;
    }

    @GetMapping("/admin/adminPage")
    public String adminPage() {
        return ADMIN_PREFIX + ADMIN_PAGE_SUFFIX;
    }

/*    @PostMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return REDIRECT + START_PAGE;
    }*/

    @GetMapping("/errorPage")
    public String errorPage() {
        return ERROR_PAGE_PREFIX;
    }

    @GetMapping("/banPage")
    public String banPage() {
        return BAN_PAGE_PREFIX;
    }

    @GetMapping("/user/showUserInfo")
    public String showUserInfo(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findAccountByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute(ACCOUNT_ATTRIBUTE, accountDto));
        return USER_PREFIX + USER_INFO_PAGE_PREFIX;
    }

    @GetMapping("/user/editUserPage")
    public String editUserPage(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findAccountByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute(ACCOUNT_ATTRIBUTE, accountDto));
        return USER_PREFIX + EDIT_USER_PREFIX;
    }

    @PostMapping("/editUser")
    public String editUser(Model model, AccountDto account) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));

            accountService.updateUser(account);
            model.addAttribute(ACCOUNT_ATTRIBUTE, account);
            return REDIRECT + USER_PAGE;
        } else {
            return REDIRECT + ERROR_PAGE;
        }
    }

    @GetMapping("/librarian/findUserOrderPage")
    public String findUserOrderPage(Model model) {
        model.addAttribute(ALL_USERS_ATTRIBUTE, accountService.findAllUsers(Integer.MAX_VALUE, OFFSET_ZERO));
        return LIBRARIAN_PREFIX + FIND_USER_ORDER_PREFIX;
    }

    @GetMapping("/admin/allUsersPage")
    public String allUsersPage(Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allUsers", accountService.findAllUsers(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(User.class));
        return ADMIN_PREFIX + "allUsers";
    }

    @GetMapping("/admin/allAdminsPage")
    public String allAdminsPage(Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allAdmins", accountService.findAllAdmins(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(Admin.class));
        return ADMIN_PREFIX + "allAdmins";
    }

    @GetMapping("/admin/allLibrariansPage")
    public String allLibrariansPage(Model model,
                                    @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allLibrarians", accountService.findAllLibrarians(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(Librarian.class));
        return ADMIN_PREFIX + "allLibrarians";
    }

    @GetMapping("/admin/blockUserPage")
    public String blockUserPage(Model model) {
        model.addAttribute("debtors", accountService.findAllDebtors());
        return ADMIN_PREFIX + "blockUser";
    }

    @PostMapping("/blockUser")
    public String blockUser(@RequestParam(USERNAME_PARAMETER) String username) {
        Optional<AccountDto> userByUsername = accountService.findAccountByUsername(username);
        if (userByUsername.isPresent()) {
            accountService.blockUser(userByUsername.get());
            return REDIRECT + ADMIN_PAGE;
        }
        return REDIRECT + "/errorPage";
    }

    @GetMapping("/admin/addEmployeePage")
    public String addEmployeePage(Model model) {
        model.addAttribute(ACCOUNT_ATTRIBUTE, AccountDto.builder().build());
        return ADMIN_PREFIX + "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(AccountDto account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (Objects.equals("librarian", account.getRole())) {
            accountService.saveLibrarian(account);
            return REDIRECT + ADMIN_PAGE;
        } else if (Objects.equals("admin", account.getRole())) {
            accountService.saveAdmin(account);
            return REDIRECT + ADMIN_PAGE;
        } else {
            return REDIRECT + ERROR_PAGE;
        }
    }

    @GetMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model) {
        Optional<AccountDto> userById = accountService.findById(Long.parseLong(id));
        userById.ifPresent(user -> {
            if (user.getOrdersAmount() == 0) {
                accountService.delete(user);
            } else {
                model.addAttribute("cannotDel", true);
            }
        });
        return REDIRECT + "/admin/allUsersPage";
    }
}
