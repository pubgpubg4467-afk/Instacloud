package com.example.model

data class User(
    val id: String,
    val username: String,
    val name: String,
    val avatarUrl: String,
    val isVerified: Boolean = false,
    val bio: String = "",
    val website: String = "",
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean = false,
    val hasStory: Boolean = false,
    val hasUnseenStory: Boolean = false
)

data class Comment(
    val id: String,
    val username: String,
    val userAvatarUrl: String,
    val text: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: String,
    val user: User,
    val mediaUrls: List<String>,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val comments: List<Comment> = emptyList(),
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val timestamp: String,
    val location: String? = null,
    val audioTrack: String? = null
)

data class Story(
    val id: String,
    val user: User,
    val mediaUrl: String,
    val caption: String? = null,
    val timestamp: String,
    val isSeen: Boolean = false
)

data class StoryHighlight(
    val id: String,
    val title: String,
    val coverUrl: String
)

data class Reel(
    val id: String,
    val user: User,
    val videoThumbnailUrl: String,
    val caption: String,
    val audioTitle: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int = 120,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isFollowing: Boolean = false
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isMe: Boolean
)

data class ChatThread(
    val id: String,
    val user: User,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val messages: List<ChatMessage> = emptyList()
)

data class UserNote(
    val id: String,
    val user: User,
    val noteText: String
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION
}

data class NotificationItem(
    val id: String,
    val user: User,
    val type: NotificationType,
    val message: String,
    val timeAgo: String,
    val postThumbnail: String? = null,
    val isFollowing: Boolean = false
)

enum class NavigationTab {
    HOME,
    EXPLORE,
    CREATE,
    REELS,
    PROFILE
}
