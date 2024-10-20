# Employee Map Dashboard Backend

This is the backend service for the Employee Map Dashboard application, built with **Spring Boot**. It serves as the core system to manage employee information, including their addresses, and provides functionality for employees to communicate with each other via email. It also integrates with a frontend React application for a seamless user experience.

## Features

- **Employee Management**  
  The application allows for storing and managing employee information, such as personal details and addresses.
  
- **Email Communication**  
  Employees can send emails to one another through the system, making internal communication easy and accessible.

- **Location Mapping**  
  Employee addresses are stored in the database and can be visualized on the frontend map.

- **User Authentication and Authorization**  
  Implemented security features, including user login, ensure that only authenticated employees can access the application. Each employee can log in, view their dashboard, and send emails to other employees.

- **Frontend Integration**  
  The backend communicates with a React-based frontend, which displays employee information and their location on a map.

## Technology Stack

### Backend (Spring Boot)

- **Spring Boot**: The core framework used for building the backend RESTful services.
- **MyBatis**: For mapping database records to Java objects and performing SQL queries in a type-safe manner.
- **PostgreSQL**: The primary database for storing employee data.
- **Flyway**: Database versioning and migration tool used to manage and apply schema changes.
- **Docker**: Containerization technology used to ensure a consistent deployment environment.
- **Spring Security**: Provides authentication and authorization mechanisms for user management.

## Infrastructure Setup

The entire infrastructure, including the backend, PostgreSQL database, and the frontend, is containerized and orchestrated using **Docker** and **Docker Compose**. This allows for easy deployment and scaling of the application.

## Testing

- **Unit Tests**: Unit tests are implemented to ensure the core logic of the application functions as expected.
- **Integration Tests**: Integration tests are provided to verify that different parts of the system work together correctly, especially in terms of database interactions and API functionality.

