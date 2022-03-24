package by.tms.model.service;

import by.tms.model.dto.user.AccountDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface AccountService extends UserDetailsService {

    List<AccountDto> findAllUsers(int limit, int offset);

    List<AccountDto> findAllDebtors(int limit, int offset);

    List<AccountDto> findAllAdmins(int limit, int offset);

    List<AccountDto> findAllLibrarians(int limit, int offset);

    Optional<AccountDto> findAccountByUsername(String username);

    Optional<AccountDto> findUserByUsername(String username);

    Optional<AccountDto> findUserById(Long id);

    Optional<AccountDto> findLibrarianByUsername(String username);

    Optional<AccountDto> findLibrarianById(Long id);

    Optional<AccountDto> findAdminById(Long id);

    Optional<AccountDto> findAdminByUsername(String username);

    void blockUser(AccountDto userDto);

    void unblockUser(AccountDto userDto);

    Long saveUser(AccountDto userDto);

    Long saveLibrarian(AccountDto libDto);

    Long saveAdmin(AccountDto adminDto);

    void updateUser(AccountDto userDto);

    void updateLibrarian(AccountDto libDto);

    void updateAdmin(AccountDto adminDto);

    void deleteAccount(AccountDto accountDto);

    boolean isAccountExist(Long id);
}
