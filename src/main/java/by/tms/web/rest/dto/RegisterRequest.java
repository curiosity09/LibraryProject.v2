package by.tms.web.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank @Size(min = 3, max = 20) String username,
                              @NotBlank @Size(min = 4, max = 20) String password,
                              @NotBlank String name,
                              @NotBlank String surname,
                              @NotBlank @Size(max = 13) String phoneNumber,
                              @Email String emailAddress) {
}
