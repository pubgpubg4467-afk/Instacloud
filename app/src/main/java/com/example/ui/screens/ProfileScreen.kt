package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Post
import com.example.model.StoryHighlight
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: User,
    isCurrentUser: Boolean,
    userPosts: List<Post>,
    highlights: List<StoryHighlight>,
    onEditProfileClick: () -> Unit,
    onFollowToggle: () -> Unit,
    onPostClick: (Post) -> Unit,
    onBackClick: (() -> Unit)? = null,
    onSignOut: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("profile_screen")
    ) {
        // --- Top Bar ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = user.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (user.isVerified) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "✓",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = LiquidCyan
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isCurrentUser) {
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color(0xFF1E1136))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Log Out", color = Color(0xFFFF5252)) },
                            onClick = {
                                showMenu = false
                                onSignOut()
                            }
                        )
                    }
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Header Stats inside Frosted Glass Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(GlassFillCard)
                        .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(24.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Avatar with Liquid Story Ring
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(82.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, LiquidStoryRingGradient, CircleShape)
                                .padding(4.dp)
                        ) {
                            AsyncImage(
                                model = user.avatarUrl,
                                contentDescription = "${user.username}'s avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.weight(1f)
                        ) {
                            ProfileStatItem(count = user.postsCount.toString(), label = "Posts")
                            ProfileStatItem(
                                count = if (user.followersCount > 999) "${user.followersCount / 1000}K" else user.followersCount.toString(),
                                label = "Followers"
                            )
                            ProfileStatItem(count = user.followingCount.toString(), label = "Following")
                        }
                    }
                }

                // Bio
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = user.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = user.bio,
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )
                    if (user.website.isNotBlank()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "🔗 ${user.website}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = LiquidCyan
                        )
                    }
                }

                // Action Buttons with Frosted Glass Styling
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    if (isCurrentUser) {
                        Button(
                            onClick = onEditProfileClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x26FFFFFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("edit_profile_button")
                        ) {
                            Text(text = "Edit profile", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { /* share profile */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x26FFFFFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Share profile", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Button(
                            onClick = onFollowToggle,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user.isFollowing) Color(0x26FFFFFF) else LiquidCyan.copy(alpha = 0.25f),
                                contentColor = if (user.isFollowing) Color.White else LiquidCyan
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (user.isFollowing) Color(0x33FFFFFF) else LiquidCyan),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = if (user.isFollowing) "Following" else "Follow",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = { /* message user */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0x26FFFFFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF)),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Message", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Story Highlights
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(highlights) { highlight ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color(0x40FFFFFF), CircleShape)
                                    .padding(3.dp)
                            ) {
                                AsyncImage(
                                    model = highlight.coverUrl,
                                    contentDescription = highlight.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = highlight.title,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Tabs: Grid / Reels / Tagged
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = LiquidCyan
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                Icons.Default.GridOn,
                                contentDescription = "Grid",
                                tint = if (selectedTab == 0) LiquidCyan else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                Icons.Outlined.VideoLibrary,
                                contentDescription = "Reels",
                                tint = if (selectedTab == 1) LiquidCyan else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                Icons.Outlined.AccountBox,
                                contentDescription = "Tagged",
                                tint = if (selectedTab == 2) LiquidCyan else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }

            // Post Grid (Chunked in rows of 3)
            val postsToDisplay = userPosts
            val chunked = postsToDisplay.chunked(3)
            items(chunked) { rowPosts ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    for (post in rowPosts) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(0.5.dp, Color(0x26FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { onPostClick(post) }
                        ) {
                            AsyncImage(
                                model = post.mediaUrls.firstOrNull() ?: "",
                                contentDescription = "Post thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    val missing = 3 - rowPosts.size
                    for (i in 0 until missing) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}
