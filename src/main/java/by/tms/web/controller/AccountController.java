package by.tms.web.controller;

import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static by.tms.web.util.PageUtil.*;

@Controller
@SessionAttributes({ACCOUNT_ATTRIBUTE, SHOPPING_CART_ATTRIBUTE})
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String loginPage() {
        return INDEX_PAGE_SUFFIX;
    }

    @PostMapping("/login")
    public String login(AccountDto accountDto, Model model) {
        Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(accountDto.getUsername());
        if (accountByUsername.isPresent()) {
            AccountDto account = accountByUsername.get();
            if (Objects.equals(account.getPassword(), accountDto.getPassword())) {
                model.addAttribute(ACCOUNT_ATTRIBUTE, account);
                ShoppingCart shoppingCart = new ShoppingCart(new ArrayList<>());
                model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
                if (Objects.equals("user", account.getRole())) {
                    return REDIRECT + USER_PAGE;
                } else if (Objects.equals("librarian", account.getRole())) {
                    return REDIRECT + LIBRARIAN_PAGE;
                } else if (Objects.equals("admin", account.getRole())) {
                    return REDIRECT + ADMIN_PAGE;
                } else {
                    return "";
                }
            } else {
                return REDIRECT + START_PAGE;
            }
        } else {
            return REDIRECT + START_PAGE;
        }
    }

    @GetMapping(REGISTER_PAGE)
    public String registerPage(Model model) {
        model.addAttribute(ACCOUNT_ATTRIBUTE, AccountDto.builder().build());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, AccountDto account) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(account.getUsername());
            if (accountByUsername.isEmpty()) {
                accountService.saveUser(account);
                model.addAttribute(ACCOUNT_ATTRIBUTE, account);
                ShoppingCart shoppingCart = new ShoppingCart(new ArrayList<>());
                model.addAttribute(SHOPPING_CART_ATTRIBUTE, shoppingCart);
                return REDIRECT + USER_PAGE;
            }
            return REDIRECT + REGISTER_PAGE;
        }
        return REDIRECT + REGISTER_PAGE;
    }

    @GetMapping(USER_PAGE)
    public String userPage() {
        return USER_PREFIX + USER_PAGE_SUFFIX;
    }

    @GetMapping(LIBRARIAN_PAGE)
    public String librarianPage() {
        return LIBRARIAN_PREFIX + LIBRARIAN_PAGE_SUFFIX;
    }

    @GetMapping(ADMIN_PAGE)
    public String adminPage() {
        return ADMIN_PREFIX + ADMIN_PAGE_SUFFIX;
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return REDIRECT + START_PAGE;
    }

    @GetMapping("/errorPage")
    public String errorPage() {
        return ERROR_PAGE;
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findAccountByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute(ACCOUNT_ATTRIBUTE, accountDto));
        return USER_PREFIX + USER_INFO_PAGE;
    }

    @GetMapping("/editUserPage")
    public String editUserPage(@SessionAttribute(ACCOUNT_ATTRIBUTE) AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findAccountByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute(ACCOUNT_ATTRIBUTE, accountDto));
        return USER_PREFIX + "editUser";
    }

    @PostMapping("/editUser")
    public String editUser(Model model, AccountDto account) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            accountService.updateUser(account);
            model.addAttribute(ACCOUNT_ATTRIBUTE, account);
            return REDIRECT + USER_PAGE;
        } else {
            return REDIRECT + ERROR_PAGE;
        }
    }

    @GetMapping("/findUserOrderPage")
    public String findUserOrderPage(Model model) {
        model.addAttribute(ALL_USERS_PAGE, accountService.findAllUsers(Integer.MAX_VALUE, OFFSET_ZERO));
        return LIBRARIAN_PREFIX + FIND_USER_ORDER_PAGE;
    }

    @GetMapping("/allUsersPage")
    public String allUsersPage(Model model,
                               @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allUsers", accountService.findAllUsers(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(User.class));
        return ADMIN_PREFIX + "allUsers";
    }

    @GetMapping("/allAdminsPage")
    public String allAdminsPage(Model model,
                                @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allAdmins", accountService.findAllAdmins(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(Admin.class));
        return ADMIN_PREFIX + "allAdmins";
    }

    @GetMapping("/allLibrariansPage")
    public String allLibrariansPage(Model model,
                                    @RequestParam(name = OFFSET_PARAMETER, defaultValue = "0") String offset) {
        model.addAttribute("allLibrarians", accountService.findAllLibrarians(LIMIT_TEN, Integer.parseInt(offset)));
        model.addAttribute(COUNT_PAGES_ATTRIBUTE, accountService.getCountPages(Librarian.class));
        return ADMIN_PREFIX + "allLibrarians";
    }

    @GetMapping("/blockUserPage")
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

    @GetMapping("/addEmployeePage")
    public String addEmployeePage(Model model) {
        model.addAttribute(ACCOUNT_ATTRIBUTE, AccountDto.builder().build());
        return ADMIN_PREFIX + "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(AccountDto account) {
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

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        Optional<AccountDto> userById = accountService.findAccountById(Long.parseLong(id));
        userById.ifPresent(accountService::deleteAccount);
        return REDIRECT + "/allUsersPage";
    }
}
