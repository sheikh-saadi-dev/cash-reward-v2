package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserProfile
import com.example.ui.components.SAMPLE_APPS_SCRIPT_CODE

@Composable
fun ProfileScreen(
    user: UserProfile?,
    totalPoints: Int,
    dailyStreak: Int,
    adsWatchedToday: Int,
    webhookUrl: String,
    onSaveWebhookUrl: (String) -> Unit,
    onNavigateToHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showSheetGuideModal by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val isVerified = user?.isVerified ?: false

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

            // Profile Page Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "প্রোফাইল ও অ্যাকাউন্ট",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF22113D))
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFFD1C4E9),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Info Hero Card with Radiant Gradient Background
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4A148C),
                                    Color(0xFF2E1253),
                                    Color(0xFF1C0A33)
                                )
                            )
                        )
                        .border(1.5.dp, Color(0xFF6B2A8C), RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar Circle with Ring
                        Box(
                            modifier = Modifier
                                .size(82.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Color(0xFFFFC107), Color(0xFF8E24AA))
                                    )
                                )
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF22113D)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (user?.name?.take(1) ?: "U").uppercase(),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFFC107)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user?.name ?: "User",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = user?.emailOrPhone ?: "",
                            fontSize = 13.sp,
                            color = Color(0xFFD1C4E9)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Verification Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isVerified) Color(0xFF00E676) else Color(0xFFFFAB00))
                                .padding(horizontal = 14.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isVerified) Icons.Default.Verified else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isVerified) "Verified Google Account" else "Unverified Phone Account",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // User Stats Grid (Points, Streak, Ads)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileStatCard(
                    title = "মোট পয়েন্ট",
                    value = "%,d".format(totalPoints),
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFFFC107),
                    modifier = Modifier.weight(1f)
                )

                ProfileStatCard(
                    title = "স্ট্রিক",
                    value = "$dailyStreak দিন",
                    icon = Icons.Default.CheckCircle,
                    iconTint = Color(0xFF00E5FF),
                    modifier = Modifier.weight(1f)
                )

                ProfileStatCard(
                    title = "আজকের অ্যাড",
                    value = "$adsWatchedToday টি",
                    icon = Icons.Default.PlayCircle,
                    iconTint = Color(0xFFA142F4),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Highlight Box for Google Apps Script
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1035)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF00E5FF))
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
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google Apps Script (Code.gs)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Apps Script Code", SAMPLE_APPS_SCRIPT_CODE)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Code.gs কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFFFC107))
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "গুগল শীটে সব ক্যাশআউট রিকোয়েস্ট অটোমেটিক পাঠাতে নিচের Code.gs স্ক্রিপ্ট ব্যবহার করুন:",
                        fontSize = 12.sp,
                        color = Color(0xFFD1C4E9)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Code snippet box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F071D))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = SAMPLE_APPS_SCRIPT_CODE.take(180) + "...\n\n/* সম্পূর্ণ কোড দেখতে ও সেটআপ গাইড খুলতে নিচে ট্যাপ করুন */",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF81C784)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showSheetGuideModal = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("open_sheets_guide_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00E5FF),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("পূর্ণাঙ্গ গাইড ও Webhook URL সেটিং", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "অন্যান্য মেনু",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Menu Options
            ProfileMenuItem(
                title = "Withdraw History (ক্যাশআউট ইতিহাস)",
                subtitle = "আপনার সব উইথড্রল স্ট্যাটাস দেখুন",
                icon = Icons.Default.History,
                iconColor = Color(0xFF00E5FF),
                onClick = onNavigateToHistory,
                testTag = "menu_history"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileMenuItem(
                title = "Help & Support (সহায়তা)",
                subtitle = "যেকোনো সমস্যা ও প্রশ্নের সমাধান",
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                iconColor = Color(0xFF00E676),
                onClick = { showHelpDialog = true },
                testTag = "menu_help"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileMenuItem(
                title = "Privacy Policy (গোপনীয়তা নীতি)",
                subtitle = "আমাদের ডেটা সুরক্ষার তথ্য",
                icon = Icons.Default.Lock,
                iconColor = Color(0xFFC788FF),
                onClick = { showPrivacyDialog = true },
                testTag = "menu_privacy"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Logout Button
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("logout_button"),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF5252)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("লগ আউট করুন (Log Out)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Google Sheets Guide Modal
        if (showSheetGuideModal) {
            com.example.ui.components.GoogleSheetsGuideModal(
                currentWebhookUrl = webhookUrl,
                onSaveWebhookUrl = onSaveWebhookUrl,
                onDismiss = { showSheetGuideModal = false }
            )
        }

        // Help Dialog
        if (showHelpDialog) {
            Dialog(onDismissRequest = { showHelpDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF472778))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Help & Support", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFC107))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "১. টাকা কখন পাওয়া যাবে?\nসাধারনত ২৪ ঘণ্টার মধ্যে bKash/Nagad নম্বরে টাকা রিসিভ হবে।\n\n২. ভিডিও লোড না হলে কী করবো?\nকিছুক্ষণ পর পুনরায় চেষ্টা করুন অথবা ইন্টারনেট কানেকশন চেক করুন।\n\n৩. অফিসিয়াল হেল্পলাইন: support@earnrewardbd.com",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { showHelpDialog = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381F62))
                        ) {
                            Text("বন্ধ করুন", color = Color.White)
                        }
                    }
                }
            }
        }

        // Privacy Policy Dialog
        if (showPrivacyDialog) {
            Dialog(onDismissRequest = { showPrivacyDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF472778))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Privacy Policy", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "আমরা আপনার গোপনীয়তা রক্ষা করতে বদ্ধপরিকর। আপনার প্রদত্ত ফোন নম্বর ও অ্যাকাউন্ট তথ্য কেবল উইথড্রল প্রসেসিং ও গুগল শীটে ট্র্যাকিংয়ের উদ্দেশ্যে ব্যবহৃত হবে। অন্য কারও সাথে শেয়ার করা হবে না।",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { showPrivacyDialog = false },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381F62))
                        ) {
                            Text("বন্ধ করুন", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF381F62))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E1752)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 11.sp, color = Color(0xFFD1C4E9))
        }
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF381F62))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E1752)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(subtitle, fontSize = 11.sp, color = Color(0xFFD1C4E9))
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF9583B5),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

