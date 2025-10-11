# Database Configuration Package

This package contains the database connection configuration for the FruitStore application.

## Files Overview

### Core Configuration Files

1. **`DatabaseConnection.java`** - Main database connection class
   - Manages connection pooling using Apache Commons DBCP2
   - Provides singleton access to database connections
   - Handles connection pool initialization and shutdown

2. **`database.properties`** - Database configuration properties
   - Contains database connection parameters
   - Configures connection pool settings
   - Defines MySQL-specific settings

3. **`DatabaseInitializer.java`** - Database schema initialization
   - Handles database setup on application startup
   - Executes schema creation scripts
   - Provides sample data creation for development

4. **`ApplicationContextListener.java`** - Servlet context listener
   - Manages application lifecycle events
   - Initializes database on startup
   - Cleans up resources on shutdown

## Configuration

### Database Properties

Edit `database.properties` to configure your database connection:

```properties
# Database connection
db.url=jdbc:mysql://localhost:3306/FruitStore?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=your_username
db.password=your_password

# Connection pool settings
db.pool.initialSize=5
db.pool.maxActive=20
db.pool.maxIdle=10
```

### Required Dependencies

Add these dependencies to your project:

```xml
<!-- MySQL Connector -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Apache Commons DBCP2 for connection pooling -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.9.0</version>
</dependency>
```

