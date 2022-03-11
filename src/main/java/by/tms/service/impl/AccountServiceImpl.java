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
    @Transactional
    public void blockUser(User user) {
        accountDao.blockUser(user);
    }

    @Override
    @Transactional
    public void unblockUser(User user) {
        accountDao.unblockUser(user);
    }

    @Override
    @Transactional
    public Long saveAccount(Account account) {
        return accountDao.save(account);
    }

    @Override
    @Transactional
    public void updateAccount(Account account) {
        accountDao.update(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Account account) {
        accountDao.delete(account);
    }

    @Override
    public boolean isAccountExist(Long id) {
        return accountDao.isExist(id);
    }
}
