package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.ads.MobileAds
import com.example.ui.AppNavHost
import com.example.ui.theme.EarnRewardTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    // Initialize Google Mobile Ads SDK
    MobileAds.initialize(this) {}

    setContent {
      EarnRewardTheme {
        AppNavHost()
      }
    }
  }
}


