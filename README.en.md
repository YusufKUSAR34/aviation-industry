# Aviation Management System

## About the Project
Aviation Industry System is a comprehensive API system developed to manage airline and transportation routes. This system enables managing transportation options between locations, finding optimal routes, and planning travel with various transfer options.

## Features
- 🔐 JWT-based secure authentication
- 👥 Role-based authorization (USER and ADMIN roles)
- 📍 Location management
- 🚆 Transportation options management
- 🛫 Route optimization and search
- 🔄 Transfer flight options
- 📊 API documentation with Swagger UI
- 🚀 Redis cache support
- 🗄️ Soft delete mechanism
- 📝 Detailed logging system (AOP Log, Slf4j)

## Technologies

### Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Cache
- JWT (JSON Web Token)
- MapStruct
- Lombok
- Docker

### Database & Cache
- PostgreSQL
- Redis

### API Documentation
- OpenAPI 3.0
- Swagger UI

### Security
- Spring Security
- JWT Authentication
- Role-Based Access Control (RBAC)

### Build & Dependency Management
- Gradle

## Installation

### Requirements
- Java 17 or higher
- PostgreSQL 12 or higher
- Redis 6.x or higher
- Gradle 7.x or higher

### Application Setup
### 1. Clone the Project
```bash
git clone https://github.com/YusufKUSAR34/aviation-industry.git
cd aviation.industry
```

### 2. Run with Docker Compose
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# View logs of a specific service
docker-compose logs -f app
docker-compose logs -f postgres
docker-compose logs -f redis
```

### 3. Check
```bash
# Check if all containers are running
docker-compose ps

# Check PostgreSQL connection
docker exec -it aviation-postgres psql -U postgres -d aviation

# Check Redis connection
docker exec -it aviation-redis redis-cli ping
```

### 4. Application Check

1. Access Swagger UI:
```
http://localhost:8080/swagger-ui.html
```
2. Access Redis UI:
```
http://localhost:8081/
```

3. Health check:
```bash
curl http://localhost:8080/actuator/health
```

## API Usage (Documentation)

### Authentication Endpoints

#### Register
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
}
```

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
    "username": "testuser",
    "password": "password123"
}

Response:
{
    "token": "jwt_token_value"
}
```

### Location Endpoints

#### Create Location
```http
POST /api/v1/locations
Authorization: Bearer jwt_token_value
Content-Type: application/json

{
    "name": "Istanbul Airport",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "Istanbul",
    "country": "Turkey"
}
```

#### Get All Locations
```http
GET /api/v1/locations
Authorization: Bearer jwt_token_value

Response:
[
    {
        "id": 1,
        "name": "Istanbul Airport",
        "type": "AIRPORT",
        "latitude": 41.275278,
        "longitude": 28.751944,
        "city": "Istanbul",
        "country": "Turkey"
    }
]
```

#### Get Location by ID
```http
GET /api/v1/locations/{id}
Authorization: Bearer jwt_token_value

Response:
{
    "id": 1,
    "name": "Istanbul Airport",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "Istanbul",
    "country": "Turkey"
}
```

#### Update Location
```http
PUT /api/v1/locations/{id}
Authorization: Bearer jwt_token_value
Content-Type: application/json

{
    "name": "Updated Istanbul Airport",
    "type": "AIRPORT",
    "latitude": 41.275278,
    "longitude": 28.751944,
    "city": "Istanbul",
    "country": "Turkey"
}
```

#### Delete Location
```http
DELETE /api/v1/locations/{id}
Authorization: Bearer jwt_token_value
```

### Transportation Endpoints

#### Create Transportation
```http
POST /api/v1/transportations
Authorization: Bearer jwt_token_value
Content-Type: application/json

{
    "type": "FLIGHT",
    "originLocationId": 1,
    "destinationLocationId": 2,
    "price": 500.00,
    "duration": 120
}
```

#### Get All Transportations
```http
GET /api/v1/transportations
Authorization: Bearer jwt_token_value

Response:
[
    {
        "id": 1,
        "type": "FLIGHT",
        "originLocation": {
            "id": 1,
            "name": "Istanbul Airport",
            "type": "AIRPORT"
        },
        "destinationLocation": {
            "id": 2,
            "name": "Ankara Airport",
            "type": "AIRPORT"
        },
        "price": 500.00,
        "duration": 120
    }
]
```

#### Get Transportation by ID
```http
GET /api/v1/transportations/{id}
Authorization: Bearer jwt_token_value

Response:
{
    "id": 1,
    "type": "FLIGHT",
    "originLocation": {
        "id": 1,
        "name": "Istanbul Airport",
        "type": "AIRPORT"
    },
    "destinationLocation": {
        "id": 2,
        "name": "Ankara Airport",
        "type": "AIRPORT"
    },
    "price": 500.00,
    "duration": 120
}
```

#### Update Transportation
```http
PUT /api/v1/transportations/{id}
Authorization: Bearer jwt_token_value
Content-Type: application/json

{
    "type": "FLIGHT",
    "originLocationId": 1,
    "destinationLocationId": 2,
    "price": 550.00,
    "duration": 120
}
```

#### Delete Transportation
```http
DELETE /api/v1/transportations/{id}
Authorization: Bearer jwt_token_value
```

### Route Endpoints

#### Search Routes
```http
POST /api/v1/routes/search
Authorization: Bearer jwt_token_value
Content-Type: application/json

{
    "originLocationId": 1,
    "destinationLocationId": 2
}

Response:
[
    {
        "transportations": [
            {
                "id": 1,
                "type": "FLIGHT",
                "originLocation": {
                    "id": 1,
                    "name": "Istanbul City Center",
                    "type": "CITY_POINT"
                },
                "destinationLocation": {
                    "id": 3,
                    "name": "Istanbul Airport",
                    "type": "AIRPORT"
                },
                "price": 10.00,
                "duration": 45
            },
            {
                "id": 2,
                "type": "FLIGHT",
                "originLocation": {
                    "id": 3,
                    "name": "Istanbul Airport",
                    "type": "AIRPORT"
                },
                "destinationLocation": {
                    "id": 4,
                    "name": "Ankara Airport",
                    "type": "AIRPORT"
                },
                "price": 500.00,
                "duration": 120
            },
            {
                "id": 3,
                "type": "OTHER",
                "originLocation": {
                    "id": 4,
                    "name": "Ankara Airport",
                    "type": "AIRPORT"
                },
                "destinationLocation": {
                    "id": 2,
                    "name": "Ankara City Center",
                    "type": "CITY_POINT"
                },
                "price": 8.00,
                "duration": 30
            }
        ],
        "totalPrice": 518.00,
        "totalDuration": 195
    }
]
```

## Security Features

### JWT Configuration
- Token Duration: 24 hours
- Token Content: Username and roles
- Security Header: Bearer token

### Role-Based Access
- ROLE_USER: Basic operations
- ROLE_ADMIN: Administrative operations

## Cache Strategy
- Implemented using Redis
- Key Prefix: "aviation_"
- Null values are not cached

## Error Handling
- Centralized error handling
- Custom exception classes
- Detailed error messages

## Database Schema
```
users
- id (PK)
- username (unique)
- email (unique)
- password
- role
- created_at
- updated_at
- version
- deleted

locations
- id (PK)
- name
- type (AIRPORT, CITY_POINT)
- latitude
- longitude
- city
- country
- created_at
- updated_at
- version
- deleted

transportations
- id (PK)
- origin_id (FK)
- destination_id (FK)
- transportationType
- duration
- price
- created_at
- updated_at
- version
- deleted
```

## Design Patterns

### Route Validation System

The route validation system uses multiple design patterns to ensure clean, maintainable, and extensible code:

#### 1. Composite Pattern
- **Purpose:** Allows handling individual validators and validator groups uniformly
- **Implementation:** `CompositeRouteValidator` acts as a container for multiple `RouteValidator` implementations
- **Benefits:**
    - Validators can be grouped and nested
    - New validators can be added without modifying existing code
    - Validation order can be controlled

#### 2. Chain of Responsibility Pattern
- **Purpose:** Passes validation request along a chain of validators
- **Implementation:** Each `RouteValidator` performs its specific validation and passes context to the next validator
- **Benefits:**
    - Makes validators independent of each other
    - Validation order can be changed without modifying validators
    - Validation steps can be easily added or removed

#### 3. Builder Pattern
- **Purpose:** Provides a flexible way to create validation context
- **Implementation:** `ValidationContext` uses Lombok's `@Builder` annotation
- **Benefits:**
    - Clear and readable context creation
    - Optional parameters can be easily managed
    - Immutable validation context objects

### Validator Implementations

The system includes various validators responsible for specific aspects of route validation:

1. **TransportationCountValidator**
    - Ensures the maximum allowed number of transportations is not exceeded
    - Order: 1 (runs first)

2. **ConnectionValidator**
    - Validates connections between consecutive transportations
    - Order: 2

3. **FlightRequirementValidator**
    - Ensures exactly one flight in the route
    - Order: 3

4. **FlightLocationValidator**
    - Validates flight origin and destination points
    - Order: 4

5. **TransferTypeValidator**
    - Validates transfer types and their positions relative to flight
    - Order: 5

### Validation Process

1. Route combinations are generated using Stream API
2. Each combination is validated through the composite validator
3. Validation results are collected and processed
4. Valid routes are returned or appropriate exceptions are thrown 
