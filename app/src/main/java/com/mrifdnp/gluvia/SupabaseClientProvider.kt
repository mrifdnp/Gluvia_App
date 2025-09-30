package com.mrifdnp.gluvia

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


object SupabaseClientProvider {

    private const val SUPABASE_URL = "https://ubfiogonayixfrvdsvjs.supabase.co"       /*ENTER SUPABASE URL HERE*/
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InViZmlvZ29uYXlpeGZydmRzdmpzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg5MDk1NDQsImV4cCI6MjA3NDQ4NTU0NH0._wgb3NlJO-3gl1PkW-L7OEzql2QxqbECGBswQqov_aY"  /*ENTER SUPABASE KEY HERE*/
    private const val TAG = "SupabaseClient"

    val client: SupabaseClient by lazy {
        val createdClient = createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Auth) { // 🔑 Gunakan Auth
                autoLoadFromStorage = true
                autoSaveToStorage = true
                alwaysAutoRefresh = true
            }
            install(Postgrest)
            install(Storage)
        }
        Log.i(TAG, "🟢 Supabase Client TERHUBUNG dan modul Auth/Postgrest terinstal.")

        createdClient

    }
}
