## 🧑‍💼 Quản lý Người dùng và Tài khoản
Đây là nhóm bảng quản lý thông tin và hoạt động của khách hàng.
- users: Lưu trữ thông tin cơ bản của người dùng.
- shipping_addresses: Lưu các địa chỉ giao hàng của người dùng. Một người dùng có thể có nhiều địa chỉ.
- user_activity_logs: Ghi lại lịch sử hoạt động của người dùng trên hệ thống.
- notifications: Chứa các thông báo được gửi đến người dùng.

## 🍓 Quản lý Sản phẩm và Danh mục
Nhóm này chịu trách nhiệm về tất cả thông tin liên quan đến sản phẩm được bán.
- products: Bảng quan trọng nhất, chứa thông tin chi tiết về từng loại trái cây.
- categories: Phân loại sản phẩm ở cấp cao nhất.
- subcategories: Phân loại sản phẩm ở cấp thấp hơn, thuộc về một category.
- product_images: Lưu trữ đường dẫn đến các hình ảnh của sản phẩm. Một sản phẩm có thể có nhiều hình ảnh.
- product_stats: Theo dõi các chỉ số của sản phẩm như lượt xem, số lượt bán, số lượt thêm vào giỏ hàng. Dữ liệu này giúp xác định sản phẩm bán chạy.
- reviews: Chứa các bài đánh giá, bình luận và xếp hạng của người dùng về sản phẩm.

## 🛒 Quản lý Đơn hàng và Thanh toán
Nhóm này xử lý toàn bộ quy trình từ khi khách hàng bỏ hàng vào giỏ cho đến khi thanh toán và nhận hàng.
- cart: Lưu thông tin giỏ hàng hiện tại của mỗi người dùng. Dữ liệu trong bảng này là tạm thời và sẽ bị xóa sau khi người dùng đặt hàng hoặc xóa khỏi giỏ.
- orders: Lưu thông tin về các đơn hàng đã được xác nhận.
- order_items: Lưu chi tiết các sản phẩm có trong một đơn hàng.
- order_status_history: Ghi lại lịch sử thay đổi trạng thái của một đơn hàng.
- payments: Lưu thông tin về các giao dịch thanh toán cho đơn hàng, bao gồm phương thức thanh toán, trạng thái (thành công, thất bại), mã giao dịch.

## 🎉 Quản lý Khuyến mãi và Ưu đãi
Nhóm này quản lý các chương trình marketing để thu hút khách hàng.
- combos: Định nghĩa các gói sản phẩm (combo) được bán cùng nhau với giá ưu đãi.
- combo_items: Liệt kê các sản phẩm cụ thể và số lượng của chúng trong một combo.
- cart_combos: Lưu các combo mà người dùng đã thêm vào giỏ hàng.
- order_combo_items: Lưu thông tin các combo trong một đơn hàng đã được đặt thành công.
- discounts: Chứa thông tin về các mã giảm giá (voucher).
- discount_usage: Theo dõi việc sử dụng mã giảm giá của người dùng để đảm bảo mỗi mã chỉ được dùng theo đúng quy định.
- flash_sales: Định nghĩa các chương trình giảm giá chớp nhoáng (flash sale) với thời gian và số lượng sản phẩm giới hạn.
- flash_sale_items: Liệt kê các sản phẩm được áp dụng trong một chương trình flash sale cùng với giá đặc biệt của chúng.

## ❤️ Tương tác và Cá nhân hóa
Nhóm này giúp tăng trải nghiệm cá nhân của người dùng trên trang web.
- wishlists: Lưu danh sách các sản phẩm mà người dùng yêu thích hoặc muốn mua sau này.

## ⚙️ Hệ thống và Quản trị
Nhóm này chứa các bảng phục vụ cho việc vận hành và quản trị hệ thống.
- system_settings: Lưu trữ các cài đặt chung cho toàn bộ hệ thống, ví dụ như phí vận chuyển mặc định, thông tin liên hệ của cửa hàng, logo.