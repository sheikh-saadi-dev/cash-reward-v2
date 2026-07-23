package com.example.data.model

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val emailOrPhone: String = "",
    val authType: String = "PHONE", // "GOOGLE" or "PHONE"
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
