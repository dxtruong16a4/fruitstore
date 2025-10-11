## 🧑‍💼 1. Quản lý Người dùng & Tài khoản

**Mô tả:** Người dùng có thể đăng ký, đăng nhập, quản lý thông tin và lịch sử hoạt động.
**Chức năng:**

* [FR-01] Đăng ký tài khoản mới (username, email, password, thông tin cá nhân).
* [FR-02] Đăng nhập / đăng xuất hệ thống.
* [FR-03] Quản lý hồ sơ cá nhân (họ tên, số điện thoại, địa chỉ, avatar).
* [FR-04] Quản lý nhiều địa chỉ giao hàng, đánh dấu địa chỉ mặc định.
* [FR-05] Xác thực email khi đăng ký tài khoản.
* [FR-06] Theo dõi lịch sử hoạt động (login, xem sản phẩm, đặt hàng, đánh giá).
* [FR-07] Quản lý vai trò người dùng (`customer`, `admin`).
* [FR-08] Kích hoạt / vô hiệu hóa tài khoản người dùng (Admin).

---

## 🛍️ 2. Quản lý Danh mục & Sản phẩm

**Mô tả:** Hệ thống cho phép thêm, chỉnh sửa, xóa và hiển thị danh mục và sản phẩm.
**Chức năng:**

* [FR-09] Quản lý danh mục chính và danh mục con (CRUD).
* [FR-10] Quản lý thông tin sản phẩm (CRUD).
* [FR-11] Liên kết sản phẩm với danh mục hoặc danh mục con.
* [FR-12] Đánh dấu sản phẩm “mới”, “bán chạy”, “nổi bật”.
* [FR-13] Quản lý hình ảnh sản phẩm (ảnh chính và ảnh phụ).
* [FR-14] Theo dõi tồn kho sản phẩm, cảnh báo khi dưới mức tối thiểu.
* [FR-15] Thống kê lượng sản phẩm bán ra (tổng số, lần cuối bán).

---

## 🧃 3. Quản lý Combo & Flash Sale

**Mô tả:** Hỗ trợ tạo các gói combo hoặc chương trình giảm giá nhanh.
**Chức năng:**

* [FR-16] Tạo combo sản phẩm với giá ưu đãi, quản lý thời gian hiệu lực.
* [FR-17] Quản lý sản phẩm trong combo (CRUD).
* [FR-18] Tạo sự kiện Flash Sale, gán sản phẩm và giá giảm riêng.
* [FR-19] Giới hạn số lượng sản phẩm Flash Sale, cập nhật khi bán.

---

## 🛒 4. Giỏ hàng & Danh sách yêu thích

**Chức năng:**

* [FR-20] Thêm sản phẩm hoặc combo vào giỏ hàng.
* [FR-21] Cập nhật số lượng, chọn / bỏ chọn sản phẩm trong giỏ.
* [FR-22] Xóa sản phẩm khỏi giỏ hàng.
* [FR-23] Lưu danh sách yêu thích (wishlist).
* [FR-24] Tự động tính tổng tiền giỏ hàng, hiển thị giá giảm (nếu có).

---

## 📦 5. Đặt hàng & Thanh toán

**Chức năng:**

* [FR-25] Tạo đơn hàng từ giỏ hàng, chọn địa chỉ giao hàng và phương thức thanh toán.
* [FR-26] Quản lý nhiều phương thức thanh toán (COD, thẻ, chuyển khoản, ví điện tử).
* [FR-27] Cập nhật trạng thái đơn hàng (pending → delivered).
* [FR-28] Quản lý lịch sử trạng thái đơn hàng.
* [FR-29] Ghi nhận thông tin thanh toán, mã giao dịch, kết quả xử lý.
* [FR-30] Cập nhật tồn kho sau khi thanh toán thành công.

---

## 💰 6. Mã giảm giá & Khuyến mãi

**Chức năng:**

* [FR-31] Tạo và quản lý mã giảm giá (`percentage`, `fixed_amount`).
* [FR-32] Kiểm tra điều kiện áp dụng (đơn hàng tối thiểu, giới hạn sử dụng).
* [FR-33] Ghi nhận mã giảm giá đã sử dụng.
* [FR-34] Áp dụng mã giảm giá tự động khi người dùng nhập mã hợp lệ.

---

## ⭐ 7. Đánh giá & Thông báo

**Chức năng:**

* [FR-35] Người dùng có thể đánh giá sản phẩm đã mua (rating, comment).
* [FR-36] Hiển thị đánh giá trung bình và số lượt đánh giá.
* [FR-37] Gửi thông báo đến người dùng khi có sản phẩm mới, giảm giá, hoặc cập nhật đơn hàng.
* [FR-38] Quản lý trạng thái đọc / chưa đọc thông báo.

---

## ⚙️ 8. Quản trị hệ thống

**Chức năng:**

* [FR-39] Cấu hình thông tin hệ thống (setting key-value).
* [FR-40] Thống kê doanh thu, sản phẩm bán chạy, người dùng hoạt động.
* [FR-41] Quản lý toàn bộ dữ liệu (user, order, review, flash sale, combo).
* [FR-42] Ghi nhận log hoạt động người dùng trong hệ thống.