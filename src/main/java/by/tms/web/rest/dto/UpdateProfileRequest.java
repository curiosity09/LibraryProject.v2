package by.tms.web.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body for the current user profile update. {@code password} is optional:
 * when omitted or blank the current password hash is preserved.
 */
public record UpdateProfileRequest(@Pattern(regexp = "^$|.{4,20}$") String password,
                                   @NotBlank String name,
                                   @NotBlank String surname,
                                   @NotBlank @Size(max = 13) String phoneNumber,
                                   @Email String emailAddress) {
}
