package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WithdrawRequest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WithdrawHistoryScreen(
    requests: List<WithdrawRequest>,
    onBackClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredRequests = remember(requests, selectedFilter) {
        when (selectedFilter) {
            "Pending" -> requests.filter { it.status.equals("Pending", ignoreCase = true) }
            "Approved" -> requests.filter { it.status.equals("Approved", ignoreCase = true) }
            else -> requests
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF140A26))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "ক্যাশআউট হিস্ট্রি (History)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC107)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Pending", "Approved").forEach { filterName ->
                    val isSelected = selectedFilter == filterName
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filterName },
                        label = { Text(filterName, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFFC107),
                            selectedLabelColor = Color.Black,
                            containerColor = Color(0xFF22113D),
                            labelColor = Color(0xFFD1C4E9)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("filter_$filterName")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredRequests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF472778),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "কোনো ক্যাশআউট হিস্ট্রি পাওয়া যায়নি",
                            fontSize = 15.sp,
                            color = Color(0xFF9583B5)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRequests, key = { it.id }) { req ->
                        WithdrawRequestCard(request = req)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawRequestCard(request: WithdrawRequest) {
    val methodColor = if (request.method.equals("bKash", ignoreCase = true)) Color(0xFFE2136E) else Color(0xFFF7931E)
    val statusBg = when (request.status.lowercase()) {
        "approved" -> Color(0xFF00E676)
        "rejected" -> Color(0xFFFF5252)
        else -> Color(0xFFFFAB00)
    }

    val dateFormatted = remember(request.dateTimestamp) {
        SimpleDateFormat("dd MMM, yyyy • hh:mm a", Locale.US).format(Date(request.dateTimestamp))
    }

    val maskedPhone = remember(request.accountNumber) {
        if (request.accountNumber.length >= 11) {
            request.accountNumber.take(3) + "****" + request.accountNumber.takeLast(4)
        } else {
            request.accountNumber
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_item_${request.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22113D)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF472778))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(methodColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = request.method.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "${request.method} Cashout",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Acc: $maskedPhone",
                            fontSize = 12.sp,
                            color = Color(0xFFD1C4E9)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "৳%.2f BDT".format(request.amountBdt),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFC107)
                    )
                    Text(
                        text = "%,d Pts".format(request.pointsUsed),
                        fontSize = 11.sp,
                        color = Color(0xFF00E5FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormatted,
                    fontSize = 11.sp,
                    color = Color(0xFF9583B5)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = request.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
