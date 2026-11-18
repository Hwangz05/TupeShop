package com.example.tupeshop.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tupeshop.R;
import com.example.tupeshop.adapter.CartAdapter;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private AppDatabase db;
    private CartAdapter cartAdapter;
    private RecyclerView rvCart;
    private TextView tvTotalPrice;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = AppDatabase.getInstance(this);
        userId = getIntent().getIntExtra("user_id", -1);

        setTitle("Giỏ hàng");

        rvCart = findViewById(R.id.rvCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        Button btnClearCart = findViewById(R.id.btnClearCart);

        rvCart.setLayoutManager(new LinearLayoutManager(this));

        btnClearCart.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa giỏ hàng")
                    .setMessage("Bạn có chắc muốn xóa tất cả sản phẩm trong giỏ hàng?")
                    .setPositiveButton("Xóa tất cả", (d, w) -> {
                        db.cartDao().clearCart(userId);
                        loadCart();
                        Toast.makeText(this, "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        loadCart();
    }

    public void loadCart() {
        List<CartItem> cartItems = db.cartDao().getCartByUserId(userId);
        cartAdapter = new CartAdapter(cartItems, db, this, this::loadCart);
        rvCart.setAdapter(cartAdapter);
        updateTotal(cartItems);
    }

    private void updateTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(fmt.format((long) total) + " đ");
    }
}
