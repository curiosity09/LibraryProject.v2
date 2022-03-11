package by.tms.service;

import by.tms.dto.user.AccountDto;
import by.tms.entity.user.Account;
import by.tms.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDto> findAllUsers();

    List<AccountDto> findAllDebtors();

    List<AccountDto> findAllAdmins();

    List<AccountDto> findAllLibrarians();

    Optional<AccountDto> findUserByUsername(String username);

    Optional<AccountDto> findUserById(Long id);

    Optional<AccountDto> findLibrarianByUsername(String username);

    Optional<AccountDto> findLibrarianById(Long id);

    Optional<AccountDto> findAdminById(Long id);

    void blockUser(User user);

    void unblockUser(User user);

    Long saveAccount(Account entity);

    void updateAccount(Account entity);

    void deleteAccount(Account entity);

    boolean isAccountExist(Long id);
}
