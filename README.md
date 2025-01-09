# Library Management System API

A RESTful API for managing a library system, including books, authors, and borrowing functionality.

## Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.8 or higher

### Database Setup
1. Create a PostgreSQL database:
```sql
CREATE DATABASE library_management;
```

2. Configure database connection in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library_management
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=true
```

### Application Setup
1. Clone the repository:
```bash
git clone [repository-url]
```

2. Navigate to project directory:
```bash
cd library-management-system
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "john.doe",
    "email": "john.doe@example.com",
    "password": "yourpassword"
}

Response:
{
    "message": "User registered successfully"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "usernameOrEmail": "john.doe",
    "password": "yourpassword"
}

Response:
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer"
}
```

### Books

#### Create Book (Admin)
```http
POST /api/books
Authorization: Bearer {token}
Content-Type: application/json

{
    "isbn": "9780451524935",
    "title": "1984",
    "authorId": 1,
    "genre": "Fiction"
}

Response:
{
    "id": 1,
    "isbn": "9780451524935",
    "title": "1984",
    "author": {
        "id": 1,
        "name": "George Orwell"
    },
    "genre": "Fiction",
    "available": true
}
```

#### Search Books
```http
GET /api/books?authorId=1&genre=Fiction&available=true
Authorization: Bearer {token}

Response:
{
    "content": [
        {
            "id": 1,
            "isbn": "9780451524935",
            "title": "1984",
            "author": {
                "id": 1,
                "name": "George Orwell"
            },
            "genre": "Fiction",
            "available": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {}
    },
    "totalElements": 1
}
```

### Authors

#### Create Author (Admin)
```http
POST /api/authors
Authorization: Bearer {token}
Content-Type: application/json

{
    "name": "George Orwell"
}

Response:
{
    "id": 1,
    "name": "George Orwell"
}
```

#### Get All Authors
```http
GET /api/authors
Authorization: Bearer {token}

Response:
[
    {
        "id": 1,
        "name": "George Orwell",
        "bookCount": 2
    }
]
```

### Borrowing

#### Borrow Book
```http
POST /api/borrow?bookId=1&borrowerId=1
Authorization: Bearer {token}

Response:
{
    "id": 1,
    "book": {
        "id": 1,
        "title": "1984"
    },
    "borrower": {
        "id": 1,
        "name": "John Doe"
    },
    "borrowDate": "2025-01-07T10:00:00",
    "dueDate": "2025-01-21T10:00:00"
}
```

#### Return Book
```http
POST /api/return?bookId=1&borrowerId=1
Authorization: Bearer {token}

Response:
{
    "id": 1,
    "book": {
        "id": 1,
        "title": "1984"
    },
    "borrower": {
        "id": 1,
        "name": "John Doe"
    },
    "borrowDate": "2025-01-07T10:00:00",
    "returnDate": "2025-01-14T15:30:00",
    "dueDate": "2025-01-21T10:00:00"
}
```

#### Get Active Borrows (Admin)
```http
GET /api/borrowed-books
Authorization: Bearer {token}

Response:
[
    {
        "id": 1,
        "book": {
            "id": 1,
            "title": "1984"
        },
        "borrower": {
            "id": 1,
            "name": "John Doe"
        },
        "borrowDate": "2025-01-07T10:00:00",
        "dueDate": "2025-01-21T10:00:00"
    }
]
```

## Additional Features

- Swagger UI Documentation: Access at `http://localhost:8080/swagger-ui/index.html`
- JWT-based Authentication
- Role-based Access Control (ADMIN and BORROWER roles)
- Pagination and Sorting for book searches
- Data validation and error handling
- Database migrations using Flyway

## Business Rules

1. Borrowing Limits:
   - Maximum 5 books per borrower
   - Cannot borrow unavailable books
   - 14-day loan period

2. Administrative Rules:
   - Only admins can add/update/delete books and authors
   - Cannot delete authors with existing books
   - Cannot delete borrowers with outstanding loans
   - Cannot delete books that are currently borrowed

## Error Handling

The API returns appropriate HTTP status codes and error messages:
- 400: Bad Request (validation errors)
- 401: Unauthorized (invalid credentials)
- 403: Forbidden (insufficient permissions)
- 404: Not Found
- 409: Conflict (e.g., duplicate ISBN)
- 500: Internal Server Error
