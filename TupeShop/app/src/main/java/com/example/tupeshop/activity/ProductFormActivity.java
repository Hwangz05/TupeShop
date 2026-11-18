package com.example.tupeshop.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tupeshop.R;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.Product;

public class ProductFormActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etName, etPrice, etCategory, etImageUrl, etDescription;
    private Button btnSave;
    private TextView tvTitle;

    private int productId = -1; // -1 = thêm mới, >0 = sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        db = AppDatabase.getInstance(this);

        tvTitle = findViewById(R.id.tvFormTitle);
        etName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etProductPrice);
        etCategory = findViewById(R.id.etProductCategory);
        etImageUrl = findViewById(R.id.etProductImageUrl);
        etDescription = findViewById(R.id.etProductDescription);
        btnSave = findViewById(R.id.btnSaveProduct);

        // Nhận productId nếu là chế độ sửa
        productId = getIntent().getIntExtra("product_id", -1);

        if (productId != -1) {
            tvTitle.setText("Chỉnh sửa sản phẩm");
            loadProduct(productId);
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void loadProduct(int id) {
        Product p = db.productDao().getProductById(id);
        if (p != null) {
            etName.setText(p.getName());
            etPrice.setText(String.valueOf(p.getPrice()));
            etCategory.setText(p.getCategory());
            etImageUrl.setText(p.getImageUrl());
            etDescription.setText(p.getDescription());
        }
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productId == -1) {
            // Thêm mới
            Product p = new Product(name, price, imageUrl, description, category);
            db.productDao().insert(p);
            Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
        } else {
            // Cập nhật
            Product p = db.productDao().getProductById(productId);
            if (p != null) {
                p.setName(name);
                p.setPrice(price);
                p.setCategory(category);
                p.setImageUrl(imageUrl);
                p.setDescription(description);
                db.productDao().update(p);
                Toast.makeText(this, "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
