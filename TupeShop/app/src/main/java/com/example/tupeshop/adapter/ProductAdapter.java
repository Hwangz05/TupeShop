package com.example.tupeshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tupeshop.R;
import com.example.tupeshop.activity.ProductDetailActivity;
import com.example.tupeshop.activity.ProductFormActivity;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.CartItem;
import com.example.tupeshop.model.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;
    private AppDatabase db;
    private int userId;

    public ProductAdapter(List<Product> products, Context context, AppDatabase db, int userId) {
        this.products = products;
        this.context = context;
        this.db = db;
        this.userId = userId;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);

        holder.tvName.setText(p.getName());

        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(fmt.format((long) p.getPrice()) + "đ");

        // Load image với Glide được tối ưu kích thước để không lag/đơ máy
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(p.getImageUrl())
                    .override(250, 250)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Bấm vào card hoặc ảnh để xem chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", p.getId());
            context.startActivity(intent);
        });

        // Sửa
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductFormActivity.class);
            intent.putExtra("product_id", p.getId());
            context.startActivity(intent);
        });

        // Xóa
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa \"" + p.getName() + "\"?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        db.productDao().delete(p);
                        products.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, products.size());
                        Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Thêm vào giỏ hàng
        holder.btnAddToCart.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }
            CartItem existing = db.cartDao().getCartItem(userId, p.getId());
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + 1);
                db.cartDao().update(existing);
                Toast.makeText(context, "Đã tăng số lượng trong giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                CartItem item = new CartItem(userId, p.getId(), p.getName(),
                        p.getPrice(), p.getImageUrl(), 1);
                db.cartDao().insert(item);
                Toast.makeText(context, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return products.size(); }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;
        Button btnEdit, btnDelete, btnAddToCart;

        ProductViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
