package com.example.catatankuliah


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Database: titik masuk Room yang menghubungkan Entity dan DAO.
@Database(entities = [Catatan::class], version = 1, exportSchema = false)
abstract class CatatanDatabase : RoomDatabase() {
    abstract fun dao(): CatatanDao

    companion object {
        @Volatile
        private var INSTANCE: CatatanDatabase? = null

        // Hanya satu instance database selama aplikasi hidup,
        // termasuk saat layar diputar dan Activity dibuat ulang.
        fun getInstance(context: Context): CatatanDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CatatanDatabase::class.java,
                    "catatan-kuliah.db"
                ).build().also { INSTANCE = it }
            }
    }
}