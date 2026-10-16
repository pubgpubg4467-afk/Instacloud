package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Reel
import com.example.ui.theme.*

@Composable
fun ReelsScreen(
    reels: List<Reel>,
    onLikeClick: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    onFollowClick: (String) -> Unit,
    onCommentClick: (String) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentReel = reels.getOrNull(currentIndex) ?: return

    // Rotating vinyl disc animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("reels_screen")
    ) {
        // Reel Video Image Background
        AsyncImage(
            model = currentReel.videoThumbnailUrl,
            contentDescription = "Reel video",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay for readability and liquid atmosphere
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x66080A1A),
                            Color.Transparent,
                            Color(0x22000000),
                            Color(0xD9080A1A)
                        )
                    )
                )
        )

        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Reels",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { /* camera */ },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x33120E28))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Up / Down Reel Switchers (overlay navigation for easy emulator switching)
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        ) {
            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x4D120D2B))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowUp,
                        contentDescription = "Previous Reel",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (currentIndex < reels.size - 1) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x4D120D2B))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Next Reel",
                        tint = Color.White
                    )
                }
            }
        }

        // Right Actions Column with Frosted Glass Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 14.dp, bottom = 95.dp)
        ) {
            // Like
            IconButton(
                onClick = { onLikeClick(currentReel.id) },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (currentReel.isLiked) InstaHeartRed.copy(alpha = 0.25f) else Color(0x4D151030))
                    .border(1.dp, if (currentReel.isLiked) InstaHeartRed.copy(alpha = 0.6f) else Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = if (currentReel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like reel",
                    tint = if (currentReel.isLiked) InstaHeartRed else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "${currentReel.likesCount / 1000}K",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Comment
            IconButton(
                onClick = { onCommentClick(currentReel.id) },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0x4D151030))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comments",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = currentReel.commentsCount.toString(),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Share
            IconButton(
                onClick = { /* share */ },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0x4D151030))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = "${currentReel.sharesCount / 1000}K",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Save / Bookmark
            IconButton(
                onClick = { onSaveClick(currentReel.id) },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (currentReel.isSaved) LiquidCyan.copy(alpha = 0.25f) else Color(0x4D151030))
                    .border(1.dp, if (currentReel.isSaved) LiquidCyan.copy(alpha = 0.6f) else Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = if (currentReel.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save reel",
                    tint = if (currentReel.isSaved) LiquidCyan else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // More Options
            IconButton(
                onClick = { /* more */ },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x33151030))
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Spinning Album Vinyl Disc with Liquid Gradient Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .rotate(rotation)
                    .clip(CircleShape)
                    .background(Color(0xFF15102E))
                    .border(2.dp, LiquidStoryRingGradient, CircleShape)
            ) {
                AsyncImage(
                    model = currentReel.user.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Bottom Left Creator & Caption Info inside Frosted Backdrop
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.82f)
                .padding(start = 16.dp, bottom = 95.dp)
        ) {
            // User row
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = currentReel.user.avatarUrl,
                    contentDescription = currentReel.user.username,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, LiquidCyan, CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = currentReel.user.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Glowing Follow button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (currentReel.isFollowing) Color(0x33FFFFFF)
                            else LiquidCyan.copy(alpha = 0.25f)
                        )
                        .border(
                            1.dp,
                            if (currentReel.isFollowing) Color(0x4DFFFFFF) else LiquidCyan,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onFollowClick(currentReel.id) }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (currentReel.isFollowing) "Following" else "Follow",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (currentReel.isFollowing) Color.White else LiquidCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption
            Text(
                text = currentReel.caption,
                color = Color.White,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Audio Bar with Liquid glass chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x33120E28))
                    .border(0.5.dp, Color(0x26FFFFFF), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio",
                    tint = LiquidCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentReel.audioTitle,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
