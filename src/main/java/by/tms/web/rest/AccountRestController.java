package by.tms.web.rest;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.dto.user.UserDataDto;
import by.tms.model.service.AccountService;
import by.tms.web.rest.dto.UpdateProfileRequest;
import by.tms.web.rest.util.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Operations on the currently authenticated account.
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountRestController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('user')")
    public AccountDto updateProfile(@Valid @RequestBody UpdateProfileRequest request, Authentication authentication) {
        AccountDto account = accountService.findAccountByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("Account not found"));

        UserDataDto userData = account.getUserData() == null ? UserDataDto.builder().build() : account.getUserData();
        userData.setName(request.name());
        userData.setSurname(request.surname());
        userData.setPhoneNumber(request.phoneNumber());
        userData.setEmailAddress(request.emailAddress());
        account.setUserData(userData);
        if (StringUtils.hasText(request.password())) {
            account.setPassword(passwordEncoder.encode(request.password()));
        }

        accountService.updateUser(account);
        log.debug("Profile of {} updated", account.getUsername());
        return AccountUtils.sanitize(accountService.findAccountByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("Account not found")));
    }
}
