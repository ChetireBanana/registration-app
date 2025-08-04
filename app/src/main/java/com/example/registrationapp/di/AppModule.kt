package com.example.registrationapp.di

import android.content.Context
import androidx.room.Room
import com.example.registrationapp.data.local.db.AppDatabase
import com.example.registrationapp.data.local.db.dao.UserDao
import com.example.registrationapp.data.repository.UserRepositoryImpl
import com.example.registrationapp.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    fun providesUserRepository(userDao: UserDao): UserRepository = UserRepositoryImpl(userDao)
}