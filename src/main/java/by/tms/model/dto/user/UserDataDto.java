package by.tms.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
public class UserDataDto implements Serializable {

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    @Size(max = 13)
    private String phoneNumber;
    @Email
    private String emailAddress;
}
