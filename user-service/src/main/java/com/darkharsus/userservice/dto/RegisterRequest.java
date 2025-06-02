package com.darkharsus.userservice.dto;

import com.darkharsus.userservice.enums.Role;
import com.darkharsus.userservice.validation.ValidEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_EMAIL;
import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_NAME;
import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_PASSWORD;
import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_PHONE_NUMBER;
import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_ROLE;
import static com.darkharsus.userservice.constants.ProjectConstants.BLANK_SURNAME;
import static com.darkharsus.userservice.constants.ProjectConstants.INVALID_EMAIL_FORMAT;
import static com.darkharsus.userservice.constants.ProjectConstants.ROLE_NOT_EXIST;

@Setter
@Getter
public class RegisterRequest {

    @Email(message = INVALID_EMAIL_FORMAT)
    @NotBlank(message = BLANK_EMAIL)
    private String email;

    @NotBlank(message = BLANK_PASSWORD)
    private String password;

    @NotNull(message = BLANK_ROLE)
    @ValidEnum(enumClass = Role.class, message = ROLE_NOT_EXIST)
    private String role;

    @NotBlank(message = BLANK_NAME)
    private String name;

    @NotBlank(message = BLANK_SURNAME)
    private String surname;

    @NotBlank(message = BLANK_PHONE_NUMBER)
    private String phoneNumber;
}