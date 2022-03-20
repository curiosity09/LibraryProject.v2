package by.tms.service;

import by.tms.dto.user.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountDto> findAllUsers();

    List<AccountDto> findAllDebtors();

    List<AccountDto> findAllAdmins();

    List<AccountDto> findAllLibrarians();

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
