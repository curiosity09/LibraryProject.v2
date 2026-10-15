package by.tms.web.rest.util;

import by.tms.model.dto.user.AccountDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * REST layer helpers for {@link AccountDto}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountUtils {

    /**
     * Removes the (hashed) password before the account is serialized to JSON.
     * The password hash must never leave the server.
     */
    public static AccountDto sanitize(AccountDto account) {
        if (account != null) {
            account.setPassword(null);
        }
        return account;
    }

    public static List<AccountDto> sanitize(List<AccountDto> accounts) {
        if (accounts != null) {
            accounts.forEach(AccountUtils::sanitize);
        }
        return accounts;
    }
}
