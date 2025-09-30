package com.mrifdnp.gluvia.data

import com.mrifdnp.gluvia.R // Pastikan R diimpor

data class Hospital(
    val name: String,
    val phone: String,
    val address: String,
    val imageResId: Int // Resource ID untuk gambar rumah sakit
)

val badungHospitals = listOf(
    Hospital("RSUD Mangusada", "0361-92257", "Jl. Raya Kapal, Mengwi", R.drawable.care),
    Hospital("RS Kasih Ibu Kedonganan", "0361-703290", "Jl. Uluwatu No. 68", R.drawable.care),
    Hospital("RS BaliMed Denpasar", "0361-484742", "Jl. Mahendradatta No. 57", R.drawable.care)
)

val denpasarHospitals = listOf(
    Hospital("RSUP Prof. Ngoerah", "0361-240797", "Jl. Diponegoro", R.drawable.care),
    Hospital("RSUD Wangaya", "0361-222141", "Jl. Kartini No.133", R.drawable.care),
    Hospital("RS Prima Medika", "0361-246473", "Jl. Pulau Serangan", R.drawable.care)
)

val gianyarHospitals = listOf(
    Hospital("RSU Ganesha", "0361-4710059", "Jl.Raya Celuk Sukawati", R.drawable.care),
    Hospital("RSU Ari Canti", "0361-982223", "Jl.Raya Mas Ubud", R.drawable.care),
    Hospital("RSU Famili Husada", "0361-8493344", "Jl. Astina Timur Samplangan", R.drawable.care)
)

val tabananHospitals = listOf(
    Hospital("RSU Tabanan", "0361-811027", "Jl. Pahlawan No. 19", R.drawable.care),
    Hospital("RS Kasih Ibu Tabanan", "0361-3006060", "Jl. Flamboyan No. 9", R.drawable.care)
)

val bangliHospitals = listOf(
    Hospital("RSUD Bangli", "0366-91530", "Jl. Brigjen Ngurah Rai", R.drawable.care),
    Hospital("RSJ Provinsi Bali", "0366-91023", "Jl. Kusumayuda No. 1", R.drawable.care)
)

val karangasemHospitals = listOf(
    Hospital("RSUD Karangasem", "0363-21010", "Jl. Nenas Subagan", R.drawable.care),
    Hospital("RS Balimed Karangasem", "0363-430119", "Jl. Veteran No. 71", R.drawable.care)
)

val bulelengHospitals = listOf(
    Hospital("RSUD Buleleng", "0362-22046", "Jl. Ngurah Rai No. 30", R.drawable.care),
    Hospital("RS Paramartha", "0362-3301011", "Jl. Ahmad Yani No. 154", R.drawable.care)
)

val klungkungHospitals = listOf(
    Hospital("RSUD Klungkung", "0363-22122", "Jl. Flamboyan No. 40", R.drawable.care),
    Hospital("RS Balimed Klungkung", "0363-430118", "Jl. Puputan", R.drawable.care)
)

val jembranaHospitals = listOf(
    Hospital("RSUD Negara", "0365-41006", "Jl. Wijaya Kusuma No. 17", R.drawable.care),
    Hospital("RS Bali Royal", "0365-42211", "Jl. Raya Denpasar-Gilimanuk", R.drawable.care)
)

/**
 * Fungsi untuk mendapatkan daftar rumah sakit berdasarkan nama kabupaten/kota.
 */
fun getHospitalsForCounty(county: String): List<Hospital> {
    return when (county.uppercase()) {
        "BADUNG" -> badungHospitals
        "DENPASAR" -> denpasarHospitals
        "GIANYAR" -> gianyarHospitals
        "TABANAN" -> tabananHospitals
        "BANGLI" -> bangliHospitals
        "KARANGASEM" -> karangasemHospitals
        "BULELENG" -> bulelengHospitals
        "KLUNGKUNG" -> klungkungHospitals
        "JEMBRANA" -> jembranaHospitals
        else -> emptyList()
    }
}