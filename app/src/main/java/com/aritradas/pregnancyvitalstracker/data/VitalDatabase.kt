package com.aritradas.pregnancyvitalstracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [VitalEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VitalDatabase : RoomDatabase() {
    abstract fun vitalEntryDao(): VitalEntryDao

    companion object {
        @Volatile
        private var INSTANCE: VitalDatabase? = null

        fun getDatabase(context: Context): VitalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalDatabase::class.java,
                    "vital_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}