# Spring Boot JWT Security & Role-Based API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-9.0-orange)
![Maven](https://img.shields.io/badge/Maven-4.0-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

A robust RESTful API built with **Spring Boot** that demonstrates a secure authentication and authorization system using **JSON Web Tokens (JWT)**. This project features role-based access control, distinguishing between `USER` and `ADMIN` permissions for different API endpoints.

---

## ✨ Features

-   **Secure Authentication**: User registration and login with JWT generation.
-   **Token Refresh**: Endpoint to generate new access tokens using a refresh token.
-   **Role-Based Authorization**: Secure endpoints based on user roles (`ROLE_ADMIN`, `ROLE_USER`).
-   **CRUD Operations**: Full Create, Read, Update, and Delete functionality for products.
-   **Custom Exception Handling**: Graceful handling of authentication and validation errors.
-   **Database Integration**: Uses Spring Data JPA with a MySQL database.

---

## 🛠️ Technologies Used

-   **Java 21**
-   **Spring Boot 3**
-   **Spring Security 6**
-   **Spring Data JPA**
-   **MySQL Database**
-   **Maven**
-   **JJWT (Java JWT)**

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Make sure you have the following installed on your system:

-   JDK 21 or later
-   Maven
-   MySQL Server

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/ahmarjamal9820/jwt-security.git](https://github.com/ahmarjamal9820/jwt-security.git)
    cd jwt-security
    ```

2.  **Create a MySQL Database:**
    Create a new database in your MySQL instance.
    ```sql
    CREATE DATABASE your_database_name;
    ```

3.  **Configure Application Properties:**
    Open the `src/main/resources/application.properties` file and update the database connection details and add your JWT secret key.

    **Important:** Your JWT secret key must be at least 256 bits (32 characters) long and Base64 encoded for security. You can generate one online or use a command-line tool.

    ```properties
    # Server Port
    server.port=8080

    # MySQL Database Configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # JPA/Hibernate Configuration
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

    # JWT Secret Key (MUST be 256-bit Base64 encoded)
    # Example key (DO NOT USE IN PRODUCTION):
    # aWxvdmVteWNvdW50cnlhbmRpbG92ZW15cGVvcGxlYW5kaXdpbGxkaWVmb3JpdA==
    jwt.secret=your_super_secret_256_bit_base64_encoded_key
    ```

4.  **Build the Project:**
    Use Maven to build the project and install dependencies.
    ```bash
    mvn clean install
    ```

5.  **Run the Application:**
    You can run the application using the Spring Boot Maven plugin.
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080`.

---

## API Endpoints Documentation

Here are the available API endpoints. You can use a tool like [Postman](https://www.postman.com/) to interact with them.

### `/api/auth` (Public Endpoints)

These endpoints are accessible to everyone without authentication.

#### 1. Register a New User

-   **URL**: `/api/auth/register`
-   **Method**: `POST`
-   **Description**: Creates a new user account. The `role` can be `ROLE_USER` or `ROLE_ADMIN`.
-   **Request Body**:
    ```json
    {
      "fullName": "Ahmar Jamal",
      "username": "ahmar",
      "password": "123456",
      "role": "ROLE_ADMIN"
    }
    ```

#### 2. Login User

-   **URL**: `/api/auth/login`
-   **Method**: `POST`
-   **Description**: Authenticates a user and returns a JWT access token and a refresh token.
-   **Request Body**:
    ```json
    {
      "username": "ahmar",
      "password": "123456"
    }
    ```
-   **Success Response**:
    ```json
    {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

#### 3. Refresh Access Token

-   **URL**: `/api/auth/refresh-token`
-   **Method**: `POST`
-   **Description**: Generates a new JWT access token using a valid refresh token.
-   **Request Body**:
    ```json
    {
      "refreshToken": "your_refresh_token_here"
    }
    ```
-   **Success Response**:
    ```json
    {
        "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
        "refreshToken": "your_old_refresh_token"
    }
    ```

---

### `/api/products` (Protected Endpoints)

These endpoints require a valid JWT `accessToken`.

> **How to Authenticate in Postman:**
> In the **Authorization** tab of your request, select **Bearer Token** as the type and paste your `accessToken` into the **Token** field.

#### 1. Get All Products

-   **URL**: `/api/products`
-   **Method**: `GET`
-   **Authorization**: `ROLE_USER` or `ROLE_ADMIN`

#### 2. Get Product by ID

-   **URL**: `/api/products/{id}`
-   **Method**: `GET`
-   **Authorization**: `ROLE_USER` or `ROLE_ADMIN`

#### 3. Create a New Product

-   **URL**: `/api/products`
-   **Method**: `POST`
-   **Authorization**: `ROLE_ADMIN` only
-   **Request Body**:
    ```json
    {
      "name": "iPhone 16 Pro Max 256 GB",
      "price": "129990"
    }
    ```

#### 4. Update an Existing Product

-   **URL**: `/api/products/{id}`
-   **Method**: `PUT`
-   **Authorization**: `ROLE_ADMIN` only
-   **Request Body**:
    ```json
    {
      "name": "Updated Product Name",
      "price": "135000"
    }
    ```

#### 5. Delete a Product

-   **URL**: `/api/products/{id}`
-   **Method**: `DELETE`
-   **Authorization**: `ROLE_ADMIN` only

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.
