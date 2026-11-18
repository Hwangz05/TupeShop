package com.example.tupeshop.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tupeshop.model.CartItem;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<CartItem> getCartByUserId(int userId);

    @Query("SELECT * FROM cart WHERE userId = :userId AND productId = :productId LIMIT 1")
    CartItem getCartItem(int userId, int productId);

    @Query("DELETE FROM cart WHERE userId = :userId")
    void clearCart(int userId);

    @Query("SELECT COUNT(*) FROM cart WHERE userId = :userId")
    int getCartCount(int userId);
}
