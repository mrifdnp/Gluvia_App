package com.mrifdnp.gluvia

sealed class AuthResponse {
    object Loading : AuthResponse()
    object Success : AuthResponse()
    data class Error(val message: String?) : AuthResponse()
}