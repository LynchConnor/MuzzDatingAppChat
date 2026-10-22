package com.muzz.datingappchat.shared.database

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.muzz.datingappchat.shared.converters.LocalDateTimeConverters
import com.muzz.datingappchat.shared.dao.MessagesDao
import com.muzz.datingappchat.shared.entities.Message

@Database(entities = [Message::class], version = 1)
@ColumnTypeConverters(LocalDateTimeConverters::class)
abstract class MuzzDatingDatabase: RoomDatabase() {
    abstract fun messagesDao(): MessagesDao

    companion object {

        /*
        SINGLETON APPROACH SO I CAN ACCESS MUZZDATINGDATABASE ANYWHERE WITHIN THE APP, SO LONG AS I HAVE ACCESS TO A CONTEXT. INIT ONCE AND SHARED.
         */

        @Volatile
        var shared: MuzzDatingDatabase? = null

        fun getDatabase(context: Context): MuzzDatingDatabase {
            return shared ?: Room.databaseBuilder(
                context,
                MuzzDatingDatabase::class.java,
                "muzz.db"
            ).fallbackToDestructiveMigration().build().also { shared = it }
        }
    }
}