package by.tms.web.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body used by the admin to create a librarian or an admin account.
 */
public record EmployeeRequest(@NotBlank @Size(min = 3, max = 20) String username,
                              @NotBlank @Size(min = 4, max = 20) String password,
                              @NotBlank @Pattern(regexp = "librarian|admin") String role,
                              @Pattern(regexp = "HEAD|SECONDARY") String level,
                              @NotBlank String name,
                              @NotBlank String surname,
                              @NotBlank @Size(max = 13) String phoneNumber,
                              @Email String emailAddress) {
}
