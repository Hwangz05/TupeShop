package com.example.tupeshop.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tupeshop.model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products ORDER BY id DESC")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :id")
    Product getProductById(int id);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%'")
    List<Product> searchProducts(String keyword);

    @Query("SELECT * FROM products WHERE category = :category")
    List<Product> getProductsByCategory(String category);
}
