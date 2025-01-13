package com.example.myschedule.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegistrationDTO {

    @NotEmpty(message = "Firstname should not be empty!")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First name should contain only Latin characters!")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty!")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Last name should contain only Latin characters!")
    private String lastName;

    @NotEmpty(message = "Username should not be empty!")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username should contain only Latin characters and numbers!")
    @Size(min = 5, max = 15, message = "Username should be between 5 and 15 characters!")
    private String userName;

    @NotEmpty(message = "E-mail should not be empty!")
    @Email(message = "E-mail should be valid!")
    private String email;

    @NotEmpty(message = "Password should not be empty!")
    @Size(min = 8, max = 16, message = "Password must be 8-16 characters long.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}$", message = "Password must contain at least one letter and one number.")
    private String password;

    @NotEmpty(message = "Confirm your password!")
    private String confirmPassword;

}
