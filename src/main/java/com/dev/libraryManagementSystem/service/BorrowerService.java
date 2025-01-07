package com.dev.libraryManagementSystem.service;


import com.dev.libraryManagementSystem.dto.BorrowerUpdateDTO;
import com.dev.libraryManagementSystem.entity.Role;
import com.dev.libraryManagementSystem.entity.User;
import com.dev.libraryManagementSystem.exception.libraryApiException;
import com.dev.libraryManagementSystem.repository.BorrowRecordRepository;
import com.dev.libraryManagementSystem.repository.RoleRepository;
import com.dev.libraryManagementSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BorrowerService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public BorrowerService(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     BorrowRecordRepository borrowRecordRepository,
                                     PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createBorrower(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add BORROWER role
        Role borrowerRole = roleRepository.findByName("ROLE_BORROWER")
                .orElseThrow(() -> new EntityNotFoundException("Borrower role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(borrowerRole);
        user.setRole(roles);

        return userRepository.save(user);
    }

    public User updateBorrower(Long id, BorrowerUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user has borrower role
        boolean isBorrower = user.getRole().stream()
                .anyMatch(role -> role.getName().equals("ROLE_BORROWER"));

        if (!isBorrower) {
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "User is not a borrower");
        }

        // Update email if provided and different
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateDTO.getEmail())) {
                throw new libraryApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
            }
            user.setEmail(updateDTO.getEmail());
        }

        // Update username if provided and different
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateDTO.getUsername())) {
                throw new libraryApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
            }
            user.setUsername(updateDTO.getUsername());
        }

        // Update password if provided
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteBorrower(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user has borrower role
        boolean isBorrower = user.getRole().stream()
                .anyMatch(role -> role.getName().equals("ROLE_BORROWER"));

        if (!isBorrower) {
            throw new libraryApiException(HttpStatus.BAD_REQUEST, "User is not a borrower");
        }

        // Check for active loans
        int activeLoans = borrowRecordRepository.countByBorrowerIdAndReturnDateIsNull(id);
        if (activeLoans > 0) {
            throw new libraryApiException(HttpStatus.BAD_REQUEST,
                    "Cannot delete borrower with outstanding borrowed books");
        }

        userRepository.delete(user);
    }

    public List<User> getAllBorrowers() {
        // Find users with BORROWER role
        Role borrowerRole = roleRepository.findByName("ROLE_BORROWER")
                .orElseThrow(() -> new EntityNotFoundException("Borrower role not found"));

        return userRepository.findByRole(borrowerRole);
    }

    public int getActiveBorrowCount(Long userId) {
        return borrowRecordRepository.countByBorrowerIdAndReturnDateIsNull(userId);
    }
}