# 🌐 Servlet Routes - FruitStore

## 📋 Tổng quan

Danh sách tất cả Servlet routes cho hệ thống bán trái cây trực tuyến FruitStore sử dụng JSP + JSTL + Servlet + Tomcat.

### Base URL
```
Development: http://localhost:8888/FruitStore
Production:
```

### Authentication
```
HttpSession - Lưu trữ thông tin user trong session
session.setAttribute("user", userObject);
```

---

## 🔐 1. Authentication & User Management

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AuthServlet` | `login` | Hiển thị form đăng nhập |
| POST | `/AuthServlet` | `login` | Xử lý đăng nhập |
| GET | `/AuthServlet` | `register` | Hiển thị form đăng ký |
| POST | `/AuthServlet` | `register` | Xử lý đăng ký |
| GET | `/AuthServlet` | `logout` | Đăng xuất |
| GET | `/AuthServlet` | `verify-email` | Xác minh email |
| GET | `/AuthServlet` | `forgot-password` | Hiển thị form quên mật khẩu |
| POST | `/AuthServlet` | `forgot-password` | Xử lý quên mật khẩu |
| GET | `/AuthServlet` | `reset-password` | Hiển thị form đặt lại mật khẩu |
| POST | `/AuthServlet` | `reset-password` | Xử lý đặt lại mật khẩu |
| GET | `/UserServlet` | `profile` | Hiển thị profile |
| POST | `/UserServlet` | `update` | Cập nhật profile |
| POST | `/UserServlet` | `change-password` | Đổi mật khẩu |
| GET | `/UserServlet` | `activity-logs` | Lấy lịch sử hoạt động |
| GET | `/ShippingAddressServlet` | `list` | Lấy danh sách địa chỉ giao hàng |
| POST | `/ShippingAddressServlet` | `add` | Thêm địa chỉ giao hàng |
| POST | `/ShippingAddressServlet` | `update` | Cập nhật địa chỉ giao hàng |
| POST | `/ShippingAddressServlet` | `delete` | Xóa địa chỉ giao hàng |
| POST | `/ShippingAddressServlet` | `set-default` | Đặt địa chỉ mặc định |

## 🍓 2. Product & Category Management

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/CategoryServlet` | `list` | Lấy danh sách danh mục |
| GET | `/CategoryServlet` | `detail` | Lấy chi tiết danh mục |
| GET | `/SubcategoryServlet` | `list` | Lấy danh sách danh mục con |
| GET | `/SubcategoryServlet` | `detail` | Lấy chi tiết danh mục con |
| GET | `/ProductServlet` | `list` | Lấy danh sách sản phẩm |
| GET | `/ProductServlet` | `detail` | Lấy chi tiết sản phẩm |
| GET | `/ProductServlet` | `search` | Tìm kiếm sản phẩm |
| GET | `/ProductServlet` | `featured` | Lấy sản phẩm nổi bật |
| GET | `/ProductServlet` | `new` | Lấy sản phẩm mới |
| GET | `/ProductServlet` | `best-seller` | Lấy sản phẩm bán chạy |
| GET | `/ReviewServlet` | `list` | Lấy đánh giá sản phẩm |
| POST | `/ReviewServlet` | `add` | Thêm đánh giá sản phẩm |
| POST | `/ReviewServlet` | `helpful` | Đánh dấu review hữu ích |
| GET | `/ProductImageServlet` | `list` | Lấy hình ảnh sản phẩm |

## 🛒 3. Cart & Wishlist Management

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/CartServlet` | `view` | Hiển thị giỏ hàng |
| POST | `/CartServlet` | `add` | Thêm sản phẩm vào giỏ hàng |
| POST | `/CartServlet` | `update` | Cập nhật số lượng sản phẩm |
| POST | `/CartServlet` | `toggle-selection` | Chọn/bỏ chọn sản phẩm |
| POST | `/CartServlet` | `remove` | Xóa sản phẩm khỏi giỏ hàng |
| POST | `/CartServlet` | `clear` | Xóa toàn bộ giỏ hàng |
| POST | `/CartServlet` | `add-combo` | Thêm combo vào giỏ hàng |
| POST | `/CartServlet` | `update-combo` | Cập nhật combo trong giỏ |
| POST | `/CartServlet` | `remove-combo` | Xóa combo khỏi giỏ hàng |
| GET | `/WishlistServlet` | `list` | Lấy danh sách yêu thích |
| POST | `/WishlistServlet` | `add` | Thêm vào danh sách yêu thích |
| POST | `/WishlistServlet` | `remove` | Xóa khỏi danh sách yêu thích |

## 📦 4. Order Management

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/OrderServlet` | `list` | Hiển thị danh sách đơn hàng |
| GET | `/OrderServlet` | `detail` | Lấy chi tiết đơn hàng |
| GET | `/OrderServlet` | `checkout` | Hiển thị trang checkout |
| POST | `/OrderServlet` | `create` | Tạo đơn hàng |
| POST | `/OrderServlet` | `cancel` | Hủy đơn hàng |
| GET | `/OrderServlet` | `status-history` | Lấy lịch sử trạng thái đơn hàng |
| POST | `/PaymentServlet` | `process` | Xử lý thanh toán |
| GET | `/PaymentServlet` | `detail` | Lấy thông tin thanh toán |
| POST | `/PaymentServlet` | `verify` | Xác minh thanh toán |

## 🎉 5. Promotions & Flash Sales

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/DiscountServlet` | `list` | Lấy danh sách mã giảm giá |
| GET | `/DiscountServlet` | `detail` | Lấy chi tiết mã giảm giá |
| POST | `/DiscountServlet` | `validate` | Kiểm tra mã giảm giá |
| GET | `/FlashSaleServlet` | `list` | Lấy danh sách Flash Sale |
| GET | `/FlashSaleServlet` | `detail` | Lấy chi tiết Flash Sale |
| GET | `/FlashSaleServlet` | `active` | Lấy Flash Sale đang hoạt động |
| GET | `/ComboServlet` | `list` | Lấy danh sách combo |
| GET | `/ComboServlet` | `detail` | Lấy chi tiết combo |
| GET | `/ComboServlet` | `active` | Lấy combo đang hoạt động |

## 👨‍💼 6. Admin APIs

### 6.1 Quản lý sản phẩm

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `products` | Hiển thị danh sách sản phẩm (Admin) |
| POST | `/AdminServlet` | `product-create` | Tạo sản phẩm mới |
| GET | `/AdminServlet` | `product-detail` | Lấy chi tiết sản phẩm (Admin) |
| POST | `/AdminServlet` | `product-update` | Cập nhật sản phẩm |
| POST | `/AdminServlet` | `product-delete` | Xóa sản phẩm |
| POST | `/AdminServlet` | `product-image-add` | Thêm hình ảnh sản phẩm |
| POST | `/AdminServlet` | `product-image-update` | Cập nhật hình ảnh |
| POST | `/AdminServlet` | `product-image-delete` | Xóa hình ảnh |

### 6.2 Quản lý danh mục

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `categories` | Hiển thị danh sách danh mục (Admin) |
| POST | `/AdminServlet` | `category-create` | Tạo danh mục mới |
| POST | `/AdminServlet` | `category-update` | Cập nhật danh mục |
| POST | `/AdminServlet` | `category-delete` | Xóa danh mục |
| GET | `/AdminServlet` | `subcategories` | Hiển thị danh sách danh mục con (Admin) |
| POST | `/AdminServlet` | `subcategory-create` | Tạo danh mục con mới |
| POST | `/AdminServlet` | `subcategory-update` | Cập nhật danh mục con |
| POST | `/AdminServlet` | `subcategory-delete` | Xóa danh mục con |

### 6.3 Quản lý đơn hàng

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `orders` | Hiển thị tất cả đơn hàng |
| GET | `/AdminServlet` | `order-detail` | Lấy chi tiết đơn hàng (Admin) |
| POST | `/AdminServlet` | `order-status-update` | Cập nhật trạng thái đơn hàng |
| GET | `/AdminServlet` | `order-statistics` | Thống kê đơn hàng |

### 6.4 Quản lý người dùng

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `users` | Hiển thị danh sách người dùng |
| GET | `/AdminServlet` | `user-detail` | Lấy chi tiết người dùng |
| POST | `/AdminServlet` | `user-status-update` | Cập nhật trạng thái người dùng |
| GET | `/AdminServlet` | `user-activity-logs` | Lấy log hoạt động người dùng |

### 6.5 Quản lý khuyến mãi

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `discounts` | Hiển thị danh sách mã giảm giá (Admin) |
| POST | `/AdminServlet` | `discount-create` | Tạo mã giảm giá |
| POST | `/AdminServlet` | `discount-update` | Cập nhật mã giảm giá |
| POST | `/AdminServlet` | `discount-delete` | Xóa mã giảm giá |
| GET | `/AdminServlet` | `flash-sales` | Hiển thị danh sách Flash Sale (Admin) |
| POST | `/AdminServlet` | `flash-sale-create` | Tạo Flash Sale |
| POST | `/AdminServlet` | `flash-sale-update` | Cập nhật Flash Sale |
| POST | `/AdminServlet` | `flash-sale-delete` | Xóa Flash Sale |
| GET | `/AdminServlet` | `combos` | Hiển thị danh sách combo (Admin) |
| POST | `/AdminServlet` | `combo-create` | Tạo combo |
| POST | `/AdminServlet` | `combo-update` | Cập nhật combo |
| POST | `/AdminServlet` | `combo-delete` | Xóa combo |

### 6.6 Thống kê & Báo cáo

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/AdminServlet` | `dashboard` | Dashboard thống kê tổng quan |
| GET | `/AdminServlet` | `sales-report` | Báo cáo doanh thu |
| GET | `/AdminServlet` | `product-report` | Báo cáo sản phẩm |
| GET | `/AdminServlet` | `customer-report` | Báo cáo khách hàng |
| GET | `/AdminServlet` | `system-settings` | Lấy cài đặt hệ thống |
| POST | `/AdminServlet` | `system-setting-update` | Cập nhật cài đặt hệ thống |

## 🔔 7. Notifications

| Method | URL | Action Parameter | Description |
|--------|-----|------------------|-------------|
| GET | `/NotificationServlet` | `list` | Hiển thị danh sách thông báo |
| GET | `/NotificationServlet` | `unread` | Lấy thông báo chưa đọc |
| POST | `/NotificationServlet` | `mark-read` | Đánh dấu đã đọc |
| POST | `/NotificationServlet` | `mark-all-read` | Đánh dấu tất cả đã đọc |
| POST | `/NotificationServlet` | `delete` | Xóa thông báo |

---

## 📝 Notes

- **Tech Stack**: JSP + JSTL + Servlet + Tomcat + MVC Pattern
- **Base URL**: `http://localhost:8080/FruitStore`
- **Authentication**: HttpSession thay vì JWT
- **Request Parameters**: Sử dụng `action` parameter để phân biệt các chức năng
- **Response**: Forward đến JSP pages hoặc Redirect
- **Database**: MySQL với JDBC
- **Admin Routes**: Yêu cầu role = 'admin' trong session
- **Pagination**: `?page=1&limit=20`
- **Currency**: VND
- **File Upload**: MultipartRequest cho hình ảnh

### Servlet Mapping Example:
```java
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        switch(action) {
            case "list":
                // Hiển thị danh sách sản phẩm
                break;
            case "detail":
                // Hiển thị chi tiết sản phẩm
                break;
        }
    }
}
```

---

*Tài liệu này được cập nhật lần cuối: 11/10/2025*
