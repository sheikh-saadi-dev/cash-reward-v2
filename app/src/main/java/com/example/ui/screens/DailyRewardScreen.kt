package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyRewardScreen(
    dailyStreak: Int,
    canClaim: Boolean,
    todayRewardAmount: Int,
    onClaimClick: () -> Int
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val currentActiveDay = ((dailyStreak - 1) % 7) + 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF140A26))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ডেইলি রিওয়ার্ড (Daily Bonus)",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107)
            )

            Text(
                text = "প্রতিদিন লগইন করে ধারাবাহিক বোনাস সংগ্রহ করুন!",
                fontSize = 13.sp,
                color = Color(0xFFD1C4E9),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Streak Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("streak_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFC107))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(Color(0xFFFF8F00), Color(0xFF381F62))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Whatshot,
                            contentDescription = "Streak",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "বর্তমান স্ট্রিক (Streak)",
                            fontSize = 13.sp,
                            color = Color(0xFFD1C4E9)
                        )
                        Text(
                            text = "$dailyStreak দিন একটানা",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "৭ দিনের বোনাস গ্রিড",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 7-day grid
            val daysList = listOf(
                Pair(1, 40),
                Pair(2, 50),
                Pair(3, 60),
                Pair(4, 70),
                Pair(5, 80),
                Pair(6, 90),
                Pair(7, 100)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val row1 = daysList.take(4)
                val row2 = daysList.drop(4)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row1.forEach { (dayNum, points) ->
                        val isClaimedPast = dayNum < currentActiveDay || (!canClaim && dayNum == currentActiveDay)
                        val isCurrent = dayNum == currentActiveDay && canClaim

                        DayRewardBox(
                            dayNumber = dayNum,
                            points = points,
                            isClaimed = isClaimedPast,
                            isCurrent = isCurrent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row2.forEach { (dayNum, points) ->
                        val isClaimedPast = dayNum < currentActiveDay || (!canClaim && dayNum == currentActiveDay)
                        val isCurrent = dayNum == currentActiveDay && canClaim

                        DayRewardBox(
                            dayNumber = dayNum,
                            points = points,
                            isClaimed = isClaimedPast,
                            isCurrent = isCurrent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Claim Button
            Button(
                onClick = {
                    val reward = onClaimClick()
                    if (reward > 0) {
                        Toast.makeText(context, "আজকের +$reward পয়েন্ট বোনাস যোগ হয়েছে!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = canClaim,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("claim_daily_reward_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    disabledContainerColor = Color(0xFF381F62),
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (canClaim) "আজকের বোনাস নেন (+$todayRewardAmount Pts)" else "আজকের বোনাস নেওয়া শেষ (Claimed)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DayRewardBox(
    dayNumber: Int,
    points: Int,
    isClaimed: Boolean,
    isCurrent: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isClaimed -> Color(0xFF2E1752)
        isCurrent -> Color(0xFF8E24AA)
        else -> Color(0xFF22113D)
    }

    val borderColor = when {
        isCurrent -> Color(0xFFFFC107)
        isClaimed -> Color(0xFF00E676)
        else -> Color(0xFF381F62)
    }

    Box(
        modifier = modifier
            .height(86.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Day $dayNumber",
                fontSize = 11.sp,
                color = Color(0xFFD1C4E9),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (isClaimed) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Claimed",
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Text(
                    text = "+$points",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isCurrent) Color(0xFFFFC107) else Color.White
                )
            }
        }
    }
}
