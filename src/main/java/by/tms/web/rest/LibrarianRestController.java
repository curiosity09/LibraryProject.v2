package by.tms.web.rest;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.service.AccountService;
import by.tms.web.rest.util.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read-only helpers used by the librarian area (e.g. the "find user order" form).
 */
@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianRestController {

    private final AccountService accountService;

    @GetMapping("/users")
    public List<AccountDto> users() {
        return AccountUtils.sanitize(accountService.findAllUsers(Integer.MAX_VALUE, 0));
    }
}
