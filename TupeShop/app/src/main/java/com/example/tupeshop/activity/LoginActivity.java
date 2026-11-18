package com.example.tupeshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tupeshop.R;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.User;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        initDefaultAdmin();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void initDefaultAdmin() {
        // Tạo tài khoản admin mặc định nếu chưa có
        User admin = db.userDao().getUserByUsername("admin");
        if (admin == null) {
            User defaultAdmin = new User("admin", "admin123", "Quản trị viên",
                    "0900000000", "admin@cellphones.vn", "Hà Nội", true);
            db.userDao().insert(defaultAdmin);
        }
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.userDao().login(username, password);
        if (user != null) {
            // Lưu session
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            prefs.edit()
                    .putInt("user_id", user.getId())
                    .putString("username", user.getUsername())
                    .putBoolean("is_admin", user.isAdmin())
                    .apply();

            Toast.makeText(this, "Chào mừng " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
        }
    }
}
