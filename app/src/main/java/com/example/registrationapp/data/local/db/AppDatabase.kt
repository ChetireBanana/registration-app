package com.example.registrationapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.registrationapp.data.local.db.dao.UserDao
import com.example.registrationapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}