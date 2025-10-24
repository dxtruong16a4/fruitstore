-- ================================================
-- FRUITSTORE DATABASE SCHEMA - SIMPLIFIED VERSION
-- Version: 1.0 (School Project)
-- Created: 2025-10-15
-- Description: Simplified e-commerce database for small projects
-- ================================================

CREATE DATABASE IF NOT EXISTS FruitStore 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE FruitStore;

-- Set timezone
SET time_zone = '+07:00';
SET FOREIGN_KEY_CHECKS = 1;

-- ================================================
-- CORE TABLES (7 bảng chính)
-- ================================================

-- 1. Bảng người dùng (đơn giản hóa)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    role ENUM('customer', 'admin') DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- 2. Bảng danh mục
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_name (name)
);

-- 3. Bảng sản phẩm (đơn giản hóa)
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    image_url VARCHAR(255),
    category_id INT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_category (category_id),
    INDEX idx_price (price),
    INDEX idx_name (name),
    
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL,
    
    CONSTRAINT chk_price_positive CHECK (price > 0),
    CONSTRAINT chk_stock_quantity CHECK (stock_quantity >= 0)
);

-- 4. Bảng giỏ hàng
CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    
    UNIQUE KEY unique_user_cart (user_id)
);

-- 5. Bảng sản phẩm trong giỏ hàng
CREATE TABLE cart_items (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_cart_id (cart_id),
    INDEX idx_product_id (product_id),
    
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    
    CONSTRAINT chk_quantity_positive CHECK (quantity > 0),
    
    UNIQUE KEY unique_cart_product (cart_id, product_id)
);

-- 6. Bảng đơn hàng (đơn giản hóa)
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id INT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_address TEXT NOT NULL,
    phone_number VARCHAR(15),
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    notes TEXT,
    shipped_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_order_number (order_number),
    
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    
    CONSTRAINT chk_total_amount_positive CHECK (total_amount > 0)
);

-- 7. Bảng chi tiết đơn hàng
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    
    CONSTRAINT chk_order_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_unit_price_positive CHECK (unit_price > 0),
    CONSTRAINT chk_subtotal_positive CHECK (subtotal > 0)
);

-- 8. Bảng mã giảm giá (nâng cấp)
CREATE TABLE discounts (
    discount_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    discount_type ENUM('percentage', 'fixed_amount') DEFAULT 'percentage',
    discount_value DECIMAL(10,2) NOT NULL,
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    max_discount_amount DECIMAL(10,2),
    usage_limit INT DEFAULT NULL,
    used_count INT DEFAULT 0,
    start_date DATETIME,
    end_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_code (code),
    INDEX idx_is_active (is_active),
    INDEX idx_dates (start_date, end_date),
    
    CONSTRAINT chk_discount_value_positive CHECK (discount_value > 0),
    CONSTRAINT chk_usage_limit CHECK (usage_limit IS NULL OR usage_limit > 0),
    CONSTRAINT chk_used_count CHECK (used_count >= 0)
);

-- 9. Bảng tracking sử dụng mã giảm giá
CREATE TABLE discount_usage (
    usage_id INT PRIMARY KEY AUTO_INCREMENT,
    discount_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT,
    discount_amount DECIMAL(10,2) NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_discount_id (discount_id),
    INDEX idx_user_id (user_id),
    INDEX idx_order_id (order_id),
    INDEX idx_used_at (used_at),
    
    FOREIGN KEY (discount_id) REFERENCES discounts(discount_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE SET NULL,
    
    CONSTRAINT chk_discount_amount_positive CHECK (discount_amount >= 0)
);

-- ================================================
-- BASIC TRIGGERS (2 triggers đơn giản)
-- ================================================

-- Trigger 1: Cập nhật stock khi tạo đơn hàng
DELIMITER //
CREATE TRIGGER tr_update_stock_after_order
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE product_id = NEW.product_id;
END //
DELIMITER ;

-- Trigger 2: Tự động tăng used_count khi sử dụng mã giảm giá
DELIMITER //
CREATE TRIGGER tr_update_discount_usage
AFTER INSERT ON discount_usage
FOR EACH ROW
BEGIN
    UPDATE discounts 
    SET used_count = used_count + 1
    WHERE discount_id = NEW.discount_id;
END //
DELIMITER ;

-- ================================================
-- SAMPLE DATA (Dữ liệu mẫu để test)
-- ================================================

-- Thêm admin mặc định (password: admin123)
INSERT INTO users (username, email, password, full_name, phone, address, role) VALUES
('admin', 'admin@fruitstore.com', '$2a$10$IaW2ljXX8.v7M8QjJxxIJ.z.Qr75LTpW/WBr6IEF7oCYJMauI2PwK', 'Administrator', '0123456789', 'HCM City', 'admin'), -- password: Admin123
('customer1', 'customer1@email.com', '$2a$10$v0ZVhV2IXYBYvwzKLsxcgO2aNd.Cy/HH4/ABfRUqv1uHPEA8OzfmS', 'Nguyen Van A', '0987654321', 'Ha Noi', 'customer'); -- password: Customer123

-- Thêm danh mục
INSERT INTO categories (name, description, image_url) VALUES
('Trái cây tươi', 'Các loại trái cây tươi ngon, nhập khẩu và nội địa', '/images/categories/fresh-fruits.jpg'),
('Trái cây nhập khẩu', 'Trái cây nhập khẩu từ các nước', '/images/categories/imported.jpg'),
('Trái cây sấy', 'Các loại trái cây sấy khô dinh dưỡng', '/images/categories/dried.jpg'),
('Giỏ quà', 'Giỏ quà trái cây cao cấp', '/images/categories/gift.jpg');

-- Thêm sản phẩm mẫu
INSERT INTO products (name, description, price, stock_quantity, image_url, category_id) VALUES
('Táo Fuji Nhật Bản', 'Táo Fuji nhập khẩu từ Nhật Bản, ngọt tự nhiên', 150000, 100, '/images/products/apple-fuji.jpg', 2),
('Cam Úc', 'Cam tươi nhập khẩu từ Úc, giàu vitamin C', 80000, 150, '/images/products/orange.jpg', 2),
('Xoài Cát Hòa Lộc', 'Xoài Cát Hòa Lộc Tiền Giang ngon ngọt', 120000, 80, '/images/products/mango.jpg', 1),
('Dưa Hấu Không Hạt', 'Dưa hấu không hạt tươi ngon', 60000, 50, '/images/products/watermelon.jpg', 1),
('Nho Mỹ', 'Nho xanh không hạt nhập khẩu từ Mỹ', 200000, 60, '/images/products/grape.jpg', 2),
('Dâu Tây Đà Lạt', 'Dâu tây Đà Lạt tươi ngon', 180000, 40, '/images/products/strawberry.jpg', 1),
('Bơ Booth', 'Bơ Booth sáp Đắk Lắk', 90000, 70, '/images/products/avocado.jpg', 1),
('Kiwi Xanh', 'Kiwi xanh New Zealand', 130000, 90, '/images/products/kiwi.jpg', 2),
('Chuối Tiêu', 'Chuối tiêu Việt Nam ngọt tự nhiên', 45000, 120, '/images/products/banana.jpg', 1),
('Mít Thái', 'Mít Thái thơm ngon, múi to', 70000, 60, '/images/products/jackfruit.jpg', 1),
('Lê Hàn Quốc', 'Lê Hàn Quốc giòn ngọt', 160000, 80, '/images/products/pear.jpg', 2),
('Sầu Riêng Ri6', 'Sầu riêng Ri6 thơm béo', 200000, 40, '/images/products/durian.jpg', 1),
('Mận Hậu', 'Mận Hậu Đà Lạt giòn ngọt', 65000, 100, '/images/products/plum.jpg', 1),
('Chôm Chôm', 'Chôm chôm Đồng Nai ngọt thanh', 55000, 90, '/images/products/rambutan.jpg', 1),
('Táo Envy', 'Táo Envy nhập khẩu từ Mỹ', 180000, 70, '/images/products/envy-apple.jpg', 2),
('Vải Thiều', 'Vải thiều Lục Ngạn tươi ngon', 60000, 110, '/images/products/lychee.jpg', 1),
('Hồng Xiêm', 'Hồng xiêm Tiền Giang ngọt lịm', 50000, 80, '/images/products/sapodilla.jpg', 1),
('Cherry Chile', 'Cherry Chile đỏ mọng, ngọt', 250000, 50, '/images/products/cherry.jpg', 2);

-- Thêm mã giảm giá mẫu
INSERT INTO discounts (code, description, discount_type, discount_value, min_order_amount, max_discount_amount, usage_limit, start_date, end_date, is_active) VALUES
('WELCOME10', 'Giảm 10% cho đơn hàng đầu tiên', 'percentage', 10, 100000, 50000, 100, '2025-01-01 00:00:00', '2025-12-31 23:59:59', TRUE),
('FRUIT50K', 'Giảm 50k cho đơn từ 500k', 'fixed_amount', 50000, 500000, NULL, 200, '2025-01-01 00:00:00', '2025-12-31 23:59:59', TRUE),
('SALE20', 'Giảm 20% không giới hạn', 'percentage', 20, 200000, 100000, NULL, '2025-01-01 00:00:00', '2025-06-30 23:59:59', TRUE),
('FREESHIP', 'Miễn phí vận chuyển đơn từ 300k', 'fixed_amount', 30000, 300000, 30000, 500, '2025-01-01 00:00:00', '2025-12-31 23:59:59', TRUE),
('VIP15', 'Giảm 15% cho khách VIP', 'percentage', 15, 0, 200000, 50, '2025-01-01 00:00:00', '2025-12-31 23:59:59', TRUE);
