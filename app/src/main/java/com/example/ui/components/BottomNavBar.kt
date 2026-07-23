package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

enum class BottomTab(
    val route: String,
    val titleBangla: String,
    val titleEnglish: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "হোম", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    DAILY_REWARD("daily_reward", "ডেইলি বোনাস", "Reward", Icons.Filled.Star, Icons.Outlined.Star),
    WITHDRAW("withdraw", "ক্যাশ আউট", "Withdraw", Icons.Filled.Payments, Icons.Outlined.Payments),
    PROFILE("profile", "প্রোফাইল", "Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
}

@Composable
fun EarnRewardBottomBar(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color(0xFF22113D)),
        containerColor = Color(0xFF22113D),
        tonalElevation = 8.dp
    ) {
        BottomTab.entries.forEach { tab ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.titleEnglish
                    )
                },
                label = {
                    Text(
                        text = tab.titleBangla,
                        color = if (isSelected) Color(0xFFFFC107) else Color(0xFFD1C4E9)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF140A26),
                    selectedTextColor = Color(0xFFFFC107),
                    indicatorColor = Color(0xFFFFC107),
                    unselectedIconColor = Color(0xFF9583B5),
                    unselectedTextColor = Color(0xFF9583B5)
                ),
                modifier = Modifier.testTag("nav_tab_${tab.route}")
            )
        }
    }
}
