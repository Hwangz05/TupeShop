package com.example.tupeshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tupeshop.R;
import com.example.tupeshop.database.AppDatabase;
import com.example.tupeshop.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private AppDatabase db;
    private Context context;
    private Runnable onCartChanged;

    public CartAdapter(List<CartItem> cartItems, AppDatabase db, Context context, Runnable onCartChanged) {
        this.cartItems = cartItems;
        this.db = db;
        this.context = context;
        this.onCartChanged = onCartChanged;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        NumberFormat fmt = NumberFormat.getInstance(new Locale("vi", "VN"));

        holder.tvName.setText(item.getProductName());
        holder.tvPrice.setText(fmt.format((long) item.getProductPrice()) + " đ");
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        if (item.getProductImageUrl() != null && !item.getProductImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getProductImageUrl())
                    .override(150, 150)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() <= 1) {
                confirmRemove(item, position);
            } else {
                item.setQuantity(item.getQuantity() - 1);
                db.cartDao().update(item);
                holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
                if (onCartChanged != null) onCartChanged.run();
            }
        });

        // Tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            db.cartDao().update(item);
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            if (onCartChanged != null) onCartChanged.run();
        });

        // Xóa sản phẩm khỏi giỏ
        holder.btnRemove.setOnClickListener(v -> confirmRemove(item, position));
    }

    private void confirmRemove(CartItem item, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa khỏi giỏ hàng")
                .setMessage("Xóa \"" + item.getProductName() + "\" khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (d, w) -> {
                    db.cartDao().delete(item);
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartItems.size());
                    if (onCartChanged != null) onCartChanged.run();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public int getItemCount() { return cartItems.size(); }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        Button btnDecrease, btnIncrease, btnRemove;

        CartViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivCartProductImage);
            tvName = itemView.findViewById(R.id.tvCartProductName);
            tvPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
