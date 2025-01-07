package com.dev.libraryManagementSystem.repository;

import com.dev.libraryManagementSystem.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByEmail(String email);
}