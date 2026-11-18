package com.example.tupeshop.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tupeshop.R;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.CartItem;
import com.example.tupeshop.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private AppDatabase db;
    private SharedPreferences prefs;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences("user_session", MODE_PRIVATE);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) { finish(); return; }

        product = db.productDao().getProductById(productId);
        if (product == null) { finish(); return; }

        displayProduct();
        setupAddToCart();
    }

    private void displayProduct() {
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        ImageView ivImage = findViewById(R.id.ivDetailImage);

        tvName.setText(product.getName());
        tvCategory.setText(product.getCategory().isEmpty() ? "Điện thoại" : product.getCategory());
        tvDescription.setText(product.getDescription().isEmpty() ? "Chưa có mô tả" : product.getDescription());

        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(fmt.format((long) product.getPrice()) + " đ");

        // Load ảnh nếu có URL
        if (!product.getImageUrl().isEmpty()) {
            try {
                // Nếu có Glide/Picasso thì dùng ở đây
                // Glide.with(this).load(product.getImageUrl()).into(ivImage);
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            } catch (Exception e) {
                ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        setTitle(product.getName());
    }

    private void setupAddToCart() {
        Button btnAddToCart = findViewById(R.id.btnAddToCartDetail);
        btnAddToCart.setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        int userId = prefs.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        CartItem existing = db.cartDao().getCartItem(userId, product.getId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
            db.cartDao().update(existing);
            Toast.makeText(this, "Đã tăng số lượng trong giỏ hàng", Toast.LENGTH_SHORT).show();
        } else {
            CartItem item = new CartItem(userId, product.getId(), product.getName(),
                    product.getPrice(), product.getImageUrl(), 1);
            db.cartDao().insert(item);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}
