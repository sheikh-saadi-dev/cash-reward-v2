package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withdraw_requests")
data class WithdrawRequest(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val verificationStatus: String, // "Verified" or "Unverified"
    val method: String, // "bKash" or "Nagad"
    val accountNumber: String,
    val pointsUsed: Int,
    val amountBdt: Double,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending", // "Pending", "Approved", "Rejected"
    val syncedToSheet: Boolean = false
)
