package com.dev.libraryManagementSystem.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerUpdateDTO {
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    private String password;
}