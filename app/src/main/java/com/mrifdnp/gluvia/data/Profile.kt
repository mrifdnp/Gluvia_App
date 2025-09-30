package com.mrifdnp.gluvia.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id") // UUID dari auth.users
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("description")
    val description: String?,
    // 🔑 TAMBAHAN: Kolom untuk URL Gambar Profil
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null // Supabase timestamp
)

@Serializable
data class ProfileUpdate(
    @SerialName("username")
    val username: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)