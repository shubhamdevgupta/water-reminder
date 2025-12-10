package com.shubhamdev.waterreminder.data.local

import android.content.Context
import androidx.room.Room

object DatabaseModule {
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
}

