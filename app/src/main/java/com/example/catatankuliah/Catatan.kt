package com.example.catatankuliah


import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity: satu objek = satu baris pada tabel "catatan".
@Entity(tableName = "catatan")
data class Catatan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val isi: String
)