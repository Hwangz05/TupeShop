package com.example.tupeshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tupeshop.R;
import com.example.tupeshop.activity.UserFormActivity;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private Context context;
    private AppDatabase db;

    public UserAdapter(List<User> users, Context context, AppDatabase db) {
        this.users = users;
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = users.get(position);

        holder.tvFullName.setText(u.getFullName());
        holder.tvUsername.setText("@" + u.getUsername() + (u.isAdmin() ? " (Admin)" : ""));
        holder.tvEmail.setText(u.getEmail().isEmpty() ? u.getPhone() : u.getEmail());

        // Ẩn nút XÓA với tài khoản Admin
        if (u.isAdmin()) {
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        // Sửa
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserFormActivity.class);
            intent.putExtra("user_id", u.getId());
            context.startActivity(intent);
        });

        // Xóa (chỉ với tài khoản không phải admin)
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa người dùng")
                    .setMessage("Bạn có chắc muốn xóa \"" + u.getFullName() + "\"?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        db.userDao().delete(u);
                        users.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, users.size());
                        Toast.makeText(context, "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return users.size(); }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullName, tvUsername, tvEmail;
        Button btnEdit, btnDelete;

        UserViewHolder(View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tvUserFullName);
            tvUsername = itemView.findViewById(R.id.tvUserUsername);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            btnEdit = itemView.findViewById(R.id.btnEditUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
