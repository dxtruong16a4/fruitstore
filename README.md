# 🍎 FruitStore - Jakarta EE E-Commerce Application

A modern e-commerce web application for fruit sales built with Jakarta EE 10, featuring a complete shopping experience with user authentication, shopping cart, and admin management system.

## 🌟 Features

### Customer Features
- **User Registration & Authentication** - Secure login/logout with session management
- **Product Catalog** - Browse fruits by category with search functionality
- **Shopping Cart** - Add, remove, and manage items in cart
- **Order Management** - Place orders and track order history
- **Responsive Design** - Mobile-friendly Bootstrap 5 interface

### Admin Features
- **Admin Dashboard** - Comprehensive management interface
- **Product Management** - Add, edit, delete products and manage inventory
- **Order Management** - View and update order status
- **User Management** - Monitor customer accounts
- **Sales Reports** - Track business performance

## 🏗️ Architecture

### Technology Stack
- **Backend**: Jakarta EE 10 (Servlets, JSP, JSTL)
- **Database**: MySQL 8.0+
- **Frontend**: Bootstrap 5, HTML5, CSS3, JavaScript
- **Server**: Apache Tomcat 10.x
- **Build Tool**: Apache Ant (NetBeans)

### Project Structure
```
FruitStore/
├── src/java/
│   ├── controller/     # Servlet controllers
│   ├── dao/           # Data Access Objects
│   ├── model/         # Entity classes
│   └── util/          # Utility classes
├── web/
│   ├── jsp/           # JSP view templates
│   ├── css/           # Stylesheets
│   ├── js/            # JavaScript files
│   ├── images/        # Product images
│   └── WEB-INF/       # Web configuration
└── docs/              # Documentation
```

### Design Patterns
- **MVC (Model-View-Controller)** - Clean separation of concerns
- **DAO Pattern** - Database access abstraction
- **Singleton Pattern** - Database connection management
- **Session Management** - User authentication and authorization

## 🚀 Quick Start

### Prerequisites
- Java JDK 11 or higher
- Apache Tomcat 10.x
- MySQL Server 8.0+
- NetBeans IDE (recommended)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/FruitStore.git
   cd FruitStore
   ```

2. **Setup Database**
   ```sql
   # Create database
   CREATE DATABASE fruitstore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # Import schema (use SQL from docs/mysql_db.md)
   mysql -u root -p fruitstore < docs/mysql_db.md
   ```

3. **Configure Database Connection**
   
   Edit `src/java/util/DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/fruitstore";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

4. **Build and Deploy**
   ```bash
   # Using NetBeans: Clean and Build project
   # Or using Ant directly:
   ant clean compile dist
   
   # Copy web/ contents to Tomcat webapps/FruitStore/
   # Or deploy the generated .war file
   ```

5. **Start Application**
   - Start Tomcat server
   - Access: `http://localhost:8080/FruitStore`

## 📊 Database Schema

The application uses 4 main tables:

- **users** - Customer and admin accounts
- **products** - Fruit inventory with categories
- **cart** - Shopping cart items
- **orders** - Order tracking and history

See `docs/mysql_db.md` for complete schema.

## 🔐 Security Features

- **Role-based Access Control** - Customer/Admin roles
- **Session Management** - Secure authentication
- **SQL Injection Prevention** - Prepared statements
- **Input Validation** - Server-side validation

## 📝 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/products` | GET | View product catalog |
| `/cart` | GET/POST | Manage shopping cart |
| `/orders` | GET/POST | Order management |
| `/login` | GET/POST | User authentication |
| `/admin/*` | GET/POST | Admin operations |

## 🎯 Demo Data

Default admin account:
- **Username**: admin
- **Password**: admin123

Sample products are included in the database setup.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Jakarta EE community for excellent documentation
- Bootstrap team for the responsive framework
- MySQL for robust database support

---

⭐ **Star this repository if you find it helpful!**