package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomTab
import com.example.ui.components.EarnRewardBottomBar
import com.example.ui.components.VideoAdDialog
import com.example.ui.screens.DailyRewardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WithdrawHistoryScreen
import com.example.ui.screens.WithdrawScreen

enum class AppScreenRoute {
    SPLASH,
    LOGIN,
    MAIN,
    WITHDRAW_HISTORY
}

@Composable
fun AppNavHost(
    viewModel: EarnRewardViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    val adsWatchedToday by viewModel.adsWatchedToday.collectAsState()
    val dailyStreak by viewModel.dailyStreak.collectAsState()
    val webhookUrl by viewModel.webhookUrl.collectAsState()
    val withdrawRequests by viewModel.withdrawRequests.collectAsState()
    val adState by viewModel.adState.collectAsState()
    val withdrawUiState by viewModel.withdrawUiState.collectAsState()

    var currentRoute by remember { mutableStateOf(AppScreenRoute.SPLASH) }
    var currentBottomTab by remember { mutableStateOf(BottomTab.HOME) }

    val isVerified = currentUser?.isVerified ?: false
    val maxAds = if (isVerified) viewModel.repository.MAX_ADS_PER_DAY_VERIFIED else viewModel.repository.MAX_ADS_PER_DAY_UNVERIFIED
    val pointsPerAd = if (isVerified) viewModel.repository.POINTS_PER_AD_VERIFIED else viewModel.repository.POINTS_PER_AD_UNVERIFIED

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentRoute) {
            AppScreenRoute.SPLASH -> {
                SplashScreen(
                    isLoggedIn = currentUser != null,
                    onNavigateNext = { isLoggedIn ->
                        if (isLoggedIn) {
                            currentRoute = AppScreenRoute.MAIN
                        } else {
                            currentRoute = AppScreenRoute.LOGIN
                        }
                    }
                )
            }

            AppScreenRoute.LOGIN -> {
                LoginScreen(
                    onGoogleLogin = { name, email ->
                        viewModel.loginWithGoogle(name, email)
                    },
                    onPhoneLogin = { name, phone ->
                        viewModel.loginWithPhone(name, phone)
                    },
                    onLoginSuccess = {
                        currentRoute = AppScreenRoute.MAIN
                    }
                )
            }

            AppScreenRoute.MAIN -> {
                Scaffold(
                    bottomBar = {
                        EarnRewardBottomBar(
                            currentTab = currentBottomTab,
                            onTabSelected = { currentBottomTab = it }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentBottomTab) {
                            BottomTab.HOME -> {
                                HomeScreen(
                                    user = currentUser,
                                    totalPoints = totalPoints,
                                    adsWatchedToday = adsWatchedToday,
                                    maxAdsPerDay = maxAds,
                                    pointsPerAd = pointsPerAd,
                                    pointsPerBdt = viewModel.repository.POINTS_PER_BDT,
                                    onWatchVideoClick = { viewModel.startRewardedAd() },
                                    onNavigateToWithdraw = { currentBottomTab = BottomTab.WITHDRAW },
                                    onNavigateToDailyReward = { currentBottomTab = BottomTab.DAILY_REWARD }
                                )
                            }

                            BottomTab.DAILY_REWARD -> {
                                DailyRewardScreen(
                                    dailyStreak = dailyStreak,
                                    canClaim = viewModel.repository.canClaimDailyReward(),
                                    todayRewardAmount = viewModel.repository.getTodayDailyRewardAmount(),
                                    onClaimClick = { viewModel.claimDailyReward() }
                                )
                            }

                            BottomTab.WITHDRAW -> {
                                WithdrawScreen(
                                    user = currentUser,
                                    totalPoints = totalPoints,
                                    pointsPerBdt = viewModel.repository.POINTS_PER_BDT,
                                    withdrawState = withdrawUiState,
                                    onSubmitWithdraw = { method, accountNumber, pointsStr ->
                                        viewModel.submitWithdraw(method, accountNumber, pointsStr)
                                    },
                                    onResetWithdrawState = { viewModel.resetWithdrawUiState() },
                                    onNavigateToHistory = { currentRoute = AppScreenRoute.WITHDRAW_HISTORY }
                                )
                            }

                            BottomTab.PROFILE -> {
                                ProfileScreen(
                                    user = currentUser,
                                    totalPoints = totalPoints,
                                    dailyStreak = dailyStreak,
                                    adsWatchedToday = adsWatchedToday,
                                    webhookUrl = webhookUrl,
                                    onSaveWebhookUrl = { viewModel.updateWebhookUrl(it) },
                                    onNavigateToHistory = { currentRoute = AppScreenRoute.WITHDRAW_HISTORY },
                                    onLogout = {
                                        viewModel.logout()
                                        currentRoute = AppScreenRoute.LOGIN
                                    }
                                )
                            }
                        }
                    }
                }
            }

            AppScreenRoute.WITHDRAW_HISTORY -> {
                WithdrawHistoryScreen(
                    requests = withdrawRequests,
                    onBackClick = { currentRoute = AppScreenRoute.MAIN }
                )
            }
        }

        // Global Rewarded Video Ad Overlay Dialog
        VideoAdDialog(
            adState = adState,
            onDismiss = { viewModel.resetAdState() }
        )
    }
}
