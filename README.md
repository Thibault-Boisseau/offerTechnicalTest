# Technical Test – Spring Boot API

This project is a REST API developed with Spring Boot, built with Maven, using an embedded H2 database and an automated Postman test collection executable via Newman.

# HOW TO BUILD

## 1. Prerequisites

The following tools must be installed on the system:

### Java 17 or higher
```bash
java -v
```

### Maven
```bash
mvn -v
```
If Maven is NOT installed, download it here:  
https://maven.apache.org/download.cgi

### Node.js and npm
```bash
node -v
npm -v
```
If Node.js & npm are NOT installed, download them here:  
https://nodejs.org/en/download

### Newman
Installation:
```bash
npm install -g newman
```

Verification:
```bash
newman -v
```

## 2. Running the Application

From the root directory of the project:

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8081
```

## 3. Running the Automated Tests

### 3.1 Unit & Integration Tests

Run tests:
```bash
mvn test
```

Clean and test:
```bash
mvn clean test
```

Test reports location:
```
target/surefire-reports
```

### 3.2 Running the Postman Collection with Newman

The API must be running before executing the collection.

```bash
newman run src/main/resources/postman_collection.json
```

All API endpoints will be tested automatically, with the execution report displayed directly in the terminal.

# HOW THE API WORKS

## 1. H2 Database Console

Once the application is running, the H2 database console is available at:

```
http://localhost:8081/h2-console
```

### H2 Connection Settings
```
JDBC URL: jdbc:h2:mem:boisseau  
Username: sa  
Password: (empty)
```

### Useful SQL Commands

Get all users:
```sql
SELECT * FROM USERS;
```

Delete all users:
```sql
DELETE FROM USERS;
```

## 2. API Endpoints

The API exposes **two main services**:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `http://localhost:8081/api/users` | Create a new user |
| GET | `http://localhost:8081/api/user/{id}` | Retrieve one user by ID |

## 3. Business Rules & Data Validation

The API enforces the following rules:

### Mandatory Fields (NOT NULL)

The following fields **must not be null**:
- `name`
- `birthdate`
- `countryOfResidence`

If one of these fields is missing, the API will automatically reject the request.

### Phone Number Uniqueness

- The `phoneNumber` field is **optional**
- If provided, it **must be unique in the database**
- This rule exists because the **phone number may be used as an informational identifier**
  (for example, to receive security or notification codes)

### Birthdate Format

The required format for `birthdate` is:

```
yyyy-MM-dd
```

Example:
```
1998-04-21
```

### Gender Field

- Stored as an **enumeration (ENUM)**
- Only predefined values are accepted
- Any invalid value will be rejected by the API
