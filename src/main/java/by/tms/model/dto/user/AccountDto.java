package by.tms.model.dto.user;

import by.tms.model.entity.user.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
    @NotNull
    private String role;
    private UserDataDto userData;
    private boolean isBanned;
    private Level level;
    @Min(0)
    private Integer ordersAmount;
}
