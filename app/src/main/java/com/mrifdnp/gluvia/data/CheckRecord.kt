// File: com.mrifdnp.gluvia.data/CheckRecord.kt

package com.mrifdnp.gluvia.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.util.UUID // Import UUID jika Anda ingin tipe yang kuat

// 🔑 Data Class ASLI (Payload dari ViewModel)
@Serializable
data class CheckRecord(
    @SerialName("ph_value")
    val phValue: Float,
    @SerialName("glucose_mgdl")
    val glucoseMgdl: Float,
    @SerialName("status_result")
    val statusResult: String
)

// 🔑 Data Class Pembantu untuk Serialisasi ke Database
@Serializable
data class CheckRecordPayload(
    @SerialName("user_id")
    val userId: String, // UUID dikirim sebagai String
    @SerialName("ph_value")
    val phValue: Float,
    @SerialName("glucose_mgdl")
    val glucoseMgdl: Float,
    @SerialName("status_result")
    val statusResult: String
)