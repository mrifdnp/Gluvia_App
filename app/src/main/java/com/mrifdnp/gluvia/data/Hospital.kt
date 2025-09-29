package com.mrifdnp.gluvia.data

import com.mrifdnp.gluvia.R


// File: com.mrifdnp.gluvia.data/Hospital.kt (atau di bagian atas file ini untuk demo)

data class Hospital(
    val name: String,
    val phone: String,
    val address: String,
    val imageResId: Int // Resource ID untuk gambar rumah sakit
)

val gianyarHospitals = listOf(
    Hospital("RSU Ganesha", "0361-4710059", "Jl.Raya Celuk Sukawati", R.drawable.sakuchang),
    Hospital("RSU Ari Canti", "0361-982223", "Jl.Raya Mas Ubud", R.drawable.sakuchang),
    Hospital("RSU Famili Husada", "0361-8493344", "Jl. Astina Timur Samplangan",R.drawable.sakuchang)
)
// Catatan: Anda harus mengganti R.drawable.placeholder dengan resource ID gambar asli Anda.