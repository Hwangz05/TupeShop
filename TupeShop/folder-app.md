# TupeShop - Ứng dụng Bán Hàng Android (Java)

> **Lưu ý quan trọng:**  
> Dự án đã được chuyển đổi hoàn toàn sang **100% Java thuần** và **Android XML Layout** (loại bỏ hoàn toàn Kotlin & Jetpack Compose), đảm bảo đơn giản, dễ học, dễ bảo trì và chạy mượt mà.  
> **Thư mục dự án:** `D:\BTL_Android\PTUDDD\cellphonesclone`

---

## 1. Cấu trúc thư mục mã nguồn

```
app/src/main/
├── java/com/example/cellphonesclone/
│   ├── model/                  # Các lớp thực thể (Entity Room)
│   │   ├── Product.java        # Sản phẩm (id, name, price, imageUrl, description, category)
│   │   ├── User.java           # Người dùng (id, username, password, fullName, phone, email, address, isAdmin)
│   │   └── CartItem.java       # Mục giỏ hàng (id, userId, productId, name, price, quantity)
│   │
│   ├── dao/                    # Giao diện truy vấn dữ liệu Room DAO
│   │   ├── ProductDao.java     # CRUD sản phẩm, tìm kiếm theo tên
│   │   ├── UserDao.java        # CRUD người dùng, đăng nhập, tìm theo username
│   │   └── CartDao.java        # CRUD giỏ hàng, tăng giảm số lượng, dọn giỏ hàng
│   │
│   ├── database/               # Cơ sở dữ liệu SQLite cục bộ
│   │   └── AppDatabase.java    # Khởi tạo Room Database (Singleton Pattern)
│   │
│   ├── adapter/                # Bộ điều hợp hiển thị danh sách (RecyclerView)
│   │   ├── ProductAdapter.java # Lưới sản phẩm 2 cột (CardView, thêm giỏ, sửa, xóa, xem chi tiết)
│   │   ├── CartAdapter.java    # Danh sách giỏ hàng (tăng, giảm số lượng, xóa)
│   │   └── UserAdapter.java    # Danh sách người dùng (sửa, xóa tài khoản)
│   │
│   └── activity/               # Các màn hình chức năng của ứng dụng
│       ├── LoginActivity.java         # Màn hình đăng nhập (tạo sẵn tài khoản admin mặc định)
│       ├── RegisterActivity.java      # Màn hình đăng ký tài khoản mới
│       ├── MainActivity.java          # Màn hình chính (Header TupeShop, 3 Tab điều hướng)
│       ├── ProductDetailActivity.java # Màn hình xem chi tiết sản phẩm
│       ├── ProductFormActivity.java   # Màn hình thêm mới & chỉnh sửa sản phẩm
│       ├── CartActivity.java          # Màn hình giỏ hàng chuyên biệt
│       └── UserFormActivity.java      # Màn hình thêm mới & chỉnh sửa người dùng
│
└── res/
    ├── layout/                        # Giao diện XML
    │   ├── activity_main.xml          # Màn hình chính (Header đỏ TupeShop + 3 Tab)
    │   ├── activity_login.xml         # Giao diện Đăng nhập
    │   ├── activity_register.xml      # Giao diện Đăng ký
    │   ├── activity_product_detail.xml# Giao diện Chi tiết sản phẩm
    │   ├── activity_product_form.xml  # Giao diện Form Sản phẩm
    │   ├── activity_user_form.xml     # Giao diện Form Người dùng
    │   ├── activity_cart.xml          # Giao diện Giỏ hàng
    │   ├── item_product.xml           # Thẻ sản phẩm dạng lưới (CardView 2 cột)
    │   ├── item_cart.xml              # Dòng sản phẩm trong giỏ hàng
    │   └── item_user.xml              # Dòng người dùng trong danh sách
    └── values/
        ├── colors.xml                 # Màu sắc chủ đạo (Đỏ TupeShop #E53935)
        ├── strings.xml                # Tên ứng dụng (TupeShop)
        └── themes.xml                 # Giao diện NoActionBar (Loại bỏ thanh tiêu đề mặc định)
```

---

## 2. Các chức năng chính của ứng dụng

| Nhóm chức năng | Chi tiết tính năng |
|----------------|--------------------|
| 🔐 **Xác thực** | • Đăng ký tài khoản mới.<br>• Đăng nhập hệ thống.<br>• Tài khoản quản trị mặc định: `admin` / `admin123`. |
| 📱 **Sản phẩm** | • Hiển thị danh sách sản phẩm dạng lưới 2 cột đẹp mắt.<br>• Tự động hiển thị sẵn 2 sản phẩm mẫu ban đầu: **iPhone 15 Pro Max 256GB** và **Samsung Galaxy S24 Ultra**.<br>• Tìm kiếm tức thì theo tên trên thanh search bo tròn "Bạn muốn mua gì ?".<br>• Xem chi tiết đầy đủ thông tin mô tả, giá tiền.<br>• Thêm mới, Chỉnh sửa thông tin, Xóa sản phẩm. |
| 🛒 **Giỏ hàng** | • Nút thêm nhanh vào giỏ `+ Giỏ` trực tiếp từ màn hình chính hoặc chi tiết.<br>• Tăng / giảm số lượng linh hoạt.<br>• Xóa từng sản phẩm hoặc dọn sạch toàn bộ giỏ hàng.<br>• Tự động tính toán tổng tiền thanh toán theo thời gian thực. |
| 👥 **Người dùng** | • Xem thông tin tài khoản hiện tại.<br>• Chỉnh sửa thông tin cá nhân.<br>• Quản trị danh sách người dùng (Thêm người dùng mới, Sửa, Xóa - bảo vệ không cho xóa tài khoản Admin).<br>• Đăng xuất an toàn. |

---

## 3. Cấu trúc Tab Bar ở đáy màn hình (Bottom Navigation)

Đã tối giản, bỏ phần danh mục theo yêu cầu thiết kế:
1. 🏠 **Trang chủ:** Header đỏ TupeShop, ô tìm kiếm sản phẩm, tiêu đề "Sản phẩm nổi bật", nút thêm nhanh `+ Thêm`, lưới sản phẩm 2 cột.
2. 🛒 **Giỏ hàng:** Xem các món hàng đã thêm, cập nhật số lượng, tổng tiền.
3. 👤 **Tài khoản:** Xem hồ sơ cá nhân, nút đổi thông tin, danh sách quản lý người dùng và nút Đăng xuất.

---

## 4. Thư viện sử dụng (Dependencies)

- **Cơ sở dữ liệu:** `androidx.room:room-runtime:2.6.1` (xử lý biên dịch qua `annotationProcessor` chuẩn Java).
- **Giao diện:** `androidx.appcompat:1.6.1`, `com.google.android.material:1.11.0`, `androidx.cardview:1.0.0`, `androidx.recyclerview:1.3.2`.
- **Tải ảnh:** `com.github.bumptech.glide:4.16.0` (được tối ưu kích thước và bộ nhớ cache).

---

## 5. Hướng dẫn chạy và gỡ lỗi

1. **Mở dự án:** Mở thư mục `D:\BTL_Android\PTUDDD\cellphonesclone` trong Android Studio.
2. **Đồng bộ:** Nhấn **File ➔ Sync Project with Gradle Files**.
3. **Chạy ứng dụng:** Chọn thiết bị (máy ảo hoặc điện thoại thật) ➔ Nhấn nút **Run (▶)** hoặc phím tắt `Shift + F10`.
4. **Mẹo chạy mượt:** Khuyên dùng máy ảo **API 30 (Android 11)** hoặc **API 28 (Android 9)**, hoặc chạy trực tiếp trên điện thoại thật để máy tính không bị quá tải.
