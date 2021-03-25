package com.droid.offlineStorage.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.droid.offlineStorage.model.ExResponse

/**
 * Created by Akhtar
 */
@Database(entities = [ExResponse::class], version = 1)
abstract class ExampleDatabase : RoomDatabase() {

    abstract val exampleDao: ExampleDao

    companion object {
        @Volatile
        private var INSTANCE: ExampleDatabase? = null
        fun getInstance(context: Context): ExampleDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExampleDatabase::class.java,
                        "example_database"
                    ).build()
                }
                return instance
            }
        }

    }
}