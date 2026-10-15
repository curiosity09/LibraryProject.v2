package by.tms.web.rest;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.dto.user.UserDataDto;
import by.tms.model.service.AccountService;
import by.tms.web.rest.dto.LoginRequest;
import by.tms.web.rest.dto.RegisterRequest;
import by.tms.web.rest.util.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.NoSuchElementException;

/**
 * JSON authentication endpoints replacing the former Spring Security form login.
 * A successful login stores the {@link SecurityContext} in the HTTP session,
 * the SPA then simply keeps the {@code JSESSIONID} cookie.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthRestController {

    private static final String USER_ROLE = "user";

    private final AccountService accountService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public AccountDto login(@Valid @RequestBody LoginRequest request,
                            HttpServletRequest httpRequest,
                            HttpServletResponse httpResponse) {
        Authentication authentication = authenticate(request.username(), request.password(), httpRequest);
        AccountDto account = accountService.findAccountByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        if (account.isBanned()) {
            throw new AccessDeniedException("Account is blocked");
        }
        persistContext(authentication, httpRequest, httpResponse);
        log.debug("Account {} logged in", account.getUsername());
        return AccountUtils.sanitize(account);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto register(@Valid @RequestBody RegisterRequest request,
                               HttpServletRequest httpRequest,
                               HttpServletResponse httpResponse) {
        if (accountService.findAccountByUsername(request.username()).isPresent()) {
            throw new IllegalStateException("Username is already taken");
        }
        AccountDto accountDto = AccountDto.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(USER_ROLE)
                .userData(UserDataDto.builder()
                        .name(request.name())
                        .surname(request.surname())
                        .phoneNumber(request.phoneNumber())
                        .emailAddress(request.emailAddress())
                        .build())
                .build();
        accountService.saveUser(accountDto);

        Authentication authentication = authenticate(request.username(), request.password(), httpRequest);
        persistContext(authentication, httpRequest, httpResponse);
        log.debug("Account {} registered", request.username());

        return AccountUtils.sanitize(findRequired(request.username()));
    }

    @GetMapping("/me")
    public AccountDto me(Authentication authentication) {
        if (authentication == null) {
            throw new UsernameNotFoundException("Not authenticated");
        }
        return AccountUtils.sanitize(findRequired(authentication.getName()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        log.debug("Account logged out");
    }

    private AccountDto findRequired(String username) {
        return accountService.findAccountByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + username));
    }

    private Authentication authenticate(String username, String rawPassword, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, rawPassword);
        token.setDetails(new WebAuthenticationDetails(request));
        return authenticationManager.authenticate(token);
    }

    private void persistContext(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
