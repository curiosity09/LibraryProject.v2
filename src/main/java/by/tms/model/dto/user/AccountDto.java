package by.tms.dto.user;

import by.tms.entity.user.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class AccountDto implements Serializable {

    private Long id;
    private String username;
    private String password;
    private UserDataDto userData;
    private boolean isBanned;
    private Level level;
}
