# Caspian Tech Demo MS

Caspian Tech Demo MS is a microservices-based application that provides user management and notification services.

## Services

- **User Service**: Manages user registration and login.
- **Notification Service**: Handles user notifications.
- **API Gateway**: Routes user requests to the appropriate services.

## Setup

### Running with Docker

1. Clone the project repository:

   ```bash
   git clone <https://github.com/Shafag42/caspian-tech-demo-ms.git>
   cd caspian-tech-demo-ms

2. Start the services using Docker Compose:

   ```bash

   docker-compose up --build

## Tech stack

1. Spring Boot
2. Java 17
3. RabbitMQ
4. PostgreSQL

## Configuration

Environment variables are defined in the **docker-compose.yml** file.   
