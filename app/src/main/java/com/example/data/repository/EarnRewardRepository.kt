package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.dao.WithdrawDao
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

class EarnRewardRepository(
    private val context: Context,
    private val withdrawDao: WithdrawDao
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("earn_reward_prefs", Context.MODE_PRIVATE)

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Configurable Constants
    val POINTS_PER_BDT = 1000
    val POINTS_PER_AD_VERIFIED = 20
    val POINTS_PER_AD_UNVERIFIED = 15
    val MAX_ADS_PER_DAY_VERIFIED = 15
    val MAX_ADS_PER_DAY_UNVERIFIED = 10
    val MIN_WITHDRAW_POINTS_VERIFIED = 50000 // 50 BDT
    val MIN_WITHDRAW_POINTS_UNVERIFIED = 75000 // 75 BDT

    // Default sample Apps Script URL (users can configure their own deployed Google Sheet Apps Script URL in app settings)
    val DEFAULT_SHEET_WEBHOOK_URL = "https://script.google.com/macros/s/AKfycbx_EARN_REWARD_WEBHOOK_SAMPLE/exec"

    private val _currentUser = MutableStateFlow<UserProfile?>(loadUserProfile())
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _totalPoints = MutableStateFlow(prefs.getInt("total_points", 1000)) // 1000 welcome bonus
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

    private val _adsWatchedToday = MutableStateFlow(getTodayAdCount())
    val adsWatchedToday: StateFlow<Int> = _adsWatchedToday.asStateFlow()

    private val _dailyStreak = MutableStateFlow(prefs.getInt("daily_streak", 1))
    val dailyStreak: StateFlow<Int> = _dailyStreak.asStateFlow()

    private val _lastClaimDate = MutableStateFlow(prefs.getString("last_claim_date", "") ?: "")
    val lastClaimDate: StateFlow<String> = _lastClaimDate.asStateFlow()

    private val _webhookUrl = MutableStateFlow(
        prefs.getString("webhook_url", DEFAULT_SHEET_WEBHOOK_URL) ?: DEFAULT_SHEET_WEBHOOK_URL
    )
    val webhookUrl: StateFlow<String> = _webhookUrl.asStateFlow()

    val allWithdrawRequests: Flow<List<WithdrawRequest>> = withdrawDao.getAllRequests()

    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private fun getTodayAdCount(): Int {
        val lastDate = prefs.getString("last_ad_date", "") ?: ""
        val today = getTodayString()
        return if (lastDate == today) {
            prefs.getInt("ads_watched_today", 0)
        } else {
            0
        }
    }

    private fun loadUserProfile(): UserProfile? {
        val id = prefs.getString("user_id", null) ?: return null
        return UserProfile(
            id = id,
            name = prefs.getString("user_name", "User") ?: "User",
            emailOrPhone = prefs.getString("user_contact", "") ?: "",
            authType = prefs.getString("auth_type", "PHONE") ?: "PHONE",
            isVerified = prefs.getBoolean("is_verified", false),
            createdAt = prefs.getLong("created_at", System.currentTimeMillis())
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("user_id", profile.id)
            putString("user_name", profile.name)
            putString("user_contact", profile.emailOrPhone)
            putString("auth_type", profile.authType)
            putBoolean("is_verified", profile.isVerified)
            putLong("created_at", profile.createdAt)
            apply()
        }
        _currentUser.value = profile
    }

    fun logout() {
        prefs.edit().apply {
            remove("user_id")
            remove("user_name")
            remove("user_contact")
            remove("auth_type")
            remove("is_verified")
            apply()
        }
        _currentUser.value = null
    }

    fun setWebhookUrl(url: String) {
        val cleanUrl = url.trim()
        prefs.edit().putString("webhook_url", cleanUrl).apply()
        _webhookUrl.value = cleanUrl
    }

    fun addPoints(points: Int) {
        val newBalance = _totalPoints.value + points
        prefs.edit().putInt("total_points", newBalance).apply()
        _totalPoints.value = newBalance
    }

    fun deductPoints(points: Int): Boolean {
        if (_totalPoints.value >= points) {
            val newBalance = _totalPoints.value - points
            prefs.edit().putInt("total_points", newBalance).apply()
            _totalPoints.value = newBalance
            return true
        }
        return false
    }

    fun recordVideoAdWatched(): Int {
        val isVerified = currentUser.value?.isVerified ?: false
        val pointsToEarn = if (isVerified) POINTS_PER_AD_VERIFIED else POINTS_PER_AD_UNVERIFIED
        val maxAds = if (isVerified) MAX_ADS_PER_DAY_VERIFIED else MAX_ADS_PER_DAY_UNVERIFIED

        val today = getTodayString()
        var currentCount = getTodayAdCount()

        if (currentCount < maxAds) {
            currentCount++
            prefs.edit().apply {
                putString("last_ad_date", today)
                putInt("ads_watched_today", currentCount)
                apply()
            }
            _adsWatchedToday.value = currentCount
            addPoints(pointsToEarn)
            return pointsToEarn
        }
        return 0
    }

    fun canClaimDailyReward(): Boolean {
        val today = getTodayString()
        return _lastClaimDate.value != today
    }

    fun getTodayDailyRewardAmount(): Int {
        val streak = _dailyStreak.value
        val dayNumber = ((streak - 1) % 7) + 1
        return 30 + (dayNumber * 10) // Day 1: 40, Day 2: 50 ... Day 7: 100
    }

    fun claimDailyReward(): Int {
        if (!canClaimDailyReward()) return 0

        val rewardAmount = getTodayDailyRewardAmount()
        val today = getTodayString()

        var streak = _dailyStreak.value
        if (streak >= 7) {
            streak = 1
        } else {
            streak++
        }

        prefs.edit().apply {
            putString("last_claim_date", today)
            putInt("daily_streak", streak)
            apply()
        }

        _lastClaimDate.value = today
        _dailyStreak.value = streak
        addPoints(rewardAmount)

        return rewardAmount
    }

    suspend fun submitWithdrawRequest(
        method: String,
        accountNumber: String,
        pointsToWithdraw: Int
    ): Pair<Boolean, String> {
        val user = _currentUser.value ?: return Pair(false, "User not logged in")
        val isVerified = user.isVerified

        val minPoints = if (isVerified) MIN_WITHDRAW_POINTS_VERIFIED else MIN_WITHDRAW_POINTS_UNVERIFIED
        if (pointsToWithdraw < minPoints) {
            return Pair(false, "Minimum withdraw limit is $minPoints points (${minPoints / POINTS_PER_BDT} BDT)")
        }

        if (_totalPoints.value < pointsToWithdraw) {
            return Pair(false, "Insufficient balance! You have ${_totalPoints.value} points.")
        }

        val amountBdt = pointsToWithdraw.toDouble() / POINTS_PER_BDT
        val requestId = "WD-" + UUID.randomUUID().toString().take(8).uppercase()

        val request = WithdrawRequest(
            id = requestId,
            userId = user.id,
            userName = user.name,
            verificationStatus = if (user.isVerified) "Verified (Google)" else "Unverified (Phone)",
            method = method,
            accountNumber = accountNumber,
            pointsUsed = pointsToWithdraw,
            amountBdt = amountBdt,
            dateTimestamp = System.currentTimeMillis(),
            status = "Pending",
            syncedToSheet = false
        )

        // Deduct balance locally
        deductPoints(pointsToWithdraw)

        // Save in local Room DB
        withdrawDao.insertRequest(request)

        // Attempt POST to Google Sheets Apps Script Webhook
        val sheetSyncSuccess = postToGoogleSheet(request)

        if (sheetSyncSuccess) {
            withdrawDao.updateRequest(request.copy(syncedToSheet = true))
        }

        return Pair(true, requestId)
    }

    private suspend fun postToGoogleSheet(withdrawRequest: WithdrawRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = _webhookUrl.value
                if (url.isBlank() || url.contains("SAMPLE")) {
                    return@withContext false
                }

                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    .format(Date(withdrawRequest.dateTimestamp))

                val jsonPayload = JSONObject().apply {
                    put("requestId", withdrawRequest.id)
                    put("userId", withdrawRequest.userId)
                    put("userName", withdrawRequest.userName)
                    put("verificationStatus", withdrawRequest.verificationStatus)
                    put("method", withdrawRequest.method)
                    put("accountNumber", withdrawRequest.accountNumber)
                    put("pointsUsed", withdrawRequest.pointsUsed)
                    put("amountBdt", withdrawRequest.amountBdt)
                    put("date", dateStr)
                    put("status", withdrawRequest.status)
                }

                val body = jsonPayload.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                val response = httpClient.newCall(request).execute()
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
