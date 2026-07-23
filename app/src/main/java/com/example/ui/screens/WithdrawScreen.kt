package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.WithdrawUiState

@Composable
fun WithdrawScreen(
    user: UserProfile?,
    totalPoints: Int,
    pointsPerBdt: Int,
    withdrawState: WithdrawUiState,
    onSubmitWithdraw: (method: String, accountNumber: String, points: String) -> Unit,
    onResetWithdrawState: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedMethod by remember { mutableStateOf("bKash") }
    var accountNumberInput by remember { mutableStateOf("") }
    var pointsInput by remember { mutableStateOf("50000") }
    var validationError by remember { mutableStateOf("") }

    val isVerified = user?.isVerified ?: false
    val minPoints = if (isVerified) 50000 else 75000
    val minBdt = minPoints / pointsPerBdt

    val enteredPoints = pointsInput.toIntOrNull() ?: 0
    val equivalentBdt = enteredPoints.toDouble() / pointsPerBdt

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ক্যাশ আউট (Withdraw)",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFC107)
                    )
                    Text(
                        text = "bKash বা Nagad নম্বরে সরাসরি টাকা তুলুন",
                        fontSize = 12.sp,
                        color = Color(0xFFD1C4E9)
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToHistory,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00E5FF)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00E5FF)),
                    modifier = Modifier.testTag("withdraw_history_button")
                ) {
                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("হিস্ট্রি", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Balance Summary Pill
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF472778))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("আপনার বর্তমান পয়েন্ট", fontSize = 12.sp, color = Color(0xFFD1C4E9))
                        Text(
                            "%,d Pts".format(totalPoints),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC107)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("সমমানের BDT", fontSize = 12.sp, color = Color(0xFFD1C4E9))
                        Text(
                            "৳%.2f BDT".format(totalPoints.toDouble() / pointsPerBdt),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E676)
                        )
                    }
                }
            }

            if (!isVerified) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF33200B))
                        .border(1.dp, Color(0xFFFFAB00), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFFAB00), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Unverified অ্যাকাউন্টের জন্য সর্বনিম্ন উত্তোলন $minPoints পয়েন্ট (৳$minBdt)। দ্রুত টাকা পেতে Google দিয়ে Verify করুন!",
                            fontSize = 11.sp,
                            color = Color(0xFFFFECB3)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "১. পেমেন্ট মেথড নির্বাচন করুন",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Method Selector Cards (bKash & Nagad)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MethodCard(
                    title = "bKash",
                    subtitle = "বিক্যাশ পার্সোনাল",
                    brandColor = Color(0xFFE2136E),
                    isSelected = selectedMethod == "bKash",
                    onClick = { selectedMethod = "bKash" },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("method_bkash")
                )

                MethodCard(
                    title = "Nagad",
                    subtitle = "নগদ পার্সোনাল",
                    brandColor = Color(0xFFF7931E),
                    isSelected = selectedMethod == "Nagad",
                    onClick = { selectedMethod = "Nagad" },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("method_nagad")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "২. অ্যাকাউন্টের তথ্য দিন",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Account Number Input
            OutlinedTextField(
                value = accountNumberInput,
                onValueChange = { accountNumberInput = it; validationError = "" },
                label = { Text("$selectedMethod নম্বর (01XXXXXXXXX)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFFFC107)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdraw_account_number_input"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color(0xFF472778),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFFC107),
                    unfocusedLabelColor = Color(0xFFD1C4E9),
                    focusedContainerColor = Color(0xFF22113D),
                    unfocusedContainerColor = Color(0xFF22113D)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Points to Withdraw Input
            OutlinedTextField(
                value = pointsInput,
                onValueChange = { pointsInput = it; validationError = "" },
                label = { Text("কত পয়েন্ট তুলবেন? (মিন: $minPoints Pts)") },
                leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFF00E5FF)) },
                trailingIcon = {
                    Text(
                        text = "= ৳%.2f".format(equivalentBdt),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("withdraw_points_input"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color(0xFF472778),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color(0xFFFFC107),
                    unfocusedLabelColor = Color(0xFFD1C4E9),
                    focusedContainerColor = Color(0xFF22113D),
                    unfocusedContainerColor = Color(0xFF22113D)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            if (validationError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = validationError,
                    color = Color(0xFFFF5252),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            val isSubmitting = withdrawState is WithdrawUiState.Submitting

            Button(
                onClick = {
                    val phoneRegex = Regex("^01[3-9]\\d{8}$")
                    if (!phoneRegex.matches(accountNumberInput.trim())) {
                        validationError = "সঠিক ১১ ডিজিটের $selectedMethod নম্বর লিখুন"
                        return@Button
                    }

                    if (enteredPoints < minPoints) {
                        validationError = "সর্বনিম্ন $minPoints পয়েন্ট (৳$minBdt) ক্যাশ আউট করতে পারবেন"
                        return@Button
                    }

                    if (enteredPoints > totalPoints) {
                        validationError = "আপনার পর্যাপ্ত পয়েন্ট নেই (বর্তমান পয়েন্ট: $totalPoints)"
                        return@Button
                    }

                    onSubmitWithdraw(selectedMethod, accountNumberInput.trim(), pointsInput.trim())
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_withdraw_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    disabledContainerColor = Color(0xFF381F62),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Submit Cashout Request",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Success / Error Feedback Dialog
        if (withdrawState is WithdrawUiState.Success) {
            WithdrawSuccessDialog(
                requestId = withdrawState.requestId,
                onDismiss = {
                    onResetWithdrawState()
                    onNavigateToHistory()
                }
            )
        } else if (withdrawState is WithdrawUiState.Error) {
            WithdrawErrorDialog(
                message = withdrawState.message,
                onDismiss = onResetWithdrawState
            )
        }
    }
}

@Composable
fun MethodCard(
    title: String,
    subtitle: String,
    brandColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) brandColor else Color(0xFF472778),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2E1752) else Color(0xFF22113D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(brandColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.take(1),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFFD1C4E9)
            )
        }
    }
}

@Composable
fun WithdrawSuccessDialog(
    requestId: String,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, Color(0xFF00E676), RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black, modifier = Modifier.size(36.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "অনুরোধ সফল হয়েছে!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Request ID: $requestId",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00E5FF)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "আপনার ক্যাশআউট রিকোয়েস্ট অ্যাডমিনের কাছে পাঠানো হয়েছে। খুব শীঘ্রই টাকা আপনার নম্বরে পৌঁছে যাবে!",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_success_dialog_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107), contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("হিস্ট্রি দেখুন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WithdrawErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(2.dp, Color(0xFFFF5252), RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ত্রুটি!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5252)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_error_dialog_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381F62), contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("পুনরায় চেষ্টা করুন")
                }
            }
        }
    }
}
