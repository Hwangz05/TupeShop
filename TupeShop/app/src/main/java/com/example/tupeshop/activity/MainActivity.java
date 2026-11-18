package com.example.tupeshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tupeshop.R;
import com.example.tupeshop.adapter.CartAdapter;
import com.example.tupeshop.adapter.ProductAdapter;
import com.example.tupeshop.adapter.UserAdapter;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.CartItem;
import com.example.tupeshop.model.Product;
import com.example.tupeshop.model.User;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private SharedPreferences prefs;
    private int currentUserId = -1;

    // Panels
    private LinearLayout panelProducts;
    private LinearLayout panelCart;
    private androidx.core.widget.NestedScrollView panelAccount;

    // Bottom Tab Items
    private LinearLayout tabHome, tabCart, tabAccount;
    private ImageView ivTabHome, ivTabCart, ivTabAccount;
    private TextView tvTabHome, tvTabCart, tvTabAccount;

    // Products Tab
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;

    // Cart Tab
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnClearCart;

    // Account Tab
    private TextView tvAccountName, tvAccountEmail;
    private RecyclerView rvUsers;
    private UserAdapter userAdapter;

    private final int COLOR_ACTIVE = 0xFFE53935;   // Đỏ nổi bật
    private final int COLOR_INACTIVE = 0xFF757575; // Xám

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUserId = prefs.getInt("user_id", -1);

        // Kiểm tra đăng nhập
        if (currentUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Tạo sẵn 2 sản phẩm mẫu ban đầu nếu danh sách đang trống
        initSampleProducts();

        initViews();
        setupBottomTabs();
        setupSearch();
        loadProducts();
    }

    /**
     * Khởi tạo sẵn 2 sản phẩm mẫu như trong thiết kế ban đầu
     */
    private void initSampleProducts() {
        if (db.productDao().searchProducts("iPhone 15 Pro Max").isEmpty()) {
            Product p1 = new Product(
                    "iPhone 15 Pro Max 256GB",
                    29990000,
                    "https://cdn.tgdd.vn/Products/Images/42/305658/iphone-15-pro-max-blue-thumbnew-600x600.jpg",
                    "Màn hình Super Retina XDR OLED 6.7 inch, Chip Apple A17 Pro mạnh mẽ, camera 48MP zoom quang 5x, khung titan sang trọng.",
                    "Điện thoại"
            );
            db.productDao().insert(p1);
        }
        if (db.productDao().searchProducts("Samsung Galaxy S24").isEmpty()) {
            Product p2 = new Product(
                    "Samsung Galaxy S24 Ultra",
                    26990000,
                    "https://cdn.tgdd.vn/Products/Images/42/319665/samsung-galaxy-s24-ultra-grey-thumb-600x600.jpg",
                    "Màn hình Dynamic AMOLED 2X 6.8 inch, Chip Snapdragon 8 Gen 3 for Galaxy, bút S-Pen tích hợp, tính năng Galaxy AI thông minh.",
                    "Điện thoại"
            );
            db.productDao().insert(p2);
        }
    }

    private void initViews() {
        // Panels
        panelProducts = findViewById(R.id.panelProducts);
        panelCart = findViewById(R.id.panelCart);
        panelAccount = findViewById(R.id.panelAccount);

        // Bottom Tab items
        tabHome = findViewById(R.id.tabHome);
        tabCart = findViewById(R.id.tabCart);
        tabAccount = findViewById(R.id.tabAccount);

        ivTabHome = findViewById(R.id.ivTabHome);
        ivTabCart = findViewById(R.id.ivTabCart);
        ivTabAccount = findViewById(R.id.ivTabAccount);

        tvTabHome = findViewById(R.id.tvTabHome);
        tvTabCart = findViewById(R.id.tvTabCart);
        tvTabAccount = findViewById(R.id.tvTabAccount);

        // Tab Products: dùng GridLayout 2 cột đẹp mắt
        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, ProductFormActivity.class));
        });

        // Tab Cart
        rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnClearCart = findViewById(R.id.btnClearCart);

        btnClearCart.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa giỏ hàng")
                    .setMessage("Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?")
                    .setPositiveButton("Xóa tất cả", (d, w) -> {
                        db.cartDao().clearCart(currentUserId);
                        loadCart();
                        Toast.makeText(this, "Đã dọn sạch giỏ hàng", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Tab Account
        tvAccountName = findViewById(R.id.tvAccountName);
        tvAccountEmail = findViewById(R.id.tvAccountEmail);
        rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        Button btnEditAccount = findViewById(R.id.btnEditAccount);
        btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserFormActivity.class);
            intent.putExtra("user_id", currentUserId);
            startActivity(intent);
        });

        Button btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(v -> {
            startActivity(new Intent(this, UserFormActivity.class));
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (d, w) -> {
                        prefs.edit().clear().apply();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void setupBottomTabs() {
        tabHome.setOnClickListener(v -> selectTab(0));
        tabCart.setOnClickListener(v -> selectTab(1));
        tabAccount.setOnClickListener(v -> selectTab(2));
    }

    /**
     * Chuyển đổi giữa 3 tab: 0=Trang chủ, 1=Giỏ hàng, 2=Tài khoản
     */
    private void selectTab(int index) {
        // Ẩn/hiện nội dung
        panelProducts.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        panelCart.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        panelAccount.setVisibility(index == 2 ? View.VISIBLE : View.GONE);

        // Đổi màu Tab 1: Trang chủ
        ImageViewCompat.setImageTintList(ivTabHome, ColorStateList.valueOf(index == 0 ? COLOR_ACTIVE : COLOR_INACTIVE));
        tvTabHome.setTextColor(index == 0 ? COLOR_ACTIVE : COLOR_INACTIVE);

        // Đổi màu Tab 2: Giỏ hàng
        ImageViewCompat.setImageTintList(ivTabCart, ColorStateList.valueOf(index == 1 ? COLOR_ACTIVE : COLOR_INACTIVE));
        tvTabCart.setTextColor(index == 1 ? COLOR_ACTIVE : COLOR_INACTIVE);

        // Đổi màu Tab 3: Tài khoản
        ImageViewCompat.setImageTintList(ivTabAccount, ColorStateList.valueOf(index == 2 ? COLOR_ACTIVE : COLOR_INACTIVE));
        tvTabAccount.setTextColor(index == 2 ? COLOR_ACTIVE : COLOR_INACTIVE);

        if (index == 0) {
            loadProducts();
        } else if (index == 1) {
            loadCart();
        } else if (index == 2) {
            loadAccountInfo();
        }
    }

    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                List<Product> results = keyword.isEmpty()
                        ? db.productDao().getAllProducts()
                        : db.productDao().searchProducts(keyword);
                if (productAdapter != null) {
                    productAdapter.setProducts(results);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    public void loadProducts() {
        List<Product> products = db.productDao().getAllProducts();
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(products, this, db, currentUserId);
            rvProducts.setAdapter(productAdapter);
        } else {
            productAdapter.setProducts(products);
        }
    }

    public void loadCart() {
        List<CartItem> cartItems = db.cartDao().getCartByUserId(currentUserId);
        cartAdapter = new CartAdapter(cartItems, db, this, this::loadCart);
        rvCart.setAdapter(cartAdapter);

        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(fmt.format((long) total) + " đ");
    }

    private void loadAccountInfo() {
        User user = db.userDao().getUserById(currentUserId);
        if (user != null) {
            tvAccountName.setText(user.getFullName());
            tvAccountEmail.setText(user.getEmail() != null && !user.getEmail().isEmpty()
                    ? user.getEmail()
                    : "@" + user.getUsername());
        }

        // Tải danh sách người dùng cho mục Quản lý người dùng
        List<User> users = db.userDao().getAllUsers();
        userAdapter = new UserAdapter(users, this, db);
        rvUsers.setAdapter(userAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
        if (panelCart.getVisibility() == View.VISIBLE) {
            loadCart();
        } else if (panelAccount.getVisibility() == View.VISIBLE) {
            loadAccountInfo();
        }
    }
}
