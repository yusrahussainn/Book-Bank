package com.example.try12.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.try12.data.local.dao.BookDao
import com.example.try12.data.local.entities.Book

@Database(version = 1, entities = [Book::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun bookDao() : BookDao
    companion object{
        @Volatile

        private var INSTANCE : AppDatabase? = null

                fun getDatabase(context: Context) : AppDatabase{
                    return INSTANCE?: synchronized(this){
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "BookDatabase"
                        ).build()
                        INSTANCE = instance
                        return instance
                    }
                }
    }
}