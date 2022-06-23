package com.yuoyama12.decidepickingorderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Group::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class GroupDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao

    companion object {

        @Volatile private var instance: GroupDatabase? = null

        fun getInstance(context: Context): GroupDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        }

        private fun buildDatabase(context: Context): GroupDatabase {
            return Room.databaseBuilder(
                context,
                GroupDatabase::class.java,
                "group.db")
                .build()
        }
    }
}