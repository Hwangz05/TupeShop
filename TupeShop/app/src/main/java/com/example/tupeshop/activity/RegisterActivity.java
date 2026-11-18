package com.example.tupeshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tupeshop.R;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.User;

public class RegisterActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etUsername, etPassword, etFullName, etPhone, etEmail, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDatabase.getInstance(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);

        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập các trường bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra username đã tồn tại
        User existing = db.userDao().getUserByUsername(username);
        if (existing != null) {
            Toast.makeText(this, "Tên đăng nhập đã được sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(username, password, fullName, phone, email, address, false);
        db.userDao().insert(newUser);

        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
