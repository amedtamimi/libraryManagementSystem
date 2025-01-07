package com.dev.libraryManagementSystem.controller;


import com.dev.libraryManagementSystem.dto.JwtAuthResponse;
import com.dev.libraryManagementSystem.dto.LoginDto;
import com.dev.libraryManagementSystem.dto.RegisterDto;
import com.dev.libraryManagementSystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User Login",
            description = "Login with username/email and password to get JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful login",
                    content = @Content(schema = @Schema(implementation = JwtAuthResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            )
    })
    @PostMapping(value = {"/login","/signIn"})
    public ResponseEntity<JwtAuthResponse> loginUser(@RequestBody LoginDto loginDto){
        String token = authService.loginUser(loginDto);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }


    @Operation(
            summary = "Register New User",
            description = "Register a new user in the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - username or email already exists"
            )
    })
    @PostMapping(value = {"/register","/signUp"})
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto){
        String response = authService.registerUser(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }





}
