package com.example.myschedule.dto;

import com.example.myschedule.entity.Role;
import com.example.myschedule.entity.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

    @NotEmpty(message = "Firstname should not be empty!")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Firstname should contain only Latin characters!")
    private String firstname;

    @NotEmpty(message = "Lastname should not be empty!")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Lastname should contain only Latin characters!")
    private String lastname;

    @NotEmpty(message = "Username should not be empty!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username should contain only Latin characters and numbers!")
    @Size(min = 5, max = 15, message = "Username should be between 5 and 15 characters!")
    private String username;

    @NotEmpty(message = "E-mail should not be empty!")
    @Email(message = "E-mail should be valid!")
    private String email;

    @NotEmpty(message = "Password should not be empty!")
    @Size(min = 8, max = 16, message = "Password must be 8-16 characters long!")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character!")
    private String password;

    @NotNull(message = "Status should not be null!")
    private Status status;

    @NotNull(message = "Role should not be null!")
    private Role role;

}