package by.tms.service.impl;

import by.tms.dao.AccountDao;
import by.tms.dto.user.AccountDto;
import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import by.tms.mapper.impl.AccountMapper;
import by.tms.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final AccountMapper accountMapper = AccountMapper.getInstance();

    @Override
    public List<AccountDto> findAllUsers() {
        List<User> allUsers = accountDao.findAllUsers();
        return accountMapper.mapUserToListDto(allUsers);
    }

    @Override
    public List<AccountDto> findAllDebtors() {
        List<User> allDebtors = accountDao.findAllDebtors();
        return accountMapper.mapUserToListDto(allDebtors);
    }

    @Override
    public List<AccountDto> findAllAdmins() {
        List<Admin> allAdmins = accountDao.findAllAdmins();
        return accountMapper.mapAdminToListDto(allAdmins);
    }

    @Override
    public List<AccountDto> findAllLibrarians() {
        List<Librarian> allLibrarians = accountDao.findAllLibrarians();
        return accountMapper.mapLibrarianToListDto(allLibrarians);
    }

    @Override
    public Optional<AccountDto> findAccountByUsername(String username) {
        Optional<Account> byUsername = accountDao.findByUsername(username);
        return Optional.ofNullable(accountMapper.mapToDto(byUsername.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findUserByUsername(String username) {
        Optional<User> optionalUser = accountDao.findUserByUsername(username);
        return Optional.ofNullable(accountMapper.mapUserToDto(optionalUser.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findUserById(Long id) {
        Optional<User> userById = accountDao.findUserById(id);
        return Optional.ofNullable(accountMapper.mapUserToDto(userById.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findLibrarianByUsername(String username) {
        Optional<Librarian> optionalLibrarian = accountDao.findLibrarianByUsername(username);
        return Optional.ofNullable(accountMapper.mapLibrarianToDto(optionalLibrarian.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findLibrarianById(Long id) {
        Optional<Librarian> librarianById = accountDao.findLibrarianById(id);
        return Optional.ofNullable(accountMapper.mapLibrarianToDto(librarianById.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findAdminById(Long id) {
        Optional<Admin> adminById = accountDao.findAdminById(id);
        return Optional.ofNullable(accountMapper.mapAdminToDto(adminById.orElse(null)));
    }

    @Override
    public Optional<AccountDto> findAdminByUsername(String username) {
        Optional<Admin> optionalAdmin = accountDao.findAdminByUsername(username);
        return Optional.ofNullable(accountMapper.mapAdminToDto(optionalAdmin.orElse(null)));
    }

    @Override
    @Transactional
    public void blockUser(AccountDto userDto) {
        accountDao.blockUser((User) accountMapper.mapToEntity(userDto));
    }

    @Override
    @Transactional
    public void unblockUser(AccountDto userDto) {
        accountDao.unblockUser((User) accountMapper.mapToEntity(userDto));
    }

    @Override
    @Transactional
    public Long saveUser(AccountDto userDto) {
        Account account = accountMapper.mapToEntity(userDto);
        if (Objects.nonNull(account)) {
            return accountDao.save(account);
        }
        return 0L;
    }

    @Override
    public Long saveLibrarian(AccountDto libDto) {
        return accountDao.save(accountMapper.mapLibrarianToEntity(libDto));
    }

    @Override
    public Long saveAdmin(AccountDto adminDto) {
        return accountDao.save(accountMapper.mapAdminToEntity(adminDto));
    }

    @Override
    @Transactional
    public void updateUser(AccountDto userDto) {
        accountDao.update(accountMapper.mapToEntity(userDto));
    }

    @Override
    public void updateLibrarian(AccountDto libDto) {
        accountDao.update(accountMapper.mapLibrarianToEntity(libDto));
    }

    @Override
    public void updateAdmin(AccountDto adminDto) {
        accountDao.update(accountMapper.mapAdminToEntity(adminDto));
    }

    @Override
    @Transactional
    public void deleteAccount(AccountDto accountDto) {
        accountDao.delete(accountMapper.mapToEntity(accountDto));
    }

    @Override
    public boolean isAccountExist(Long id) {
        return accountDao.isExist(id);
    }
}
