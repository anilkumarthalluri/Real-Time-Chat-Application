# Real-Time Chat Application

This is a web-based real-time chat application built using Java, Spring Boot, and WebSockets. It provides a platform for users to sign up, log in, and exchange messages in real-time.

## Features

*   **User Authentication**: Secure user registration and login functionality using JWT (JSON Web Tokens).
*   **Real-Time Messaging**: Instant message delivery between users using WebSockets and the STOMP protocol.
*   **Typing Indicators**: Shows when a user is typing or stops typing.
*   **Join/Leave Notifications**: Broadcasts a message when a user joins or leaves the chat.

## Technologies Used

*   **Backend**:
    *   [Java](https://www.java.com/)
    *   [Spring Boot](https://spring.io/projects/spring-boot): For the core application framework.
    *   [Spring Security](https://spring.io/projects/spring-security): For authentication and authorization.
    *   [Spring WebSocket](https://docs.spring.io/spring-framework/reference/web/websocket.html): For real-time communication.
    *   [Spring Data JPA](https://spring.io/projects/spring-data-jpa): For database interaction.
    *   [PostgreSQL](https://www.postgresql.org/): As the relational database.
    *   [JWT (Java Web Token)](https://github.com/jwtk/jjwt): For creating and validating access tokens.
    *   [Maven](https://maven.apache.org/): For dependency management and build automation.
    *   [Lombok](https://projectlombok.org/): To reduce boilerplate code.

*   **Frontend**:
    *   HTML, CSS, JavaScript
    *   [SockJS](https://github.com/sockjs/sockjs-client): As a browser JavaScript library to enable WebSocket emulation.
    *   [STOMP.js](https://stomp-js.github.io/): A library for STOMP messaging over WebSockets in the browser.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   JDK (Java Development Kit) 17 or higher
*   Apache Maven
*   PostgreSQL database server

### Installation

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/Real-Time-Chat-Application.git
    cd Real-Time-Chat-Application
    ```

2.  **Create the database:**
    *   Open your PostgreSQL client (e.g., `psql` or pgAdmin).
    *   Create a new database for the application.
        ```sql
        CREATE DATABASE chat;
        ```

3.  **Configure the application:**
    *   Navigate to the `src/main/resources` directory.
    *   Create a new file named `application.properties` by copying the example file.
        ```sh
        cp application.properties.example application.properties
        ```
    *   Open `application.properties` and update the following properties with your database credentials and a unique, strong secret key for JWT signing:
        ```properties
        spring.datasource.username=your_db_username
        spring.datasource.password=your_db_password
        token.signing.key=your_super_secret_and_long_signing_key
        ```

4.  **Run the application:**
    *   Use the Maven wrapper to build and run the project.
        ```sh
        ./mvnw spring-boot:run
        ```
    *   The application will start on `http://localhost:8081`.

## How It Works

*   **Authentication**: When a user signs up or signs in, the server generates a JWT and sends it back. This token must be included in the `Authorization` header for subsequent requests, including the WebSocket connection.
*   **WebSocket Connection**: The client connects to the `/ws` endpoint. The `WebSocketAuthConfig` intercepts this connection, validates the JWT from the headers, and establishes the user's security context.
*   **Messaging**: Messages sent to `/app/chat` are handled by the `ChatController`, which then broadcasts them to all subscribed clients on the `/topic/public` destination.
