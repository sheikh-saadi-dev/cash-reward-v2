package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawRequest
import com.example.data.repository.EarnRewardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed class AdPlayState {
    object Idle : AdPlayState()
    object Loading : AdPlayState()
    data class Playing(val secondsLeft: Int, val totalSeconds: Int = 5) : AdPlayState()
    data class Completed(val pointsEarned: Int) : AdPlayState()
    data class Error(val message: String) : AdPlayState()
}

sealed class WithdrawUiState {
    object Idle : WithdrawUiState()
    object Submitting : WithdrawUiState()
    data class Success(val requestId: String) : WithdrawUiState()
    data class Error(val message: String) : WithdrawUiState()
}

class EarnRewardViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = EarnRewardRepository(application, db.withdrawDao())

    val currentUser: StateFlow<UserProfile?> = repository.currentUser
    val totalPoints: StateFlow<Int> = repository.totalPoints
    val adsWatchedToday: StateFlow<Int> = repository.adsWatchedToday
    val dailyStreak: StateFlow<Int> = repository.dailyStreak
    val lastClaimDate: StateFlow<String> = repository.lastClaimDate
    val webhookUrl: StateFlow<String> = repository.webhookUrl

    val withdrawRequests: StateFlow<List<WithdrawRequest>> = repository.allWithdrawRequests
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _adState = MutableStateFlow<AdPlayState>(AdPlayState.Idle)
    val adState: StateFlow<AdPlayState> = _adState.asStateFlow()

    private val _withdrawUiState = MutableStateFlow<WithdrawUiState>(WithdrawUiState.Idle)
    val withdrawUiState: StateFlow<WithdrawUiState> = _withdrawUiState.asStateFlow()

    private var adJob: Job? = null

    fun loginWithGoogle(name: String, email: String) {
        val googleUser = UserProfile(
            id = "G-" + UUID.randomUUID().toString().take(8),
            name = name.ifBlank { "Google User" },
            emailOrPhone = email.ifBlank { "user@gmail.com" },
            authType = "GOOGLE",
            isVerified = true
        )
        repository.saveUserProfile(googleUser)
    }

    fun loginWithPhone(name: String, phone: String): Pair<Boolean, String> {
        val cleanPhone = phone.trim()
        val cleanName = name.trim()

        if (cleanName.isBlank()) {
            return Pair(false, "আপনার নাম লিখুন")
        }

        val phoneRegex = Regex("^01[3-9]\\d{8}$")
        if (!phoneRegex.matches(cleanPhone)) {
            return Pair(false, "সঠিক ১১ ডিজিটের বাংলাদেশি নম্বর দিন (যেমন: 01712345678)")
        }

        val phoneUser = UserProfile(
            id = "P-" + cleanPhone, // Same phone number lookup identifier
            name = cleanName,
            emailOrPhone = cleanPhone,
            authType = "PHONE",
            isVerified = false
        )
        repository.saveUserProfile(phoneUser)
        return Pair(true, "সফলভাবে লগইন হয়েছে")
    }

    fun logout() {
        repository.logout()
    }

    fun startRewardedAd() {
        val user = currentUser.value
        val isVerified = user?.isVerified ?: false
        val maxAds = if (isVerified) repository.MAX_ADS_PER_DAY_VERIFIED else repository.MAX_ADS_PER_DAY_UNVERIFIED

        if (adsWatchedToday.value >= maxAds) {
            _adState.value = AdPlayState.Error("আজকের দৈনিক লিমিট ($maxAds ভিডিও) শেষ হয়েছে! আগামীকাল আবার চেষ্টা করুন।")
            return
        }

        adJob?.cancel()
        adJob = viewModelScope.launch {
            _adState.value = AdPlayState.Loading
            delay(1200) // Simulated ad load delay

            val totalDuration = 5
            for (sec in totalDuration downTo 1) {
                _adState.value = AdPlayState.Playing(secondsLeft = sec, totalSeconds = totalDuration)
                delay(1000)
            }

            val earned = repository.recordVideoAdWatched()
            _adState.value = AdPlayState.Completed(earned)
        }
    }

    fun resetAdState() {
        _adState.value = AdPlayState.Idle
    }

    fun claimDailyReward(): Int {
        return repository.claimDailyReward()
    }

    fun submitWithdraw(method: String, accountNumber: String, pointsStr: String) {
        viewModelScope.launch {
            val points = pointsStr.toIntOrNull()
            if (points == null || points <= 0) {
                _withdrawUiState.value = WithdrawUiState.Error("সঠিক পয়েন্ট সংখ্যা প্রদান করুন")
                return@launch
            }

            _withdrawUiState.value = WithdrawUiState.Submitting
            val (success, messageOrId) = repository.submitWithdrawRequest(method, accountNumber, points)

            if (success) {
                _withdrawUiState.value = WithdrawUiState.Success(messageOrId)
            } else {
                _withdrawUiState.value = WithdrawUiState.Error(messageOrId)
            }
        }
    }

    fun resetWithdrawUiState() {
        _withdrawUiState.value = WithdrawUiState.Idle
    }

    fun updateWebhookUrl(newUrl: String) {
        repository.setWebhookUrl(newUrl)
    }
}
