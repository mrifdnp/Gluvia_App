package com.mrifdnp.gluvia


import android.app.Application
import com.mrifdnp.gluvia.di.appModule
import io.github.jan.supabase.SupabaseClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GluvApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 🚀 INI ADALAH TITIK AWAL SEMUA DEPENDENSI
        startKoin {
            // Memberi Koin konteks aplikasi Android
            androidContext(this@GluvApp)

            // Memuat module DI Anda
            modules(appModule)

            // Opsional: Untuk melihat log Koin yang lebih detail
            // androidLogger(org.koin.core.logger.Level.INFO)
        }
        val supabaseClientInstance: SupabaseClient = get()

        // Opsional: Panggil client di sini untuk memastikan by lazy terpicu
        // Log.d("AppInit", "Klien Supabase diakses pertama kali...")
        // val client = get<SupabaseClient>()
    }
}