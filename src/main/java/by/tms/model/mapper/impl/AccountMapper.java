package by.tms.model.mapper.impl;

import by.tms.model.dto.user.AccountDto;
import by.tms.model.dto.user.UserDataDto;
import by.tms.model.entity.user.Account;
import by.tms.model.entity.user.Admin;
import by.tms.model.entity.user.Librarian;
import by.tms.model.entity.user.User;
import by.tms.model.entity.user.UserData;
import by.tms.model.mapper.Mapper;
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

    private AccountDto toDto(Account account) {
        if (Objects.nonNull(account)) {
            AccountDto accountDto = AccountDto.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .role(account.getRole())
                    .userData(UserDataDto.builder().build())
                    .password(account.getPassword())
                    .build();
            if (Objects.nonNull(account.getUserData())) {
                UserDataDto userDataDto = UserDataDto.builder()
                        .name(account.getUserData().getName())
                        .surname(account.getUserData().getSurname())
                        .emailAddress(account.getUserData().getEmailAddress())
                        .phoneNumber(account.getUserData().getPhoneNumber())
                        .build();
                accountDto.setUserData(userDataDto);
            }
            return accountDto;
        }
        return null;
    }

    @Override
    public AccountDto mapToDto(Account account) {
        AccountDto accountDto = null;
        if (account instanceof User) {
            accountDto = toDto(account);
            assert accountDto != null;
            accountDto.setBanned(((User) account).isBanned());
        } else if (account instanceof Librarian) {
            accountDto = toDto(account);
            assert accountDto != null;
            accountDto.setLevel(((Librarian) account).getLibLevel());
        } else if (account instanceof Admin) {
            accountDto = toDto(account);
            assert accountDto != null;
            accountDto.setLevel(((Admin) account).getAdminLevel());
        }
        return accountDto;
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

    @Override
    public Account mapToEntity(AccountDto userDto) {
        if (Objects.nonNull(userDto)) {
            User user = User.builder()
                    .username(userDto.getUsername())
                    .role(userDto.getRole())
                    .userData(UserData.builder().build())
                    .password(userDto.getPassword())
                    .isBanned(userDto.isBanned())
                    .build();
            user.setId(userDto.getId());
            if (Objects.nonNull(userDto.getUserData())) {
                UserData userData = UserData.builder()
                        .name(userDto.getUserData().getName())
                        .surname(userDto.getUserData().getSurname())
                        .phoneNumber(userDto.getUserData().getPhoneNumber())
                        .emailAddress(userDto.getUserData().getEmailAddress())
                        .build();
                user.setUserData(userData);
            }
            return user;
        }
        return null;
    }

    public Account mapLibrarianToEntity(AccountDto libDto) {
        if (Objects.nonNull(libDto)) {
            Librarian librarian = Librarian.builder()
                    .username(libDto.getUsername())
                    .password(libDto.getPassword())
                    .role(libDto.getRole())
                    .userData(UserData.builder()
                            .name(libDto.getUserData().getName())
                            .surname(libDto.getUserData().getSurname())
                            .phoneNumber(libDto.getUserData().getPhoneNumber())
                            .emailAddress(libDto.getUserData().getEmailAddress())
                            .build())
                    .libLevel(libDto.getLevel())
                    .build();
            librarian.setId(libDto.getId());
            return librarian;
        }
        return null;
    }

    public Account mapAdminToEntity(AccountDto adminDto) {
        if (Objects.nonNull(adminDto)) {
            Admin admin = Admin.builder()
                    .username(adminDto.getUsername())
                    .password(adminDto.getPassword())
                    .role(adminDto.getRole())
                    .userData(UserData.builder()
                            .name(adminDto.getUserData().getName())
                            .surname(adminDto.getUserData().getSurname())
                            .phoneNumber(adminDto.getUserData().getPhoneNumber())
                            .emailAddress(adminDto.getUserData().getEmailAddress())
                            .build())
                    .adminLevel(adminDto.getLevel())
                    .build();
            admin.setId(adminDto.getId());
            return admin;
        }
        return null;
    }

    public List<AccountDto> mapLibrarianToListDto(List<Librarian> librarians) {
        if (Objects.nonNull(librarians)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (Librarian librarian : librarians) {
                accountDtoList.add(mapToDto(librarian));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public List<AccountDto> mapAdminToListDto(List<Admin> admins) {
        if (Objects.nonNull(admins)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (Admin admin : admins) {
                accountDtoList.add(mapToDto(admin));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }

    public List<AccountDto> mapUserToListDto(List<User> users) {
        if (Objects.nonNull(users)) {
            List<AccountDto> accountDtoList = new ArrayList<>();
            for (User user : users) {
                accountDtoList.add(mapToDto(user));
            }
            return accountDtoList;
        }
        return Collections.emptyList();
    }
}
