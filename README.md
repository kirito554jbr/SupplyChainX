# SupplyChainX 🚀

A comprehensive Supply Chain Management System built with Spring Boot, designed to manage procurement, production, and delivery operations with role-based access control.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [Documentation](#documentation)

## 🎯 Overview

SupplyChainX is an enterprise-grade supply chain management system that streamlines operations across three key domains:

1. **Procurement (Approvisionnement)** - Raw material and supplier management
2. **Production** - Product manufacturing and BOM management
3. **Delivery (Livraison)** - Customer orders and logistics

The system features comprehensive role-based access control (RBAC) with 10 distinct roles, ensuring secure and appropriate access to resources.

## ✨ Features

### Core Functionality

- **User Management**
  - OAuth2/OIDC authentication via Keycloak
  - Role-based authorization (10 roles)
  - JWT token validation
  - Centralized user management

- **Procurement Management**
  - Raw material inventory tracking
  - Supplier management
  - Supply order creation and tracking
  - Stock level monitoring

- **Production Management**
  - Product catalog management
  - Bill of Materials (BOM) configuration
  - Production order tracking
  - Status management

- **Delivery Management**
  - Customer relationship management
  - Order processing
  - Delivery scheduling and tracking
  - Multi-status workflow

### Technical Features

- RESTful API architecture
- OpenAPI/Swagger documentation
- Docker containerization
- Database migrations with JPA/Hibernate
- MapStruct for DTO mapping
- AOP for cross-cutting concerns
- Comprehensive error handling
- CORS configuration
- Health checks and monitoring

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Build Tool**: Maven 3.8+
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security (Basic Auth + JWT)
- **API Documentation**: SpringDoc OpenAPI 2.3.0
- **Mapping**: MapStruct 1.6.3
- **Database**: MySQL/MariaDB 10.11

### DevOps & Tools
- **Containerization**: Docker & Docker Compose
- **Database Admin**: phpMyAdmin
- **Code Quality**: SonarQube
- **Testing**: JUnit 5, Mockito, H2 (test database)
- **Development**: Spring Boot DevTools, Lombok

## 🏗️ Architecture

### Project Structure

```
SupplyChainX/
├── src/
│   ├── main/
│   │   ├── java/org/example/supplychainx/
│   │   │   ├── SupplyChainXApplication.java
│   │   │   ├── annotation/          # Custom annotations (@RequiresRole)
│   │   │   ├── aspect/              # AOP aspects (AuthorizationAspect)
│   │   │   ├── Config/              # Configuration classes
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── context/             # Thread-local context (UserContext)
│   │   │   ├── Controller/          # REST controllers
│   │   │   │   ├── UserController.java
│   │   │   │   ├── Approvisionnement/
│   │   │   │   ├── Production/
│   │   │   │   └── Livraison/
│   │   │   ├── DTO/                 # Data Transfer Objects
│   │   │   ├── exception/           # Custom exceptions & handlers
│   │   │   ├── interceptor/         # HTTP interceptors
│   │   │   ├── Mappers/             # MapStruct mappers
│   │   │   ├── Model/               # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── Approvisionnement/
│   │   │   │   ├── Production/
│   │   │   │   └── Livraison/
│   │   │   ├── Repository/          # Spring Data repositories
│   │   │   └── Service/             # Business logic
│   │   └── resources/
│   │       ├── application.properties
│   │       └── logback-spring.xml
│   └── test/                        # Unit & integration tests
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

### Domain Model

#### Approvisionnement (Procurement)
- `RawMaterial` - Raw materials inventory
- `Supplier` - Supplier information
- `SupplyOrder` - Purchase orders
- `SupplyOrderMaterials` - Order line items
- `StatusSupply` - Order status enum

#### Production
- `Product` - Finished goods catalog
- `BOM` - Bill of Materials
- `ProductionOrder` - Manufacturing orders
- `StatusProduction` - Production status enum

#### Livraison (Delivery)
- `Customer` - Customer records
- `Order` - Sales orders
- `Delivery` - Shipment tracking
- `StatusOrder` - Order status enum
- `StatusDelivery` - Delivery status enum

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **Docker** and **Docker Compose**
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/SupplyChainX.git
   cd SupplyChainX
   ```

2. **Start the database and supporting services**
   ```powershell
   docker-compose up -d db phpmyadmin
   ```

3. **Build the application**
   ```powershell
   ./mvnw.cmd clean install
   ```

4. **Run the application**
   
   **Option A: Using Maven**
   ```powershell
   ./mvnw.cmd spring-boot:run
   ```
   
   **Option B: Using Docker**
   ```powershell
   docker-compose up -d app
   ```

5. **Verify the application is running**
   - Application: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui/index.html
   - phpMyAdmin: http://localhost:8089

### Quick Start - Create Your First User

```bash
# Register a new admin user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@supplychainx.com",
    "password": "admin123",
    "role": "ADMIN"
  }'
```

### Environment Variables

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/supplyChainX?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# Server Configuration
SERVER_PORT=8080
```

## 📚 API Documentation

### Interactive API Documentation

Access the Swagger UI at: http://localhost:8080/swagger-ui/index.html

### API Overview

#### User Management
- `POST /api/users/register` - Register new user
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### Procurement (Approvisionnement)
- `GET /api/rawMaterials` - List raw materials
- `POST /api/rawMaterials` - Create raw material
- `GET /api/suppliers` - List suppliers
- `POST /api/suppliers` - Create supplier
- `GET /api/supply-orders` - List supply orders
- `POST /api/supply-orders` - Create supply order

#### Production
- `GET /api/products` - List products
- `POST /api/products` - Create product
- `GET /api/bom` - List BOMs
- `POST /api/bom` - Create BOM
- `GET /api/production-orders` - List production orders

#### Delivery (Livraison)
- `GET /api/customers` - List customers
- `POST /api/customers` - Create customer
- `GET /api/orders` - List orders
- `POST /api/orders` - Create order
- `GET /api/deliveries` - List deliveries
- `POST /api/deliveries` - Create delivery

### ⚠️ Authentication Status

**Authentication is currently DISABLED.** All API endpoints are publicly accessible without any credentials.

```bash
# No authentication required - just call the endpoints directly
curl http://localhost:8080/api/users
curl http://localhost:8080/api/products
```

For more details, see [AUTHENTICATION_REMOVED.md](AUTHENTICATION_REMOVED.md)

## 🔐 Security

### ⚠️ Current Security Status: Authentication Disabled

**IMPORTANT**: Authentication has been removed from this application. All endpoints are publicly accessible.

### User Roles (Defined but Not Enforced)

The following roles are defined in the system but are **not currently enforced**:

| Role | Intended Access Rights |
|------|--------------|
| `ADMIN` | Full system access |
| `GESTIONNAIRE_APPROVISIONNEMENT` | Raw materials, suppliers |
| `RESPONSABLE_ACHATS` | Supply orders |
| `SUPERVISEUR_LOGISTIQUE` | Cross-domain logistics view |
| `CHEF_PRODUCTION` | Production management |
| `PLANIFICATEUR` | Planning and scheduling |
| `SUPERVISEUR_PRODUCTION` | Production supervision |
| `GESTIONNAIRE_COMMERCIAL` | Customers, orders |
| `RESPONSABLE_LOGISTIQUE` | Delivery management |
| `SUPERVISEUR_LIVRAISONS` | Delivery supervision |

### Security Features Still Active

- ✅ Password encryption with BCrypt (for user records)
- ✅ CSRF protection (disabled for API)
- ✅ CORS configuration
- ✅ SQL injection prevention (JPA/Hibernate)

### Security Features Disabled

- ❌ Authentication (Basic Auth removed)
- ❌ Authorization (role-based access control not enforced)
- ❌ Protected endpoints (all endpoints are public)

### ⚠️ Security Warning

This configuration is **NOT SECURE** for production. Use only for:
- Local development
- Testing environments
- Prototyping

For production deployment, you must re-enable authentication. See [AUTHENTICATION_REMOVED.md](AUTHENTICATION_REMOVED.md) for details.

## 🧪 Testing

### Run All Tests
```powershell
./mvnw.cmd test
```

### Run Specific Test Class
```powershell
./mvnw.cmd test -Dtest=UserServiceTest
```

### Test Coverage
- Unit tests for services
- Integration tests for controllers
- H2 in-memory database for testing
- Mockito for mocking dependencies

## 🐳 Deployment

### Docker Deployment

1. **Build the Docker image**
   ```powershell
   docker build -t supplychainx:latest .
   ```

2. **Run with Docker Compose**
   ```powershell
   docker-compose up -d
   ```

### Production Considerations

- Use environment-specific configuration files
- Configure proper database credentials
- Enable HTTPS/TLS
- Set up monitoring and logging (ELK stack ready)
- Configure backup strategies
- Use secrets management (e.g., Docker secrets, Vault)

## 📊 Monitoring & Quality

### SonarQube Integration

Access SonarQube at: http://localhost:9001

```powershell
./mvnw.cmd sonar:sonar
```

### Database Administration

Access phpMyAdmin at: http://localhost:8089
- Username: `root`
- Password: `root`

## 📖 Documentation

Comprehensive documentation is available:

### Quick Start
- **[KEYCLOAK_QUICKSTART.md](KEYCLOAK_QUICKSTART.md)** - 5-minute Keycloak setup guide
- **[KEYCLOAK_SETUP.md](KEYCLOAK_SETUP.md)** - Detailed Keycloak configuration

### API & Development
- **[Swagger UI](http://localhost:8080/swagger-ui/index.html)** - Interactive API documentation
- **[API_DOCUMENTATION.md](docs/API_DOCUMENTATION.md)** - Complete API reference (if available)
- **[DEVELOPMENT_GUIDE.md](docs/DEVELOPMENT_GUIDE.md)** - Developer onboarding (if available)

### Security & Deployment
- **[SECURITY_GUIDE.md](docs/SECURITY_GUIDE.md)** - Security best practices (if available)
- **[DEPLOYMENT_GUIDE.md](docs/DEPLOYMENT_GUIDE.md)** - Production deployment guide (if available)

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Standards
- Follow Java naming conventions
- Write unit tests for new features
- Document public APIs with JavaDoc
- Ensure code passes SonarQube quality gates

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - *Initial work*

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Spring Security for robust authentication
- MapStruct for efficient object mapping
- The open-source community

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Email: support@supplychainx.com
- Documentation: [docs/](docs/)

---

**Built with ❤️ using Spring Boot**

