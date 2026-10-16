package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.model.NavigationTab
import com.example.model.User
import com.example.ui.theme.LiquidCyan
import com.example.ui.theme.LiquidGlassBorderBrush
import com.example.ui.theme.LiquidStoryRingGradient

@Composable
fun InstaBottomNavBar(
    currentTab: NavigationTab,
    currentUser: User,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        // Floating Frosted Glass Capsule
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0x66151030))
                .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(30.dp))
                .padding(horizontal = 6.dp)
        ) {
            // 1. Home
            NavIconItem(
                isSelected = currentTab == NavigationTab.HOME,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                contentDescription = "Home",
                testTag = "nav_home",
                onClick = { onTabSelected(NavigationTab.HOME) }
            )

            // 2. Explore / Search
            NavIconItem(
                isSelected = currentTab == NavigationTab.EXPLORE,
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                contentDescription = "Explore",
                testTag = "nav_explore",
                onClick = { onTabSelected(NavigationTab.EXPLORE) }
            )

            // 3. Create (+)
            NavIconItem(
                isSelected = false,
                selectedIcon = Icons.Outlined.AddBox,
                unselectedIcon = Icons.Outlined.AddBox,
                contentDescription = "Create Post",
                testTag = "nav_create",
                onClick = { onTabSelected(NavigationTab.CREATE) }
            )

            // 4. Reels
            NavIconItem(
                isSelected = currentTab == NavigationTab.REELS,
                selectedIcon = Icons.Outlined.VideoLibrary,
                unselectedIcon = Icons.Outlined.VideoLibrary,
                contentDescription = "Reels",
                testTag = "nav_reels",
                onClick = { onTabSelected(NavigationTab.REELS) }
            )

            // 5. Profile
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .clickable { onTabSelected(NavigationTab.PROFILE) }
                    .testTag("nav_profile")
            ) {
                val isSelected = currentTab == NavigationTab.PROFILE
                val borderModifier = if (isSelected) {
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .border(2.dp, LiquidStoryRingGradient, CircleShape)
                        .padding(2.dp)
                } else {
                    Modifier
                        .size(26.dp)
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                }

                AsyncImage(
                    model = currentUser.avatarUrl,
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = borderModifier.clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun NavIconItem(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color(0x2B00F2FE) else Color.Transparent)
            .border(1.dp, if (isSelected) Color(0x4D00F2FE) else Color.Transparent, CircleShape)
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else unselectedIcon,
            contentDescription = contentDescription,
            tint = if (isSelected) LiquidCyan else Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
    }
}
