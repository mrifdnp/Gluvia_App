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
    @SerialName("created_at")
    val createdAt: String? = null // Supabase timestamp
)