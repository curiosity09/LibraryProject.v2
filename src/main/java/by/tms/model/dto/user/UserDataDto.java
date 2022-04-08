package by.tms.model.dto.user;

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
@EqualsAndHashCode
@Builder
@ToString
public class UserDataDto implements Serializable {

    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
}
