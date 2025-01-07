-- V3__Insert_Sample_Books.sql

-- Insert Authors
INSERT INTO authors (name) VALUES
                               ('George Orwell'),
                               ('J.K. Rowling'),
                               ('Jane Austen'),
                               ('Stephen King'),
                               ('F. Scott Fitzgerald'),
                               ('Ernest Hemingway'),
                               ('Agatha Christie'),
                               ('J.R.R. Tolkien'),
                               ('Charles Dickens'),
                               ('Virginia Woolf');

-- Insert Books
INSERT INTO book (isbn, title, author_id, genre, available) VALUES
-- George Orwell's books
('978-0451524935', '1984', 1, 'Dystopian Fiction', true),
('978-0452277502', 'Animal Farm', 1, 'Political Satire', true),

-- J.K. Rowling's books
('978-0439708180', 'Harry Potter and the Philosopher''s Stone', 2, 'Fantasy', true),
('978-0439064873', 'Harry Potter and the Chamber of Secrets', 2, 'Fantasy', true),
('978-0439136365', 'Harry Potter and the Prisoner of Azkaban', 2, 'Fantasy', true),

-- Jane Austen's books
('978-0141439518', 'Pride and Prejudice', 3, 'Classic Romance', true),
('978-0141439587', 'Emma', 3, 'Classic Romance', true),
('978-0141439687', 'Sense and Sensibility', 3, 'Classic Romance', true),

-- Stephen King's books
('978-0307743657', 'The Shining', 4, 'Horror', true),
('978-0307743664', 'It', 4, 'Horror', true),
('978-0307743671', 'The Stand', 4, 'Horror', true),

-- F. Scott Fitzgerald's books
('978-0743273565', 'The Great Gatsby', 5, 'Classic Fiction', true),
('978-0743273572', 'Tender Is the Night', 5, 'Classic Fiction', true),

-- Ernest Hemingway's books
('978-0684801223', 'The Old Man and the Sea', 6, 'Literary Fiction', true),
('978-0684801469', 'For Whom the Bell Tolls', 6, 'War Fiction', true),

-- Agatha Christie's books
('978-0062073488', 'Murder on the Orient Express', 7, 'Mystery', true),
('978-0062073495', 'Death on the Nile', 7, 'Mystery', true),

-- J.R.R. Tolkien's books
('978-0547928227', 'The Hobbit', 8, 'Fantasy', true),
('978-0547928203', 'The Fellowship of the Ring', 8, 'Fantasy', true),

-- Charles Dickens' books
('978-0141439563', 'Great Expectations', 9, 'Classic Fiction', true),
('978-0141439556', 'A Tale of Two Cities', 9, 'Historical Fiction', true),

-- Virginia Woolf's books
('978-0156628709', 'Mrs. Dalloway', 10, 'Modernist Fiction', true),
('978-0156949606', 'To the Lighthouse', 10, 'Modernist Fiction', true);