package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.MockDataProvider
import com.example.model.ChatMessage
import com.example.model.ChatThread
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun DirectMessagesScreen(
    currentUser: User,
    threads: List<ChatThread>,
    activeChatId: String?,
    onOpenThread: (String) -> Unit,
    onCloseChat: () -> Unit,
    onBack: () -> Unit,
    onSendMessage: (String, String) -> Unit
) {
    if (activeChatId != null) {
        val activeThread = threads.find { it.id == activeChatId }
        if (activeThread != null) {
            ChatDetailScreen(
                thread = activeThread,
                onBack = onCloseChat,
                onSendMessage = { text -> onSendMessage(activeThread.id, text) }
            )
            return
        }
    }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("direct_messages_screen")
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
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
                    contentDescription = "Back to Feed",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = currentUser.username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { /* new message */ },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New message",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            // Notes Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(MockDataProvider.sampleNotes) { note ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(76.dp)
                        ) {
                            // Frosted Note Bubble
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x3B15102E))
                                    .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(14.dp))
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = note.noteText,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // Avatar
                            AsyncImage(
                                model = note.user.avatarUrl,
                                contentDescription = note.user.username,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, Color(0x40FFFFFF), CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (note.user.id == currentUser.id) "Your note" else note.user.username,
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0x26FFFFFF))
            }

            // Messages section title
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Messages",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Requests",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LiquidCyan
                    )
                }
            }

            // Thread items
            items(threads, key = { it.id }) { thread ->
                ChatThreadItem(
                    thread = thread,
                    onClick = { onOpenThread(thread.id) }
                )
            }
        }
    }
}

@Composable
fun ChatThreadItem(
    thread: ChatThread,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("chat_thread_${thread.id}")
    ) {
        Box(modifier = Modifier.size(54.dp)) {
            AsyncImage(
                model = thread.user.avatarUrl,
                contentDescription = "${thread.user.username}'s avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            )
            if (thread.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(LiquidCyan)
                        .border(2.dp, LiquidDeepSpace, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = thread.user.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = thread.lastMessage,
                    fontSize = 13.sp,
                    color = if (thread.unreadCount > 0) Color.White else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (thread.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = " • ${thread.lastMessageTime}",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        if (thread.unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(LiquidCyan)
            )
        } else {
            IconButton(onClick = { /* camera */ }, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ChatDetailScreen(
    thread: ChatThread,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit
) {
    BackHandler { onBack() }
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("chat_detail_screen")
    ) {
        // Chat Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp)
                .background(Color(0x3B15102E))
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            AsyncImage(
                model = thread.user.avatarUrl,
                contentDescription = thread.user.username,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0x4DFFFFFF), CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = thread.user.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = if (thread.isOnline) "Active now" else "Active ${thread.lastMessageTime} ago",
                    fontSize = 11.sp,
                    color = LiquidCyan
                )
            }

            IconButton(onClick = { /* call */ }) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
            }
            IconButton(onClick = { /* video */ }) {
                Icon(Icons.Default.Videocam, contentDescription = "Video", tint = Color.White)
            }
        }

        HorizontalDivider(color = Color(0x26FFFFFF))

        // Messages List
        LazyColumn(
            reverseLayout = false,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(thread.messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Bottom Message Input in Liquid Glass Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = {
                    Text(text = "Message...", fontSize = 14.sp, color = Color.White.copy(alpha = 0.6f))
                },
                leadingIcon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(LiquidCyan, LiquidNeonPurple)
                                )
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0x3B15102E),
                    unfocusedContainerColor = Color(0x3B15102E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(24.dp))
                    .testTag("chat_message_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (messageText.isNotBlank()) {
                IconButton(
                    onClick = {
                        onSendMessage(messageText)
                        messageText = ""
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(LiquidCyan.copy(alpha = 0.25f))
                        .border(1.dp, LiquidCyan, CircleShape)
                        .testTag("send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = LiquidCyan
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        onSendMessage("❤️")
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Send Heart",
                        tint = InstaHeartRed
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleShape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = if (message.isMe) 18.dp else 4.dp,
        bottomEnd = if (message.isMe) 4.dp else 18.dp
    )
    val bubbleModifier = if (message.isMe) {
        Modifier.background(
            Brush.linearGradient(
                colors = listOf(Color(0xD900B4D8), Color(0xD97209B7))
            ),
            bubbleShape
        )
    } else {
        Modifier.background(Color(0x33FFFFFF), bubbleShape)
    }

    Box(
        contentAlignment = if (message.isMe) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .then(bubbleModifier)
                .border(
                    1.dp,
                    if (message.isMe) Color(0x66FFFFFF) else Color(0x33FFFFFF),
                    bubbleShape
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
