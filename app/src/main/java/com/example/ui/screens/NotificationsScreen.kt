package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.ui.theme.LiquidCyan

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onFollowToggle: (String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("notifications_screen")
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            IconButton(
                onClick = onBack,
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
            Text(
                text = "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        HorizontalDivider(color = Color(0x26FFFFFF))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            item {
                Text(
                    text = "Activity",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(notifications, key = { it.id }) { item ->
                NotificationRow(item = item, onFollowToggle = { onFollowToggle(item.user.id) })
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
fun NotificationRow(
    item: NotificationItem,
    onFollowToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = item.user.avatarUrl,
            contentDescription = "${item.user.username}'s avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                append(item.user.username + " ")
            }
            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.9f))) {
                append(item.message + " ")
            }
            withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)) {
                append(item.timeAgo)
            }
        }

        Text(
            text = annotatedText,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (item.type == NotificationType.FOLLOW) {
            Button(
                onClick = onFollowToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.isFollowing) Color(0x26FFFFFF) else LiquidCyan.copy(alpha = 0.25f),
                    contentColor = if (item.isFollowing) Color.White else LiquidCyan
                ),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (item.isFollowing) Color(0x33FFFFFF) else LiquidCyan),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = if (item.isFollowing) "Following" else "Follow Back",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (item.postThumbnail != null) {
            AsyncImage(
                model = item.postThumbnail,
                contentDescription = "Post thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
            )
        }
    }
}
