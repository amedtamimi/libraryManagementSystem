-- V2__Insert_Initial_Data.sql

-- Insert roles
INSERT INTO roles (id, name) VALUES
                                 (1, 'ROLE_BORROWER'),
                                 (2, 'ROLE_ADMIN');

-- Insert admin user (password: admin123 - bcrypt encoded)
INSERT INTO users (username, email, password) VALUES
    ('admin', 'admin@library.com', '$2a$10$6hg/QTw8Th1EmYtg9/5HhOmRdZJ1V.pR2yrpEMQMaiqDlP7U0AmyG');

-- Assign admin role
INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 2);

