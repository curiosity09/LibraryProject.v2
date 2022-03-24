package by.tms.web.controller;

import by.tms.model.dto.ShoppingCart;
import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@SessionAttributes({"account", "shoppingCart"})
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ModelAttribute("allUsers")
    public List<AccountDto> allUsers() {
        return accountService.findAllUsers(10,0);
    }

    @ModelAttribute("allEmployees")
    public List<AccountDto> allEmployees() {
        List<AccountDto> allEmployees = new ArrayList<>();
        allEmployees.addAll(accountService.findAllLibrarians(10,0));
        allEmployees.addAll(accountService.findAllAdmins(10,0));
        return allEmployees;
    }

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(AccountDto accountDto, Model model) {
        Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(accountDto.getUsername());
        if (accountByUsername.isPresent()) {
            AccountDto account = accountByUsername.get();
            if (Objects.equals(account.getPassword(), accountDto.getPassword())) {
                model.addAttribute("account", account);
                ShoppingCart shoppingCart = new ShoppingCart(new ArrayList<>());
                model.addAttribute("shoppingCart", shoppingCart);
                if (Objects.equals("user", account.getRole())) {
                    return "redirect:/userPage";
                } else if (Objects.equals("librarian", account.getRole())) {
                    return "redirect:/librarianPage";
                } else if (Objects.equals("admin", account.getRole())) {
                    return "redirect:/adminPage";
                } else {
                    return "";
                }
            } else {
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/registerPage")
    public String registerPage(Model model) {
        model.addAttribute("account", AccountDto.builder().build());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, AccountDto account) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            Optional<AccountDto> accountByUsername = accountService.findAccountByUsername(account.getUsername());
            if (accountByUsername.isEmpty()) {
                accountService.saveUser(account);
                model.addAttribute("account", account);
                ShoppingCart shoppingCart = new ShoppingCart(new ArrayList<>());
                model.addAttribute("shoppingCart", shoppingCart);
                return "redirect:/userPage";
            }
            return "redirect:/registerPage";
        }
        return "redirect:/registerPage";
    }

    @GetMapping("/userPage")
    public String userPage() {
        return "page/user/user";
    }

    @GetMapping("/librarianPage")
    public String librarianPage() {
        return "page/librarian/librarian";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "page/admin/admin";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping("/errorPage")
    public String errorPage() {
        return "page/errorPage";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo(@SessionAttribute("account") AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findUserByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute("account", accountDto));
        return "page/user/userInfo";
    }

    @GetMapping("/editUserPage")
    public String editUserPage(@SessionAttribute("account") AccountDto account, Model model) {
        Optional<AccountDto> userByUsername = accountService.findUserByUsername(account.getUsername());
        userByUsername.ifPresent(accountDto -> model.addAttribute("account", accountDto));
        return "page/user/editUser";
    }

    @PostMapping("/editUser")
    public String editUser(Model model, AccountDto account) {
        if (Objects.nonNull(account.getUsername()) && Objects.nonNull(account.getPassword())) {
            accountService.updateUser(account);
            model.addAttribute("account", account);
            return "redirect:/userPage";
        } else {
            return "redirect:/errorPage";
        }
    }

    @GetMapping("/findUserOrderPage")
    public String findUserOrderPage(Model model) {
        model.addAttribute("allUsers", accountService.findAllUsers(10,0));
        return "page/librarian/findUserOrder";
    }

    @GetMapping("/allUsersPage")
    public String allUsersPage() {
        return "page/admin/allUsers";
    }

    @GetMapping("/allEmployeesPage")
    public String allEmployeesPage() {
        return "page/admin/allEmployees";
    }

    @GetMapping("/blockUserPage")
    public String blockUserPage(Model model) {
        model.addAttribute("debtors", accountService.findAllDebtors(10,0));
        return "page/admin/blockUser";
    }

    @PostMapping("/blockUser")
    public String blockUser(@RequestParam("username") String username) {
        Optional<AccountDto> userByUsername = accountService.findUserByUsername(username);
        if (userByUsername.isPresent()) {
            accountService.blockUser(userByUsername.get());
            return "redirect:/adminPage";
        }
        return "redirect:/errorPage";
    }

    @GetMapping("addEmployeePage")
    public String addEmployeePage(Model model) {
        model.addAttribute("account", AccountDto.builder().build());
        return "page/admin/addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(AccountDto account) {
        if (Objects.equals("librarian", account.getRole())) {
            accountService.saveLibrarian(account);
            return "redirect:/adminPage";
        } else if (Objects.equals("admin", account.getRole())) {
            accountService.saveAdmin(account);
            return "redirect:/adminPage";
        } else {
            return "redirect:/errorPage";
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        Optional<AccountDto> userById = accountService.findUserById(Long.parseLong(id));
        userById.ifPresent(accountService::deleteAccount);
        return "redirect:/allUsersPage";
    }
}
