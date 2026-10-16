package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Post
import com.example.model.User
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostCard(
    post: Post,
    onLikeClick: () -> Unit,
    onDoubleTapLike: () -> Unit,
    onSaveClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onUserClick: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showBurstHeart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = GlassFillCard),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 7.dp)
            .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(24.dp))
            .testTag("post_card_${post.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            // --- Header ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                AsyncImage(
                    model = post.user.avatarUrl,
                    contentDescription = "${post.user.username}'s profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0x66FFFFFF), CircleShape)
                        .clickable { onUserClick(post.user) }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onUserClick(post.user) }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.user.username,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (post.user.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "✓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LiquidCyan
                            )
                        }
                    }
                    if (!post.location.isNullOrBlank()) {
                        Text(
                            text = post.location,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else if (!post.audioTrack.isNullOrBlank()) {
                        Text(
                            text = "🎵 ${post.audioTrack}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Main Media with Double Tap Detection & Burst Heart ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .aspectRatio(1.05f)
                    .border(1.dp, Color(0x26FFFFFF), RoundedCornerShape(18.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                onDoubleTapLike()
                                showBurstHeart = true
                                scope.launch {
                                    delay(800)
                                    showBurstHeart = false
                                }
                            }
                        )
                    }
            ) {
                AsyncImage(
                    model = post.mediaUrls.firstOrNull() ?: "",
                    contentDescription = "Post image by ${post.user.username}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x33000000))
                )

                // Animated Heart Pop
                val heartScale by animateFloatAsState(
                    targetValue = if (showBurstHeart) 1f else 0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "heart_scale"
                )
                val heartAlpha by animateFloatAsState(
                    targetValue = if (showBurstHeart) 0.95f else 0f,
                    animationSpec = tween(durationMillis = if (showBurstHeart) 150 else 300),
                    label = "heart_alpha"
                )
                if (heartAlpha > 0.01f) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = heartAlpha),
                        modifier = Modifier
                            .size((110 * heartScale).coerceAtLeast(1f).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- Glass Action Buttons Bar ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                // Like Button
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (post.isLiked) InstaHeartRed.copy(alpha = 0.18f) else Color(0x14FFFFFF))
                        .border(1.dp, if (post.isLiked) InstaHeartRed.copy(alpha = 0.5f) else Color(0x26FFFFFF), CircleShape)
                        .testTag("like_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (post.isLiked) "Unlike" else "Like",
                        tint = if (post.isLiked) InstaHeartRed else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Comment Button
                IconButton(
                    onClick = onCommentClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .border(1.dp, Color(0x26FFFFFF), CircleShape)
                        .testTag("comment_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Share Button
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .border(1.dp, Color(0x26FFFFFF), CircleShape)
                        .testTag("share_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Save / Bookmark Button
                IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (post.isSaved) LiquidCyan.copy(alpha = 0.2f) else Color(0x14FFFFFF))
                        .border(1.dp, if (post.isSaved) LiquidCyan.copy(alpha = 0.6f) else Color(0x26FFFFFF), CircleShape)
                        .testTag("save_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (post.isSaved) "Unsave" else "Save",
                        tint = if (post.isSaved) LiquidCyan else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Likes Count ---
            Text(
                text = "${java.text.NumberFormat.getIntegerInstance().format(post.likesCount)} likes",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 14.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // --- Caption with Expandable text ---
            Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                val fullCaption = post.caption
                val isLong = fullCaption.length > 80
                val displayedCaption = if (isLong && !isExpanded) fullCaption.take(75) + "..." else fullCaption

                Row {
                    Text(
                        text = post.user.username + " ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = displayedCaption,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (isLong && !isExpanded) {
                    Text(
                        text = "more",
                        fontSize = 12.sp,
                        color = LiquidCyan,
                        modifier = Modifier
                            .clickable { isExpanded = true }
                            .padding(top = 2.dp)
                    )
                }
            }

            // --- Comments Link ---
            if (post.commentsCount > 0) {
                Text(
                    text = "View all ${post.commentsCount} comments",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable { onCommentClick() }
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                )
            }

            // --- Timestamp ---
            Text(
                text = post.timestamp,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
            )
        }
    }
}
