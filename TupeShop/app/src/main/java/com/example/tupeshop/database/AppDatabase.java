package com.example.tupeshop.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tupeshop.dao.CartDao;
import com.example.tupeshop.dao.ProductDao;
import com.example.tupeshop.dao.UserDao;
import com.example.tupeshop.model.CartItem;
import com.example.tupeshop.model.Product;
import com.example.tupeshop.model.User;

@Database(entities = {Product.class, User.class, CartItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ProductDao productDao();
    public abstract UserDao userDao();
    public abstract CartDao cartDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cellphones_db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Cho phép query trên main thread (đơn giản hóa)
                    .build();
        }
        return instance;
    }
}
