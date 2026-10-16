package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Post
import com.example.model.Story
import com.example.model.User
import com.example.ui.components.PostCard
import com.example.ui.components.StoryAvatar
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<Story>,
    currentUser: User,
    unreadMessagesCount: Int,
    unreadNotificationsCount: Int,
    onStoryClick: (Int) -> Unit,
    onLikePost: (String) -> Unit,
    onDoubleTapLikePost: (String) -> Unit,
    onSavePost: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onSharePost: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onOpenDirectMessages: () -> Unit,
    onOpenNotifications: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                unreadNotificationsCount = unreadNotificationsCount,
                unreadMessagesCount = unreadMessagesCount,
                onNotificationsClick = onOpenNotifications,
                onDirectMessagesClick = onOpenDirectMessages
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_feed_list")
        ) {
            // Stories Glass Tray
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(GlassFillCard)
                        .border(1.dp, LiquidGlassSubtleBorderBrush, RoundedCornerShape(22.dp))
                ) {
                    StoriesRow(
                        stories = stories,
                        currentUser = currentUser,
                        onStoryClick = onStoryClick
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Feed Posts
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onLikeClick = { onLikePost(post.id) },
                    onDoubleTapLike = { onDoubleTapLikePost(post.id) },
                    onSaveClick = { onSavePost(post.id) },
                    onCommentClick = { onCommentClick(post.id) },
                    onShareClick = { onSharePost(post.id) },
                    onUserClick = onUserClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun HomeTopBar(
    unreadNotificationsCount: Int,
    unreadMessagesCount: Int,
    onNotificationsClick: () -> Unit,
    onDirectMessagesClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 16.dp)
            .background(Color(0x3B0C091E))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(Color.Transparent, Color(0x26FFFFFF))),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        // Glowing Brand Title with Liquid Gradient
        Text(
            text = "InstaSnap",
            style = TextStyle(
                brush = LiquidBrandGradient,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive
            ),
            modifier = Modifier.weight(1f)
        )

        // Glass Activity / Notifications button
        IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
                .testTag("notifications_button")
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Activity Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                if (unreadNotificationsCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(LiquidNeonPink)
                            .border(1.5.dp, LiquidDeepSpace, CircleShape)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Glass Direct Messages button
        IconButton(
            onClick = onDirectMessagesClick,
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
                .testTag("direct_messages_button")
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Direct Messages",
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
                if (unreadMessagesCount > 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(LiquidCyan)
                    ) {
                        Text(
                            text = unreadMessagesCount.toString(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF090A14)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StoriesRow(
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Current User Story item
        item {
            StoryAvatar(
                avatarUrl = currentUser.avatarUrl,
                username = currentUser.username,
                isCurrentUser = true,
                hasStory = currentUser.hasStory,
                isSeen = !currentUser.hasUnseenStory,
                onClick = { onStoryClick(0) }
            )
        }

        // Friends stories
        itemsIndexed(stories.drop(1)) { index, story ->
            StoryAvatar(
                avatarUrl = story.user.avatarUrl,
                username = story.user.username,
                hasStory = true,
                isSeen = story.isSeen,
                onClick = { onStoryClick(index + 1) }
            )
        }
    }
}
