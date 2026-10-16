package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*

@Composable
fun StoryAvatar(
    avatarUrl: String,
    username: String,
    size: Dp = 70.dp,
    hasStory: Boolean = true,
    isSeen: Boolean = false,
    isCurrentUser: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            val ringModifier = if (hasStory) {
                val brush = if (isSeen) InstaStorySeenBrush else LiquidStoryRingGradient
                Modifier
                    .size(size)
                    .clip(CircleShape)
                    .border(2.5.dp, brush, CircleShape)
                    .padding(3.5.dp)
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            } else {
                Modifier
                    .size(size - 6.dp)
                    .border(1.dp, Color(0x22FFFFFF), CircleShape)
            }

            AsyncImage(
                model = avatarUrl,
                contentDescription = "$username's avatar",
                contentScale = ContentScale.Crop,
                modifier = ringModifier
                    .clip(CircleShape)
                    .background(Color(0x2BFFFFFF))
            )

            if (isCurrentUser) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(LiquidCyan, LiquidNeonPurple)
                            )
                        )
                        .border(2.dp, LiquidDeepSpace, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add story",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = if (isCurrentUser) "Your story" else username,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(size + 8.dp)
        )
    }
}
