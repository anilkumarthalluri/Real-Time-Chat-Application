# Real-Time Chat Application

This is a full-stack, real-time chat application built with Java, Spring Boot, and WebSockets. It provides a secure, multi-user chat environment with JWT-based authentication and a complete, secure password reset functionality.

## Features

- **Public Chat Room:** A single, public chat room where all connected users can communicate in real-time.
- **User Authentication:** Secure user registration and login system using JWT (JSON Web Tokens).
- **WebSocket Communication:** Real-time messaging using STOMP over SockJS for broad browser compatibility.
- **Secure Password Storage:** User passwords are securely hashed using BCrypt before being stored in the database.
- **Full Password Reset Flow:**
    - Users can request a password reset via a "Forgot Password" form.
    - A unique, secure token is generated and sent to the user's email.
    - The email contains a link to a dedicated page where the user can set a new password.
- **Typing Indicators:** Users can see when other participants are typing a message.
- **Professional Email Delivery:** Uses the SendGrid API to send professional, HTML-formatted emails for password resets, improving deliverability and avoiding spam folders.

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3**
- **Spring Security:** For authentication and authorization.
- **Spring WebSockets:** For real-time messaging.
- **Spring Data JPA (Hibernate):** For database interaction.
- **PostgreSQL:** As the relational database.
- **SendGrid API:** For transactional email sending.
- **JJWT (Java JWT):** For creating and parsing JSON Web Tokens.

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript (ES6+):** For all client-side logic.
- **SockJS & STOMP.js:** To handle the WebSocket connection from the browser.

### Build Tool
- **Maven**

## Local Development Setup

To run this project on your local machine, follow these steps.

### Prerequisites

- **JDK 17** or later.
- **Apache Maven**.
- **PostgreSQL** server running locally.

### Installation

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/anilkumarthalluri/Real-Time-Chat-Application.git
    cd Real-Time-Chat-Application
    ```

2.  **Create the Database:**
    - In PostgreSQL, create a new database named `chat`.

3.  **Configure Local Properties:**
    - In the `src/main/resources/` directory, create a new file named `application-dev.properties`.
    - **This file is ignored by Git** and will contain your local secrets.
    - Copy and paste the following content into the file, replacing the placeholder values with your own local credentials:

    ```properties
    # Server Port
    server.port=8081

    # Local PostgreSQL Database Connection
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=jdbc:postgresql://localhost:5432/chat
    spring.datasource.username=your_local_db_user # e.g., postgres
    spring.datasource.password=your_local_db_password

    # Use 'update' for local development to automatically update the schema
    spring.jpa.hibernate.ddl-auto=update

    # JWT Secret Key for Local Development (use a strong, random string)
    token.signing.key=YOUR_LOCAL_JWT_SECRET_KEY

    # SendGrid API Key for Local Development
    SENDGRID_API_KEY=YOUR_SENDGRID_API_KEY
    SENDGRID_FROM_EMAIL=YOUR_VERIFIED_SENDER_EMAIL_IN_SENDGRID

    # Frontend URL for local development
    FRONTEND_URL=http://localhost:8081
    ```

4.  **Build the Project:**
    - Open a terminal in the project root and run the Maven build command:
    ```sh
    mvn clean install
    ```

5.  **Run the Application:**
    - You can run the application from your IDE (like IntelliJ IDEA) by running the `RealTimeChatApplication` main class.
    - Alternatively, you can run it from the terminal:
    ```sh
    mvn spring-boot:run
    ```

6.  **Access the Application:**
    - Open your web browser and navigate to **http://localhost:8081**.

## Configuration Management

The project uses Spring Profiles to manage different environments:

-   **`application.properties`**: This file is committed to Git and sets the default active profile to `dev`. This ensures a smooth experience for local development.
-   **`application-dev.properties`**: This file is for local development secrets. It is **NOT** committed to Git and is listed in `.gitignore`.
-   **`application-prod.properties`**: This file contains the configuration for the production environment. It is safe to commit because all secrets are replaced with placeholders (e.g., `${DB_PASSWORD}`) that are read from environment variables on the production server.

## API Endpoints

The primary authentication endpoints are:

-   `POST /api/v1/auth/signup`: User registration.
-   `POST /api/v1/auth/signin`: User login.
-   `POST /api/v1/auth/forgot-password`: Initiates the password reset process.
-   `POST /api/v1/auth/reset-password`: Completes the password reset process with a valid token.

## Deployment

The application is configured for deployment on **Render**. Production secrets (database credentials, JWT secret, SendGrid API key) are managed securely as **Environment Variables** in the Render dashboard.
