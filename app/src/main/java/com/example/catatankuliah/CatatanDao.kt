package com.example.catatankuliah


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// DAO: kumpulan perintah untuk membaca dan mengubah tabel.
@Dao
interface CatatanDao {
    @Query("SELECT * FROM catatan ORDER BY id DESC")
    fun semua(): Flow<List<Catatan>>

    @Insert
    suspend fun tambah(item: Catatan)

    @Update
    suspend fun ubah(item: Catatan)

    @Delete
    suspend fun hapus(item: Catatan)
}