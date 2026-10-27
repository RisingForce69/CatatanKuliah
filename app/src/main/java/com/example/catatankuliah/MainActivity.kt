package com.example.catatankuliah // ganti sesuai package proyekmu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = CatatanDatabase.getInstance(applicationContext).dao()
        val repo = CatatanRepository(dao)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val vm: CatatanViewModel = viewModel(
                        factory = viewModelFactory {
                            initializer { CatatanViewModel(repo) }
                        }
                    )
                    LayarCatatan(vm)
                }
            }
        }
    }
}

@Composable
fun LayarCatatan(vm: CatatanViewModel) {
    val daftar by vm.data.collectAsState()

    var judul by rememberSaveable { mutableStateOf("") }
    var isi by rememberSaveable { mutableStateOf("") }
    // null = mode tambah, berisi id = mode ubah catatan tersebut
    var editId by rememberSaveable { mutableStateOf<Int?>(null) }

    val valid = judul.isNotBlank() && isi.isNotBlank()
    val sedangUbah = editId != null

    fun kosongkanForm() {
        judul = ""
        isi = ""
        editId = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Catatan Kuliah", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = judul,
            onValueChange = { judul = it },
            label = { Text("Judul") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = isi,
            onValueChange = { isi = it },
            label = { Text("Isi") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = valid,
                onClick = {
                    val id = editId
                    if (id == null) {
                        vm.tambah(judul.trim(), isi.trim())
                    } else {
                        vm.ubah(Catatan(id = id, judul = judul.trim(), isi = isi.trim()))
                    }
                    kosongkanForm()
                }
            ) {
                Text(if (sedangUbah) "Simpan Perubahan" else "Tambah")
            }
            if (sedangUbah) {
                TextButton(onClick = { kosongkanForm() }) { Text("Batal") }
            }
        }

        if (!valid) {
            Text(
                text = "Judul dan isi wajib diisi.",
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (daftar.isEmpty()) {
            Text("Belum ada catatan.")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(daftar, key = { it.id }) { catatan ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(catatan.judul, style = MaterialTheme.typography.titleMedium)
                        Text(catatan.isi)
                        Row {
                            TextButton(onClick = {
                                editId = catatan.id
                                judul = catatan.judul
                                isi = catatan.isi
                            }) { Text("Ubah") }
                            TextButton(onClick = {
                                if (editId == catatan.id) kosongkanForm()
                                vm.hapus(catatan)
                            }) { Text("Hapus") }
                        }
                    }
                }
            }
        }
    }
}