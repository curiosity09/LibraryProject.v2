package by.tms.mapper.impl;

import by.tms.dto.user.AccountDto;
import by.tms.dto.user.UserDataDto;
import by.tms.entity.user.Account;
import by.tms.entity.user.Admin;
import by.tms.entity.user.Librarian;
import by.tms.entity.user.User;
import by.tms.mapper.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper implements Mapper<Account, AccountDto> {

    private static final AccountMapper INSTANCE = new AccountMapper();

    public static AccountMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public AccountDto mapToDto(Account account) {
        if (Objects.nonNull(account)) {
            return AccountDto.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .userData(UserDataDto.builder()
                            .name(account.getUserData().getName())
                            .surname(account.getUserData().getSurname())
                            .emailAddress(account.getUserData().getEmailAddress())
                            .phoneNumber(account.getUserData().getPhoneNumber())
                            .build())
                    .build();
        }
        return null;
    }

    @Override
    public List<AccountDto> mapToListDto(List<Account> accounts) {
        if (Objects.nonNull(accounts)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (Account account : accounts) {
                accountDtoList.add(mapToDto(account));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public List<AccountDto> mapLibrarianToListDto(List<Librarian> librarians) {
        if (Objects.nonNull(librarians)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (Librarian librarian : librarians) {
                accountDtoList.add(mapLibrarianToDto(librarian));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public List<AccountDto> mapAdminToListDto(List<Admin> admins) {
        if (Objects.nonNull(admins)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (Admin admin : admins) {
                accountDtoList.add(mapAdminToDto(admin));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public List<AccountDto> mapUserToListDto(List<User> users) {
        if (Objects.nonNull(users)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (User user : users) {
                accountDtoList.add(mapUserToDto(user));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public AccountDto mapUserToDto(User user) {
        if (Objects.nonNull(user)) {
            AccountDto accountDto = mapToDto(user);
            accountDto.setBanned(user.isBanned());
            return accountDto;
        }
        return null;
    }

    public AccountDto mapLibrarianToDto(Librarian librarian) {
        if (Objects.nonNull(librarian)) {
            AccountDto accountDto = mapToDto(librarian);
            accountDto.setLevel(librarian.getLibLevel());
            return accountDto;
        }
        return null;
    }

    public AccountDto mapAdminToDto(Admin admin) {
        if (Objects.nonNull(admin)) {
            AccountDto accountDto = mapToDto(admin);
            accountDto.setLevel(admin.getAdminLevel());
            return accountDto;
        }
        return null;
    }
}
