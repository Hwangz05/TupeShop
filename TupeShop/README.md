# 📱 TupeShop — Ứng dụng Bán Điện Thoại Android (Java)

Dự án xây dựng một **ứng dụng thương mại điện tử bán điện thoại trên Android** bằng **100% Java + XML Layouts**, sử dụng **Room Database (SQLite)** để lưu trữ dữ liệu cục bộ ngay trên thiết bị.

Hệ thống hỗ trợ:
- **Đăng nhập / Đăng ký & Phân quyền**: tài khoản quản trị viên mặc định (`admin` / `admin123`) và người dùng thường, lưu phiên bằng `SharedPreferences`
- **Trang chủ sản phẩm (2 cột)**: hiển thị danh sách sản phẩm nổi bật với ảnh tải qua **Glide**, giá định dạng VNĐ (`18.990.000đ`)
- **Tìm kiếm sản phẩm theo thời gian thực**: lọc nhanh theo tên sản phẩm ngay trên thanh tìm kiếm
- **Xem chi tiết sản phẩm**: màn hình chi tiết đầy đủ ảnh, danh mục, giá, mô tả và nút quay lại Trang chủ
- **Quản lý sản phẩm (CRUD)**: thêm, sửa, xóa sản phẩm trực tiếp trên ứng dụng
- **Giỏ hàng thông minh**: thêm vào giỏ, tăng/giảm số lượng (`+` / `−`), xóa từng món, xóa toàn bộ giỏ, tính tổng thanh toán tự động và xác nhận đặt hàng
- **Quản lý tài khoản & người dùng (CRUD)**: cập nhật thông tin cá nhân, thêm/sửa/xóa người dùng (tự động ẩn nút xóa với tài khoản Admin), đăng xuất

---

## 📂 Cấu trúc thư mục dự án

```
TupeShop/
├── app/
│   ├── build.gradle.kts                    # Cấu hình SDK (min 24, target/compile 34) & thư viện
│   └── src/main/
│       ├── AndroidManifest.xml             # Khai báo quyền INTERNET & 7 Activity
│       ├── java/com/example/tupeshop/
│       │   ├── activity/                   # Tầng giao diện & xử lý sự kiện (7 màn hình)
│       │   │   ├── LoginActivity.java      #   Đăng nhập + khởi tạo admin mặc định
│       │   │   ├── RegisterActivity.java   #   Đăng ký tài khoản mới
│       │   │   ├── MainActivity.java       #   Màn hình chính 3 tab (Trang chủ, Giỏ hàng, Tài khoản)
│       │   │   ├── ProductDetailActivity.java  # Chi tiết sản phẩm + header quay lại
│       │   │   ├── ProductFormActivity.java    # Form thêm / sửa sản phẩm
│       │   │   ├── UserFormActivity.java       # Form thêm / sửa người dùng
│       │   │   └── CartActivity.java       #   Activity giỏ hàng độc lập
│       │   ├── adapter/                    # Adapter cho RecyclerView
│       │   │   ├── ProductAdapter.java     #   Hiển thị lưới sản phẩm + nút giỏ/sửa/xóa
│       │   │   ├── CartAdapter.java        #   Hiển thị item giỏ hàng + tăng/giảm/xóa
│       │   │   └── UserAdapter.java        #   Hiển thị danh sách người dùng (ẩn xóa với Admin)
│       │   ├── dao/                        # Data Access Object (truy vấn SQLite qua Room)
│       │   │   ├── ProductDao.java         #   CRUD bảng products
│       │   │   ├── CartDao.java            #   CRUD bảng cart
│       │   │   └── UserDao.java            #   CRUD & xác thực bảng users
│       │   ├── database/
│       │   │   └── AppDatabase.java        #   Cấu hình RoomDatabase ("cellphones_db")
│       │   └── model/                      # Entity ánh xạ thành các bảng SQLite
│       │       ├── Product.java            #   Bảng products
│       │       ├── CartItem.java           #   Bảng cart
│       │       └── User.java               #   Bảng users
│       └── res/
│           ├── layout/                     # Giao diện XML (10 file)
│           │   ├── activity_login.xml
│           │   ├── activity_register.xml
│           │   ├── activity_main.xml       #   Header đỏ + 3 panel tab + Bottom Tab Bar
│           │   ├── activity_product_detail.xml
│           │   ├── activity_product_form.xml
│           │   ├── activity_user_form.xml
│           │   ├── activity_cart.xml
│           │   ├── item_product.xml        #   CardView sản phẩm 2 cột
│           │   ├── item_cart.xml           #   CardView sản phẩm trong giỏ hàng
│           │   └── item_user.xml           #   Item người dùng
│           └── drawable/                   # Vector icons & background bo tròn
│               ├── ic_home.xml, ic_cart.xml, ic_user.xml, ic_bell.xml, ic_search.xml
│               ├── ic_arrow_back.xml, ic_add.xml, ic_edit.xml, ic_delete.xml, ic_edit_person.xml
│               └── bg_search_bar.xml, bg_product_card.xml, bg_btn_cart.xml, bg_btn_edit.xml, bg_btn_delete.xml
├── gradle/                                 # Gradle Wrapper & Version Catalog
├── build.gradle.kts                        # Cấu hình Gradle cấp project
├── settings.gradle.kts
├── gradlew / gradlew.bat
└── README.md
```

---

## 🧠 Mục tiêu & Điểm nổi bật

- **100% Java thuần + XML Layout** — cấu trúc rõ ràng theo mô hình **Activity – Adapter – DAO – Entity**, dễ đọc và bảo trì
- **Lưu trữ offline bằng Room (SQLite)**: tự động tạo database `cellphones_db` ngay lần đầu chạy app, không cần cài đặt server hay database ngoài
- **Tự động nạp dữ liệu mẫu (Seed Data)**:
  - Tài khoản quản trị mặc định: `admin` / `admin123`
  - 4 sản phẩm mẫu sẵn có: *iPhone 15 Pro Max 256GB*, *Samsung Galaxy S24 Ultra*, *iPhone 14 Pro Max 128GB*, *iPhone 13 Pro Max 128GB*
- **Giao diện hiện đại tông đỏ `#E53935`**:
  - Thanh điều hướng dưới gồm **3 tab**: Trang chủ / Giỏ hàng / Tài khoản
  - Card sản phẩm bo tròn kèm các nút icon vector (`ImageButton`) trực quan
  - Tối ưu hiển thị ảnh sản phẩm từ Internet bằng **Glide** kèm disk cache & placeholder
- **Bảo vệ tài khoản Admin**: tự động ẩn nút Xóa đối với tài khoản quản trị viên

---

## ⚙️ Công nghệ sử dụng

| Công nghệ | Phiên bản | Mục đích sử dụng |
|---|---|---|
| **Java** | JDK 11+ | Ngôn ngữ lập trình chính của toàn bộ ứng dụng |
| **Android SDK** | Min 24 / Target 34 | Nền tảng phát triển ứng dụng Android |
| **Room Persistence** (`androidx.room`) | Room Runtime + Annotation Processor | ORM quản lý cơ sở dữ liệu SQLite cục bộ (`cellphones_db`) |
| **Glide** (`com.github.bumptech.glide`) | `4.16.0` | Tải, resize, cache và hiển thị ảnh sản phẩm từ URL |
| **Material Components** | `1.11.0` | Thành phần UI hiện đại (Button, Input, Dialog) |
| **RecyclerView & CardView** | `1.3.2` / `1.0.0` | Hiển thị danh sách lưới sản phẩm, giỏ hàng, người dùng |
| **AppCompat & ConstraintLayout** | `1.6.1` / `2.1.4` | Hỗ trợ tương thích và bố cục giao diện XML |

---

## ▶️ Hướng dẫn cài đặt & chạy dự án

### **Bước 1 — Yêu cầu môi trường**

- **Android Studio** (khuyến nghị bản Hedgehog / Iguana trở lên)
- **JDK 11** hoặc **JDK 17**
- **Android Emulator** (API 24+) hoặc điện thoại Android thật bật *USB Debugging*

### **Bước 2 — Mở dự án**

1. Mở **Android Studio** → chọn **Open** → trỏ tới thư mục `TupeShop/`
2. Chờ Android Studio thực hiện **Gradle Sync** để tải đầy đủ các thư viện (`Room`, `Glide`, `Material`, ...)

### **Bước 3 — Chạy ứng dụng**

Chọn thiết bị ảo (ví dụ *Small Phone API 31*) và nhấn nút **Run (▶)**, hoặc build APK trực tiếp bằng Gradle Wrapper:

```powershell
# Trên Windows (PowerShell)
.\gradlew assembleDebug --no-daemon

# Cài đặt APK lên máy ảo đang chạy
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

```bash
# Trên macOS / Linux
./gradlew assembleDebug --no-daemon
```

> 💡 **Tài khoản đăng nhập mặc định:**
> - **Tên đăng nhập:** `admin`
> - **Mật khẩu:** `admin123`
>
> Bạn cũng có thể bấm **Đăng ký** ngay trên màn hình đăng nhập để tạo tài khoản người dùng mới.

---

## 🗄️ Cơ sở dữ liệu (Room / SQLite)

Ứng dụng sử dụng **Room Database** với tên database là **`cellphones_db`** (`version = 1`), định nghĩa tại [AppDatabase.java](app/src/main/java/com/example/tupeshop/database/AppDatabase.java). Database được tự động khởi tạo trên thiết bị khi ứng dụng chạy lần đầu.

### Danh sách các bảng

| Bảng | Entity | Mô tả |
|---|---|---|
| `products` | `Product.java` | Lưu thông tin sản phẩm: `id`, `name`, `price`, `imageUrl`, `description`, `category` |
| `users` | `User.java` | Lưu tài khoản người dùng: `id`, `username`, `password`, `fullName`, `phone`, `email`, `address`, `isAdmin` |
| `cart` | `CartItem.java` | Lưu giỏ hàng theo người dùng: `id`, `userId`, `productId`, `productName`, `productPrice`, `productImageUrl`, `quantity` |

### Kiến trúc mã nguồn

Ứng dụng tổ chức theo kiến trúc phân tầng rõ ràng:

| Tầng | Package | Trách nhiệm |
|---|---|---|
| Giao diện (UI) | `activity/` + `res/layout/` | Hiển thị màn hình, bắt sự kiện người dùng, điều hướng |
| Kết nối dữ liệu | `adapter/` | Ánh xạ danh sách Entity lên `RecyclerView` (`ProductAdapter`, `CartAdapter`, `UserAdapter`) |
| Truy vấn (DAO) | `dao/` | Định nghĩa các câu lệnh `@Insert`, `@Update`, `@Delete`, `@Query` |
| Cơ sở dữ liệu | `database/` | Singleton `AppDatabase` quản lý kết nối Room |
| Mô hình dữ liệu | `model/` | Các lớp POJO/Entity đại diện cho bảng trong SQLite |

> 🔍 **Mẹo kiểm tra dữ liệu trực tiếp:** Trong Android Studio, mở tab **App Inspection → Database Inspector** khi app đang chạy để xem và truy vấn trực tiếp 3 bảng `products`, `users`, `cart`.

---

## 📱 Các màn hình & chức năng chính

| Màn hình / Tab | Chức năng |
|---|---|
| **Đăng nhập / Đăng ký** | Xác thực người dùng từ bảng `users`, lưu session bằng `SharedPreferences` |
| **Tab Trang chủ** | Tìm kiếm theo tên, xem lưới sản phẩm 2 cột, thêm sản phẩm mới (`+ Thêm`), sửa/xóa nhanh, thêm vào giỏ |
| **Chi tiết sản phẩm** | Xem ảnh lớn, danh mục, giá bán, mô tả chi tiết, thêm vào giỏ hàng, nút **← Trang chủ** trên header |
| **Tab Giỏ hàng** | Điều chỉnh số lượng (`+` / `−`), xóa từng sản phẩm, xóa toàn bộ giỏ, xem tổng tiền, nút **Đặt hàng** |
| **Tab Tài khoản** | Xem hồ sơ cá nhân, nút **Đổi thông tin**, quản lý danh sách người dùng (thêm/sửa/xóa), **Đăng xuất** |

---

## 📝 Ghi chú

- Ứng dụng cần quyền `INTERNET` (`uses-permission android:name="android.permission.INTERNET"`) trong `AndroidManifest.xml` để **Glide** tải ảnh sản phẩm từ URL.
- Cho phép `usesCleartextTraffic="true"` để hỗ trợ các đường dẫn ảnh HTTP nếu cần.
- Có thể mở rộng thêm các tính năng trong tương lai như: lịch sử đơn hàng (`orders`), lọc theo danh mục sản phẩm, thanh toán trực tuyến hoặc đồng bộ API backend.
