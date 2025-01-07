-- V1__Initial_Schema.sql

-- Create roles table
CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL
);

-- Create users table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);

-- Create user_roles junction table
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Create authors table
CREATE TABLE authors (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

-- Create book table (note: singular as per entity)
CREATE TABLE book (
                      id SERIAL PRIMARY KEY,
                      isbn VARCHAR(255) NOT NULL UNIQUE,
                      title VARCHAR(255) NOT NULL,
                      author_id BIGINT NOT NULL,
                      genre VARCHAR(255) NOT NULL,
                      available BOOLEAN NOT NULL DEFAULT true,
                      FOREIGN KEY (author_id) REFERENCES authors(id)
);

-- Create borrowers table
CREATE TABLE borrowers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE
);

-- Create borrow_records table
CREATE TABLE borrow_records (
                                id SERIAL PRIMARY KEY,
                                book_id BIGINT NOT NULL,
                                borrower_id BIGINT NOT NULL,
                                borrow_date TIMESTAMP NOT NULL,
                                return_date TIMESTAMP,
                                due_date TIMESTAMP NOT NULL,
                                FOREIGN KEY (book_id) REFERENCES book(id),
                                FOREIGN KEY (borrower_id) REFERENCES borrowers(id)
);

-- Create indexes for better performance
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_book_isbn ON book(isbn);
CREATE INDEX idx_book_author ON book(author_id);
CREATE INDEX idx_book_available ON book(available);
CREATE INDEX idx_borrower_email ON borrowers(email);
CREATE INDEX idx_borrow_record_dates ON borrow_records(borrow_date, due_date, return_date);