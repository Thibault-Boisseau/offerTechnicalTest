# Technical Test – Spring Boot API

This project is a REST API developed with Spring Boot, built with Maven, using an embedded H2 database and an automated Postman test collection executable via Newman.

---

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

### Node.js and npm (required for Newman)

```bash
node -v
npm -v
```

### Newman

Installation:

```bash
npm install -g newman
```

Verification:

```bash
newman -v
```

---

## 2. Running the Application

From the root directory of the project:

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8081
```

---

## 3. Running the Automated Tests

### 3.1. Unit and Integration Tests

Run the test suite:

```bash
mvn test
```

To clean the project before running the tests:

```bash
mvn clean test
```

Test reports are generated in:

```
target/surefire-reports
```

---

### 3.2. Running the Postman Collection with Newman

The API must be running before executing the Postman collection.

From the project root directory:

```bash
newman run src/main/resources/postman_collection.json
```

All API endpoints are tested automatically, and the execution report is displayed in the terminal.

---

## 4. H2 Database Console

Once the application is running, the H2 database console is accessible at:

```
http://localhost:8081/h2-console
```

### H2 Connection Settings

```
JDBC URL: jdbc:h2:mem:boisseau
Username: sa
Password: (empty)
```
