package com.example.catatankuliah


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Repository: perantara antara DAO dan ViewModel (UI tidak memanggil DAO langsung).
class CatatanRepository(private val dao: CatatanDao) {
    val semua: Flow<List<Catatan>> = dao.semua()

    suspend fun tambah(judul: String, isi: String) =
        dao.tambah(Catatan(judul = judul, isi = isi))

    suspend fun ubah(catatan: Catatan) = dao.ubah(catatan)

    suspend fun hapus(catatan: Catatan) = dao.hapus(catatan)
}

// ViewModel: menjalankan aksi di coroutine dan mengirim daftar ke UI sebagai StateFlow.
class CatatanViewModel(private val repo: CatatanRepository) : ViewModel() {
    val data: StateFlow<List<Catatan>> = repo.semua.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun tambah(judul: String, isi: String) {
        viewModelScope.launch { repo.tambah(judul, isi) }
    }

    fun ubah(catatan: Catatan) {
        viewModelScope.launch { repo.ubah(catatan) }
    }

    fun hapus(catatan: Catatan) {
        viewModelScope.launch { repo.hapus(catatan) }
    }
}