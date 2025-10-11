## 🧍‍♂️ **CUSTOMER USER STORIES**

### **US01 – Đăng ký tài khoản**
**Given** người dùng truy cập trang đăng ký
**When** họ nhập email, username, mật khẩu và nhấn “Đăng ký”
**Then** hệ thống tạo tài khoản mới, gửi email xác minh, và hiển thị thông báo “Đăng ký thành công”.

### **US02 – Đăng nhập**
**Given** người dùng có tài khoản hợp lệ
**When** họ nhập đúng email/username và mật khẩu
**Then** hệ thống cho phép đăng nhập và chuyển đến trang chủ.

### **US03 – Quên mật khẩu**
**Given** người dùng quên mật khẩu
**When** họ nhập email khôi phục
**Then** hệ thống gửi liên kết đặt lại mật khẩu qua email.

### **US04 – Xác minh email**
**Given** người dùng vừa đăng ký
**When** họ nhấn liên kết xác minh trong email
**Then** tài khoản được đánh dấu `email_verified = TRUE`.

### **US05 – Cập nhật hồ sơ cá nhân**
**Given** người dùng đã đăng nhập
**When** họ chỉnh sửa thông tin cá nhân (họ tên, sđt, avatar, địa chỉ)
**Then** hệ thống cập nhật `users.updated_at` và lưu thay đổi thành công.

### **US06 – Xem lịch sử đăng nhập**
**Given** người dùng đã từng đăng nhập
**When** họ truy cập trang “Hoạt động gần đây”
**Then** hệ thống hiển thị danh sách log từ bảng `user_activity_logs` với IP và thời gian.

### **US07 – Xem danh mục sản phẩm**
**Given** người dùng truy cập trang cửa hàng
**When** họ chọn xem danh mục
**Then** hệ thống hiển thị danh sách từ bảng `categories` có `is_active = TRUE`.

### **US08 – Xem chi tiết sản phẩm**
**Given** người dùng chọn 1 sản phẩm
**When** họ truy cập trang chi tiết
**Then** hệ thống hiển thị thông tin từ bảng `products` + `product_images` + `reviews`.

### **US09 – Lọc sản phẩm**
**Given** người dùng muốn tìm sản phẩm
**When** họ chọn tiêu chí lọc (giá, danh mục, khuyến mãi, bán chạy)
**Then** hệ thống trả về danh sách sản phẩm thỏa điều kiện.

### **US10 – Xem sản phẩm Flash Sale / Combo**
**Given** có chương trình Flash Sale hoặc Combo
**When** người dùng truy cập phần “Ưu đãi”
**Then** hệ thống hiển thị danh sách từ `flash_sales` và `combos` đang `is_active = TRUE`.

### **US11 – Thêm sản phẩm vào giỏ hàng**
**Given** người dùng xem sản phẩm
**When** họ nhấn “Thêm vào giỏ”
**Then** hệ thống tạo bản ghi trong `cart` (user_id, product_id, quantity).

### **US12 – Cập nhật/xóa sản phẩm trong giỏ**
**Given** người dùng đã có sản phẩm trong giỏ
**When** họ chỉnh số lượng hoặc xóa
**Then** hệ thống cập nhật hoặc xóa bản ghi trong `cart`.

### **US13 – Chọn/Bỏ chọn sản phẩm**
**Given** người dùng mở giỏ hàng
**When** họ bật/tắt lựa chọn sản phẩm
**Then** hệ thống cập nhật cột `selected` trong bảng `cart`.

### **US14 – Thêm combo vào giỏ**
**Given** có combo đang hoạt động
**When** người dùng chọn combo và thêm vào giỏ
**Then** bản ghi được thêm vào `cart_combos`.

### **US15 – Tạo đơn hàng**
**Given** người dùng có sản phẩm trong giỏ
**When** họ xác nhận thanh toán
**Then** hệ thống tạo bản ghi trong `orders`, `order_items`, tính tổng tiền và trừ kho.

### **US16 – Thanh toán online**
**Given** người dùng chọn hình thức thanh toán Momo, ZaloPay…
**When** họ hoàn tất giao dịch
**Then** hệ thống cập nhật `payments.payment_status = 'completed'` và `orders.status = 'confirmed'`.

### **US17 – Xem trạng thái đơn hàng**
**Given** người dùng đã đặt hàng
**When** họ truy cập trang “Đơn hàng của tôi”
**Then** hệ thống hiển thị trạng thái từ `orders.status`.

### **US18 – Xem chi tiết đơn hàng**
**Given** người dùng mở chi tiết đơn hàng
**When** họ truy cập một đơn cụ thể
**Then** hệ thống hiển thị danh sách `order_items` + `order_combo_items` + phí ship, giảm giá.

### **US19 – Hủy đơn hàng**
**Given** đơn hàng đang ở trạng thái “pending” hoặc “confirmed”
**When** người dùng chọn “Hủy đơn”
**Then** hệ thống cập nhật `orders.status = 'cancelled'` và lưu vào `order_status_history`.

### **US20 – Nhập mã giảm giá**
**Given** người dùng có mã hợp lệ
**When** họ nhập mã vào form thanh toán
**Then** hệ thống kiểm tra bảng `discounts` và áp dụng `discount_value` nếu còn hiệu lực.

### **US21 – Xem danh sách mã giảm giá**
**Given** người dùng truy cập trang “Mã khuyến mãi”
**When** có mã đang `is_active = TRUE`
**Then** hệ thống hiển thị từ bảng `discounts`.

### **US22 – Tham gia Flash Sale**
**Given** đang trong thời gian Flash Sale
**When** người dùng thêm sản phẩm Flash Sale vào giỏ
**Then** hệ thống áp dụng giá `flash_sale_items.flash_sale_price` thay vì `products.price`.

### **US23 – Thêm vào danh sách yêu thích**
**Given** người dùng đăng nhập
**When** họ nhấn “❤️ Yêu thích” trên sản phẩm
**Then** hệ thống thêm bản ghi vào `wishlists`.

### **US24 – Đánh giá sản phẩm**

**Given** người dùng đã mua sản phẩm
**When** họ nhập đánh giá và gửi
**Then** hệ thống tạo bản ghi trong `reviews` với `is_verified_purchase = TRUE`.

### **US25 – Đánh dấu đánh giá hữu ích**
**Given** người dùng đọc đánh giá
**When** họ nhấn “Hữu ích”
**Then** hệ thống tăng `helpful_count` trong `reviews`.

### **US26 – Quản lý địa chỉ giao hàng**
**Given** người dùng có tài khoản
**When** họ thêm/sửa/xóa địa chỉ
**Then** hệ thống cập nhật `shipping_addresses`.

### **US27 – Chọn địa chỉ mặc định**
**Given** có nhiều địa chỉ giao hàng
**When** người dùng chọn một địa chỉ làm mặc định
**Then** hệ thống gán `is_default = TRUE` cho địa chỉ đó và `FALSE` cho các địa chỉ khác.

### **US28 – Nhận thông báo**
**Given** có sự kiện mới (đơn hàng, giảm giá, sản phẩm mới)
**When** người dùng đăng nhập
**Then** hệ thống hiển thị thông báo từ bảng `notifications`.

### **US29 – Đánh dấu thông báo đã đọc**
**Given** người dùng xem thông báo
**When** họ nhấn “Đã đọc”
**Then** hệ thống cập nhật `notifications.is_read = TRUE`.

## 🧑‍💼 **ADMIN USER STORIES**

### **US30 – Quản lý người dùng**
**Given** admin truy cập trang quản trị
**When** họ mở danh sách người dùng
**Then** hệ thống hiển thị thông tin từ bảng `users`.

### **US31 – Kích hoạt / Vô hiệu hóa người dùng**
**Given** admin chọn 1 người dùng
**When** họ thay đổi trạng thái
**Then** hệ thống cập nhật `users.is_active`.

### **US32 – Quản lý sản phẩm**
**Given** admin truy cập trang sản phẩm
**When** họ thêm hoặc sửa sản phẩm
**Then** hệ thống cập nhật bảng `products` và `product_images`.

### **US33 – Quản lý danh mục**
**Given** admin mở trang danh mục
**When** họ thêm, sửa, xóa danh mục
**Then** hệ thống thay đổi dữ liệu trong `categories` hoặc `subcategories`.

### **US34 – Gán sản phẩm vào danh mục**
**Given** admin chỉnh sửa sản phẩm
**When** họ chọn danh mục
**Then** hệ thống cập nhật `products.category_id` hoặc `subcategory_id`.

### **US35 – Xem tất cả đơn hàng**
**Given** admin vào trang quản lý đơn hàng
**When** họ lọc theo trạng thái hoặc ngày
**Then** hệ thống hiển thị từ bảng `orders`.

### **US36 – Cập nhật trạng thái đơn hàng**
**Given** admin chọn 1 đơn
**When** họ thay đổi trạng thái (ví dụ: “shipped”)
**Then** hệ thống cập nhật `orders.status` và thêm bản ghi vào `order_status_history`.

### **US37 – Xem lịch sử thay đổi trạng thái**
**Given** admin mở chi tiết đơn
**When** họ vào mục “Lịch sử”
**Then** hệ thống hiển thị các bản ghi từ `order_status_history`.

### **US38 – Tạo mã giảm giá**
**Given** admin mở trang mã giảm giá
**When** họ nhập thông tin và lưu
**Then** hệ thống tạo bản ghi trong `discounts`.

### **US39 – Xem thống kê sử dụng mã**
**Given** admin vào phần mã giảm giá
**When** họ xem chi tiết mã
**Then** hệ thống hiển thị số lần dùng từ `discount_usage.used_at`.

### **US40 – Tạo Flash Sale**
**Given** admin muốn khởi tạo Flash Sale
**When** họ nhập thông tin chương trình và sản phẩm tham gia
**Then** hệ thống lưu vào `flash_sales` và `flash_sale_items`.

### **US41 – Xem thống kê hệ thống**
**Given** admin truy cập Dashboard
**When** họ xem báo cáo
**Then** hệ thống hiển thị dữ liệu từ `product_stats`, `orders`, `payments`.

### **US42 – Xem log hoạt động người dùng**
**Given** admin truy cập trang log
**When** họ lọc theo user hoặc loại hoạt động
**Then** hệ thống hiển thị từ `user_activity_logs`.

### **US43 – Quản lý cài đặt hệ thống**
**Given** admin vào mục “System Settings”
**When** họ thay đổi giá trị cấu hình
**Then** hệ thống cập nhật `system_settings.setting_value`.