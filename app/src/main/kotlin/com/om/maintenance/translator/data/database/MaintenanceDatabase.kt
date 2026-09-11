package com.om.maintenance.translator.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.om.maintenance.translator.data.dao.MaintenanceTermDao
import com.om.maintenance.translator.data.entity.MaintenanceTerm

@Database(entities = [MaintenanceTerm::class], version = 1, exportSchema = false)
abstract class MaintenanceDatabase : RoomDatabase() {
    abstract fun maintenanceTermDao(): MaintenanceTermDao

    companion object {
        @Volatile
        private var INSTANCE: MaintenanceDatabase? = null

        fun getDatabase(context: Context): MaintenanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaintenanceDatabase::class.java,
                    "maintenance_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
