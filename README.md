# Visitors Logbook Management System (backend)

The Visitors Logbook Management System is an automated monitoring system designed to track visitors to an organization's premises, providing accurate and reliable reports to management. This system includes modules for guest registration, host management, and administrative functions.

## Project Objective

The objective of this project is to create an efficient and user-friendly system for monitoring visitors to an organization's premises. Key features include fast check-in and check-out processes, QR code-based registration, smart check-ins for recurring visitors, real-time visitor tracking, employee data management, and a comprehensive admin dashboard.

## Technologies Used

- **Spring Boot**: Backend development framework for building robust Java applications.
- **JWT (JSON Web Tokens)**: Used for secure authentication and authorization of users.
- **PostgreSQL**: Relational database management system for storing visitor and employee data.
- **Swagger**: API documentation tool for documenting the RESTful APIs.
- **Docker**: Containerization platform for packaging and deploying the application.
- **AWS (Amazon Web Services)**: Cloud computing platform for hosting and deploying the application.

## Features

- **Guest/Visitor Module**: Allows guests to log in and out quickly, with options for QR code-based registration and smart check-ins for recurring visits.
- **Host Module**: Provides host management functionality, enabling hosts to receive timely notifications when their guests arrive and access visitor details.
- **Admin Module**: Offers comprehensive administrative features, including real-time visitor tracking, employee data management, statistical reports, and custom report generation.

## Getting Started

### Prerequisites

- Docker

### Installation

1. Clone the repository: `git clone https://github.com/theophilusgordon/VLMS-backend.git`
2. Navigate to the project directory: `cd VLMS-backend`
3. Run application through docker: `docker compose up -d`

### Usage

Refer to the Swagger documentation for API endpoints and usage instructions.

## Documentation

- [Swagger API Documentation](http://localhost:8080/swagger-ui/index.html): View detailed documentation for the RESTful APIs.

## License

This project is licensed under the [MIT License](link-to-license).
