package com.dev.libraryManagementSystem.service;

import com.dev.libraryManagementSystem.dto.LoginDto;
import com.dev.libraryManagementSystem.dto.RegisterDto;
import com.dev.libraryManagementSystem.entity.Role;
import com.dev.libraryManagementSystem.entity.User;
import com.dev.libraryManagementSystem.exception.libraryApiException;
import com.dev.libraryManagementSystem.repository.RoleRepository;
import com.dev.libraryManagementSystem.repository.UserRepository;
import com.dev.libraryManagementSystem.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthService(AuthenticationManager authenticationManager,
                           ModelMapper modelMapper,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String registerUser(RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }
        User user = mapToUserEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();

        roles.add(userRole);
        user.setRole(roles);


        userRepository.save(user);

        return "User registered successfully";
    }

    public String loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    private User mapToUserEntity(RegisterDto registerDto){
        return modelMapper.map(registerDto, User.class);
    }
}
