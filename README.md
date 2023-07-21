# Simple REST CRUD API with Spring Boot, PostgreSQL, JPA and Hibernate

## Pre requisites
- JDK 17**
- Maven 3.1+**

## Steps to Setup

**1. Clone the application**

```bash
https://github.com/GabyRibeiro/simple-products-api-spring.git
```

**2. Create PostgreSQL database**
```bash
create database products
```

**3. Change PostgreSQL username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. you can run the app without packaging it using**
```bash
mvn spring-boot:run
```

Or manually on your IDE.

The app will start running at <http://localhost:8080>.

## Explore Rest APIs

The app defines following CRUD APIs.

    GET /api/v1/products
    
    POST /api/v1/products
    
    GET /api/v1/products/{uuid}
    
    PUT /api/v1/products/{uuid}
    
    DELETE /api/v1/products/{uuid}
