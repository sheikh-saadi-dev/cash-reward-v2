package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile

@Composable
fun HomeScreen(
    user: UserProfile?,
    totalPoints: Int,
    adsWatchedToday: Int,
    maxAdsPerDay: Int,
    pointsPerAd: Int,
    pointsPerBdt: Int,
    onWatchVideoClick: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onNavigateToDailyReward: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isVerified = user?.isVerified ?: false
    val amountBdt = totalPoints.toDouble() / pointsPerBdt

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF140A26))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // User Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "হ্যালো, ${user?.name ?: "User"}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isVerified) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Verified Account (Google)",
                                fontSize = 12.sp,
                                color = Color(0xFF00E676),
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFFFAB00),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Unverified Account (Phone)",
                                fontSize = 12.sp,
                                color = Color(0xFFFFAB00),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Quick Daily Bonus Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF22113D))
                        .border(1.dp, Color(0xFFFFC107), RoundedCornerShape(12.dp))
                        .clickable { onNavigateToDailyReward() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Daily Bonus",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Points Balance Card (Gradient, Large Number, Equivalent BDT)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("points_balance_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF8E24AA), Color(0xFF3D1E6D), Color(0xFF1F0D3D))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "মোট ব্যালেন্স (Points)",
                                fontSize = 13.sp,
                                color = Color(0xFFD1C4E9),
                                fontWeight = FontWeight.Medium
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x33000000))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "$pointsPerBdt Pts = ৳১ BDT",
                                    fontSize = 11.sp,
                                    color = Color(0xFF00E5FF),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "%,d Pts".format(totalPoints),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFC107)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "= ৳%.2f BDT".format(amountBdt),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToWithdraw,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("home_withdraw_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "টাকা তুলুন (Withdraw to bKash / Nagad)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Banner Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1784748660991),
                    contentDescription = "Hero Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Watch Video & Earn Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("watch_video_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF472778))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF381F62)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ভিডিও দেখে আয় করুন (Rewarded Video)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "প্রতিটি ভিডিও = +$pointsPerAd পয়েন্ট",
                                fontSize = 12.sp,
                                color = Color(0xFF00E5FF)
                            )
                            Text(
                                text = "Ad Unit: Video (ID: ca-app-pub-4161371611521203/1344210098)",
                                fontSize = 10.sp,
                                color = Color(0xFFB388FF)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress Bar
                    val progressRatio = adsWatchedToday.toFloat() / maxAdsPerDay.toFloat()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "আজকের ভিডিও লিমিট",
                            fontSize = 12.sp,
                            color = Color(0xFFD1C4E9)
                        )
                        Text(
                            text = "$adsWatchedToday / $maxAdsPerDay Ads",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progressRatio.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFFFFC107),
                        trackColor = Color(0xFF381F62)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onWatchVideoClick,
                        enabled = adsWatchedToday < maxAdsPerDay,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("watch_ad_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFA142F4),
                            disabledContainerColor = Color(0xFF381F62),
                            contentColor = Color.White,
                            disabledContentColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (adsWatchedToday < maxAdsPerDay) "Watch Video & Earn Points" else "দৈনিক লিমিট পূর্ণ হয়েছে",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3-Step How It Works Guide
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF381F62))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "কীভাবে কাজ করে (৩ সহজ ধাপ)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val steps = listOf(
                        Pair("১", "ভিডিও অ্যাড দেখুন ও প্রতিদিন ডেইলি বোনাস ক্লেইম করে পয়েন্ট সংগ্রহ করুন।"),
                        Pair("২", "জমানো পয়েন্ট সর্বনিম্ন লিমিটে (৫০,০০০+ Pts) পৌঁছালে Cashout বাটনে চাপুন।"),
                        Pair("৩", "আপনার bKash বা Nagad নম্বর দিয়ে সাবমিট দিন। অ্যাডমিন রিভিউ করে টাকা পাঠিয়ে দেবে।")
                    )

                    steps.forEach { step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF381F62)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = step.first,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E5FF)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step.second,
                                fontSize = 13.sp,
                                color = Color(0xFFD1C4E9),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
