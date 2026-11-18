package com.example.tupeshop.activity;

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

public class UserFormActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText etUsername, etPassword, etFullName, etPhone, etEmail, etAddress;
    private TextView tvTitle;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        db = AppDatabase.getInstance(this);

        tvTitle = findViewById(R.id.tvUserFormTitle);
        etUsername = findViewById(R.id.etUserUsername);
        etPassword = findViewById(R.id.etUserPassword);
        etFullName = findViewById(R.id.etUserFullName);
        etPhone = findViewById(R.id.etUserPhone);
        etEmail = findViewById(R.id.etUserEmail);
        etAddress = findViewById(R.id.etUserAddress);

        Button btnSave = findViewById(R.id.btnSaveUser);

        // Nhận userId nếu là chế độ sửa
        userId = getIntent().getIntExtra("user_id", -1);
        if (userId != -1) {
            tvTitle.setText("Chỉnh sửa người dùng");
            loadUser(userId);
        }

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void loadUser(int id) {
        User u = db.userDao().getUserById(id);
        if (u != null) {
            etUsername.setText(u.getUsername());
            etPassword.setText(u.getPassword());
            etFullName.setText(u.getFullName());
            etPhone.setText(u.getPhone());
            etEmail.setText(u.getEmail());
            etAddress.setText(u.getAddress());
            etUsername.setEnabled(false); // Không cho sửa username
        }
    }

    private void saveUser() {
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

        if (userId == -1) {
            // Kiểm tra username tồn tại
            User existing = db.userDao().getUserByUsername(username);
            if (existing != null) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }
            User newUser = new User(username, password, fullName, phone, email, address, false);
            db.userDao().insert(newUser);
            Toast.makeText(this, "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
        } else {
            User u = db.userDao().getUserById(userId);
            if (u != null) {
                u.setPassword(password);
                u.setFullName(fullName);
                u.setPhone(phone);
                u.setEmail(email);
                u.setAddress(address);
                db.userDao().update(u);

                // Cập nhật session nếu đang sửa chính mình
                SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                if (prefs.getInt("user_id", -1) == userId) {
                    prefs.edit().putString("username", u.getUsername()).apply();
                }
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}
