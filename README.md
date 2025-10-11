# 🍓 FruitStore - Hệ thống bán trái cây trực tuyến

## 📋 Tổng quan

FruitStore là một hệ thống bán trái cây trực tuyến được xây dựng bằng Java Web với kiến trúc MVC, sử dụng JSP + JSTL + Servlet + Tomcat. Hệ thống cung cấp đầy đủ các chức năng cho cả khách hàng và quản trị viên trong việc quản lý và mua bán trái cây.

## ✨ Tính năng chính

### 🧑‍♂️ Dành cho Khách hàng
- **Quản lý tài khoản**: Đăng ký, đăng nhập, quên mật khẩu, xác minh email
- **Hồ sơ cá nhân**: Cập nhật thông tin, quản lý địa chỉ giao hàng
- **Mua sắm**: Duyệt danh mục, tìm kiếm sản phẩm, xem chi tiết
- **Giỏ hàng**: Thêm/xóa sản phẩm, quản lý số lượng, tính tổng tiền
- **Đơn hàng**: Đặt hàng, thanh toán, theo dõi trạng thái
- **Khuyến mãi**: Sử dụng mã giảm giá, tham gia Flash Sale, mua combo
- **Tương tác**: Danh sách yêu thích, đánh giá sản phẩm
- **Thông báo**: Nhận thông báo về đơn hàng và khuyến mãi

### 👨‍💼 Dành cho Admin
- **Quản lý người dùng**: Xem danh sách, kích hoạt/vô hiệu hóa tài khoản
- **Quản lý sản phẩm**: CRUD sản phẩm, danh mục, hình ảnh
- **Quản lý đơn hàng**: Xem tất cả đơn hàng, cập nhật trạng thái
- **Quản lý khuyến mãi**: Tạo mã giảm giá, Flash Sale, combo
- **Thống kê**: Báo cáo doanh thu, sản phẩm bán chạy, hoạt động người dùng
- **Cài đặt hệ thống**: Quản lý cấu hình chung

## 🛠️ Công nghệ sử dụng

### Backend
- **Java Servlet** - Xử lý HTTP requests/responses
- **JSP (JavaServer Pages)** - Tạo giao diện web động
- **JSTL (JSP Standard Tag Library)** - Thư viện thẻ chuẩn cho JSP
- **JDBC** - Kết nối và thao tác với database
- **MVC Pattern** - Kiến trúc Model-View-Controller

### Database
- **MySQL** - Hệ quản trị cơ sở dữ liệu chính
- **UTF-8** - Hỗ trợ tiếng Việt

### Server
- **Apache Tomcat** - Web server và servlet container

### Frontend
- **HTML5/CSS3** - Cấu trúc và styling
- **JavaScript** - Tương tác người dùng
- **Bootstrap** (khuyến nghị) - Framework CSS responsive

## 🗄️ Cơ sở dữ liệu

Hệ thống sử dụng MySQL với các bảng chính:

### 👥 Quản lý người dùng
- `users` - Thông tin người dùng
- `shipping_addresses` - Địa chỉ giao hàng
- `user_activity_logs` - Lịch sử hoạt động
- `notifications` - Thông báo

### 🍓 Quản lý sản phẩm
- `products` - Thông tin sản phẩm
- `categories` - Danh mục chính
- `subcategories` - Danh mục con
- `product_images` - Hình ảnh sản phẩm
- `product_stats` - Thống kê sản phẩm
- `reviews` - Đánh giá sản phẩm

### 🛒 Quản lý đơn hàng
- `cart` - Giỏ hàng sản phẩm lẻ
- `cart_combos` - Giỏ hàng combo
- `orders` - Đơn hàng
- `order_items` - Chi tiết đơn hàng sản phẩm lẻ
- `order_combo_items` - Chi tiết đơn hàng combo
- `payments` - Thông tin thanh toán
- `order_status_history` - Lịch sử trạng thái

### 🎉 Quản lý khuyến mãi
- `combos` - Combo sản phẩm
- `combo_items` - Sản phẩm trong combo
- `discounts` - Mã giảm giá
- `discount_usage` - Lịch sử sử dụng mã giảm giá
- `flash_sales` - Flash sale
- `flash_sale_items` - Sản phẩm trong flash sale

### ❤️ Tương tác
- `wishlists` - Danh sách yêu thích

### ⚙️ Hệ thống
- `system_settings` - Cài đặt hệ thống

## 📚 API Documentation

### Base URL
```
Development: http://localhost:8888/FruitStore
Production:
```

### Các endpoint chính

Chi tiết đầy đủ xem trong [docs/APIDocumentation.md](docs/APIDocumentation.md)

## 👥 User Stories

Chi tiết xem trong [docs/UserStories.md](docs/UserStories.md)

## 🧪 Testing

### Functional Requirements

Chi tiết xem trong [docs/FunctionalRequirements.md](docs/FunctionalRequirements.md)

## 📄 License

Dự án này được phân phối dưới [MIT License](LICENSE). Xem file `LICENSE` để biết thêm chi tiết.