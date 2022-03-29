package by.tms.model.service;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.entity.user.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService extends GenericService<AccountDto, Long, Account> {

    List<AccountDto> findAllUsers(int limit, int offset);

    List<Long> getCountPages(Class<? extends Account> clazz);

    List<AccountDto> findAllDebtors();

    List<AccountDto> findAllAdmins(int limit, int offset);

    List<AccountDto> findAllLibrarians(int limit, int offset);

    Optional<AccountDto> findAccountByUsername(String username);

    void blockUser(AccountDto userDto);

    void unblockUser(AccountDto userDto);

    Long saveUser(AccountDto userDto);

    Long saveLibrarian(AccountDto libDto);

    Long saveAdmin(AccountDto adminDto);

    void updateUser(AccountDto userDto);

    void updateLibrarian(AccountDto libDto);

    void updateAdmin(AccountDto adminDto);
}
