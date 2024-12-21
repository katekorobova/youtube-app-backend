# youtube-app-backend
## Overview
The youtube-app-backend is a backend service for a web application that leverages the YouTube Data API for advanced video search. Built with Java and Spring Boot, it serves as a mediator between the frontend application and the YouTube Data API, offering features such as secure user authentication and search request history management.

The backend handles user authentication using JSON Web Tokens and integrates a database to store search request history.

## Features
- YouTube Data API Mediation
Processes API requests, manages API keys, and optimizes communication with the YouTube Data API.

- User Authentication
Secure JWT-based authentication for authorized access to protected endpoints.

- Request History Storage
Stores and retrieves authenticated users’ search histories in a PostgreSQL database.

- Scalable RESTful API
Provides endpoints for video search, user authentication, and history management.

## Technologies Used
- Java 17
- Spring Boot 3.4
- Spring Security (JWT-based authentication)
- Spring Data JPA (database interaction)
- PostgreSQL
- YouTube Data API v3
- Maven (dependency management)

## Project Setup
### Prerequisites
- Java 17+
- PostgreSQL

### Steps to Run

#### Obtain YouTube Data API Key
- Create a new project on the [Google Cloud Console](https://console.cloud.google.com/projectcreate).
- [Generate an API key](https://console.cloud.google.com/apis/credentials) for your project.
- Enable the [YouTube Data API v3](https://console.cloud.google.com/apis/api/youtube.googleapis.com/).


#### Generate a JWT secret key:
You can potentially use any base64-encoded string, but the recommended approach is to use a cryptographically secure random generator, for example:
```
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256); // Set key size to 256 bits
        SecretKey secretKey = keyGen.generateKey();
        String jwtSecret = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated JWT Secret: " + jwtSecret);
    }
}
```

#### Database Setup:
Ensure your PostgreSQL database is running.

Make sure an empty schema called public exists in your database.

Create a new schema called authentication.

#### Clone the Repository:
```
git clone https://github.com/katekorobova/youtube-app-backend.git  
cd youtube-app-backend
```

#### Configure Environment Variables:
Create an env.properties file in the root directory with the following variables:
```
GOOGLE_API_KEY=<your_api_key>
JWT_SECRET_KEY=<your_secret_key_base64_encoded>
JWT_SECURE_COOKIES=false # Set to true if you're deploying the app on a remote server

SERVER_PORT=<desired_server_port>

DATABASE_URL=<your_database_url> # Example: localhost:5432/youtube-app
DATABASE_USERNAME=<your_database_username>
DATABASE_PASSWORD=<your_database_password>

ALLOWED_ORIGINS=<your_frontend_urls> # Example: http://localhost:5173,http://localhost:3000
```

#### Install Dependencies:
Use Maven to download and set up all dependencies:
```
mvn clean install
```

#### Run the Application:
Start the Spring Boot server:
```
mvn spring-boot:run
```
The backend will run at http://localhost:<desired_server_port>.

## API Endpoints
### Authentication
POST /register – Register a new user and return an access token and a refresh token.

POST /login – Authenticate a user and return an access token and a refresh token.

POST /refresh - Generate a new access token using a valid refresh token.

POST /logout - Invalidate the refresh token to log the user out.

### Search Functionality
POST /search – Perform a video search using query parameters and filters.

POST /searchAuth – Perform a video search and add the query to the authenticated user's search history.

### User Search History
GET /history – Retrieve the search history of the authenticated user.

## Links:
Frontend Deployment: The frontend is deployed on [Vercel](https://youtube-app-tawny.vercel.app).

Frontend Repository: View the frontend repository [here](https://github.com/katekorobova/youtube-app).
