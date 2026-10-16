package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirestoreRepository
import com.example.data.MockDataProvider
import com.example.model.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class InstaUiState(
    val currentTab: NavigationTab = NavigationTab.HOME,
    val currentUser: User = MockDataProvider.currentUser,
    val posts: List<Post> = MockDataProvider.samplePosts,
    val stories: List<Story> = MockDataProvider.sampleStories,
    val reels: List<Reel> = MockDataProvider.sampleReels,
    val chatThreads: List<ChatThread> = MockDataProvider.sampleChatThreads,
    val notifications: List<NotificationItem> = MockDataProvider.sampleNotifications,
    val highlights: List<StoryHighlight> = MockDataProvider.sampleHighlights,
    val exploreGrid: List<String> = MockDataProvider.exploreGridItems,
    val selectedExploreCategory: String = "For You",
    val searchQuery: String = "",
    // Navigation overlays
    val activeStoryIndex: Int? = null,
    val isDirectMessagesOpen: Boolean = false,
    val activeChatId: String? = null,
    val isNotificationsOpen: Boolean = false,
    val activeCommentsPostId: String? = null,
    val activePostDetail: Post? = null,
    val isCreatePostOpen: Boolean = false,
    val isEditProfileOpen: Boolean = false,
    val inspectedUser: User? = null
)

class InstaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(InstaUiState())
    val uiState: StateFlow<InstaUiState> = _uiState.asStateFlow()

    private var repository: FirestoreRepository? = null

    fun bindRepository(repo: FirestoreRepository, currentFirebaseUser: FirebaseUser?) {
        this.repository = repo
        if (currentFirebaseUser != null) {
            val uid = currentFirebaseUser.uid
            val name = currentFirebaseUser.displayName ?: "Creator"
            val username = currentFirebaseUser.displayName?.replace(" ", "")?.lowercase()
                ?: (currentFirebaseUser.email?.substringBefore("@") ?: "user_${uid.take(4)}")
            val avatar = currentFirebaseUser.photoUrl?.toString()
                ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400"

            _uiState.update { state ->
                state.copy(
                    currentUser = state.currentUser.copy(
                        id = uid,
                        name = name,
                        username = username,
                        avatarUrl = avatar
                    )
                )
            }

            viewModelScope.launch {
                repo.syncUserProfile(name, username, "", "")
            }
        }

        viewModelScope.launch {
            repo.observePosts()
                .catch { /* fallback silently */ }
                .collect { livePosts ->
                    _uiState.update { state ->
                        val liveIds = livePosts.map { it.id }.toSet()
                        val nonDuplicateMocks = MockDataProvider.samplePosts.filter { it.id !in liveIds }
                        val merged = livePosts + nonDuplicateMocks
                        state.copy(posts = merged)
                    }
                }
        }
    }

    fun selectTab(tab: NavigationTab) {
        if (tab == NavigationTab.CREATE) {
            _uiState.update { it.copy(isCreatePostOpen = true) }
        } else {
            _uiState.update {
                it.copy(
                    currentTab = tab,
                    activePostDetail = null,
                    inspectedUser = null
                )
            }
        }
    }

    fun openDirectMessages() {
        _uiState.update { it.copy(isDirectMessagesOpen = true, activeChatId = null) }
    }

    fun closeDirectMessages() {
        _uiState.update { it.copy(isDirectMessagesOpen = false, activeChatId = null) }
    }

    fun openChat(threadId: String) {
        _uiState.update { it.copy(activeChatId = threadId) }
    }

    fun closeChat() {
        _uiState.update { it.copy(activeChatId = null) }
    }

    fun openNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = true) }
    }

    fun closeNotifications() {
        _uiState.update { it.copy(isNotificationsOpen = false) }
    }

    fun openComments(postId: String) {
        _uiState.update { it.copy(activeCommentsPostId = postId) }
    }

    fun closeComments() {
        _uiState.update { it.copy(activeCommentsPostId = null) }
    }

    fun openPostDetail(post: Post) {
        _uiState.update { it.copy(activePostDetail = post) }
    }

    fun closePostDetail() {
        _uiState.update { it.copy(activePostDetail = null) }
    }

    fun openStory(index: Int) {
        val stories = _uiState.value.stories.toMutableList()
        if (index in stories.indices) {
            stories[index] = stories[index].copy(isSeen = true)
            _uiState.update { it.copy(activeStoryIndex = index, stories = stories) }
        }
    }

    fun nextStory() {
        val currentIdx = _uiState.value.activeStoryIndex ?: return
        val stories = _uiState.value.stories
        if (currentIdx + 1 < stories.size) {
            openStory(currentIdx + 1)
        } else {
            closeStory()
        }
    }

    fun previousStory() {
        val currentIdx = _uiState.value.activeStoryIndex ?: return
        if (currentIdx - 1 >= 0) {
            openStory(currentIdx - 1)
        }
    }

    fun closeStory() {
        _uiState.update { it.copy(activeStoryIndex = null) }
    }

    fun openCreatePost() {
        _uiState.update { it.copy(isCreatePostOpen = true) }
    }

    fun closeCreatePost() {
        _uiState.update { it.copy(isCreatePostOpen = false) }
    }

    fun openEditProfile() {
        _uiState.update { it.copy(isEditProfileOpen = true) }
    }

    fun closeEditProfile() {
        _uiState.update { it.copy(isEditProfileOpen = false) }
    }

    fun inspectUser(user: User) {
        _uiState.update { it.copy(inspectedUser = user) }
    }

    fun closeInspectedUser() {
        _uiState.update { it.copy(inspectedUser = null) }
    }

    fun togglePostLike(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    val wasLiked = post.isLiked
                    post.copy(
                        isLiked = !wasLiked,
                        likesCount = if (wasLiked) post.likesCount - 1 else post.likesCount + 1
                    )
                } else post
            }
            val updatedDetail = if (state.activePostDetail?.id == postId) {
                val p = state.activePostDetail
                val wasLiked = p.isLiked
                p.copy(
                    isLiked = !wasLiked,
                    likesCount = if (wasLiked) p.likesCount - 1 else p.likesCount + 1
                )
            } else state.activePostDetail
            state.copy(posts = updated, activePostDetail = updatedDetail)
        }

        repository?.let { repo ->
            viewModelScope.launch {
                repo.togglePostLike(postId)
            }
        }
    }

    fun doubleTapLikePost(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId && !post.isLiked) {
                    post.copy(isLiked = true, likesCount = post.likesCount + 1)
                } else post
            }
            val updatedDetail = if (state.activePostDetail?.id == postId && !state.activePostDetail.isLiked) {
                state.activePostDetail.copy(isLiked = true, likesCount = state.activePostDetail.likesCount + 1)
            } else state.activePostDetail
            state.copy(posts = updated, activePostDetail = updatedDetail)
        }

        repository?.let { repo ->
            viewModelScope.launch {
                repo.togglePostLike(postId)
            }
        }
    }

    fun togglePostSave(postId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
            }
            val updatedDetail = if (state.activePostDetail?.id == postId) {
                state.activePostDetail.copy(isSaved = !state.activePostDetail.isSaved)
            } else state.activePostDetail
            state.copy(posts = updated, activePostDetail = updatedDetail)
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = UUID.randomUUID().toString(),
            username = _uiState.value.currentUser.username,
            userAvatarUrl = _uiState.value.currentUser.avatarUrl,
            text = text.trim(),
            timestamp = "Just now",
            likesCount = 0
        )
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        comments = post.comments + newComment,
                        commentsCount = post.commentsCount + 1
                    )
                } else post
            }
            val updatedDetail = if (state.activePostDetail?.id == postId) {
                state.activePostDetail.copy(
                    comments = state.activePostDetail.comments + newComment,
                    commentsCount = state.activePostDetail.commentsCount + 1
                )
            } else state.activePostDetail
            state.copy(posts = updated, activePostDetail = updatedDetail)
        }

        repository?.let { repo ->
            viewModelScope.launch {
                repo.addComment(postId, text)
            }
        }
    }

    fun toggleCommentLike(postId: String, commentId: String) {
        _uiState.update { state ->
            val updated = state.posts.map { post ->
                if (post.id == postId) {
                    val updatedComments = post.comments.map { c ->
                        if (c.id == commentId) {
                            c.copy(
                                isLiked = !c.isLiked,
                                likesCount = if (c.isLiked) c.likesCount - 1 else c.likesCount + 1
                            )
                        } else c
                    }
                    post.copy(comments = updatedComments)
                } else post
            }
            state.copy(posts = updated)
        }
    }

    fun toggleReelLike(reelId: String) {
        _uiState.update { state ->
            val updated = state.reels.map { reel ->
                if (reel.id == reelId) {
                    val wasLiked = reel.isLiked
                    reel.copy(
                        isLiked = !wasLiked,
                        likesCount = if (wasLiked) reel.likesCount - 1 else reel.likesCount + 1
                    )
                } else reel
            }
            state.copy(reels = updated)
        }
    }

    fun toggleReelSave(reelId: String) {
        _uiState.update { state ->
            val updated = state.reels.map { reel ->
                if (reel.id == reelId) reel.copy(isSaved = !reel.isSaved) else reel
            }
            state.copy(reels = updated)
        }
    }

    fun toggleReelFollow(reelId: String) {
        _uiState.update { state ->
            val updated = state.reels.map { reel ->
                if (reel.id == reelId) reel.copy(isFollowing = !reel.isFollowing) else reel
            }
            state.copy(reels = updated)
        }
    }

    fun toggleUserFollow(userId: String) {
        _uiState.update { state ->
            val updatedInspected = if (state.inspectedUser?.id == userId) {
                val u = state.inspectedUser
                u.copy(
                    isFollowing = !u.isFollowing,
                    followersCount = if (u.isFollowing) u.followersCount - 1 else u.followersCount + 1
                )
            } else state.inspectedUser

            val updatedNotifs = state.notifications.map { notif ->
                if (notif.user.id == userId) notif.copy(isFollowing = !notif.isFollowing) else notif
            }

            state.copy(
                inspectedUser = updatedInspected,
                notifications = updatedNotifs
            )
        }
    }

    fun sendChatMessage(threadId: String, text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderId = _uiState.value.currentUser.id,
            text = text.trim(),
            timestamp = "Just now",
            isMe = true
        )

        _uiState.update { state ->
            val updatedThreads = state.chatThreads.map { thread ->
                if (thread.id == threadId) {
                    thread.copy(
                        messages = thread.messages + userMsg,
                        lastMessage = userMsg.text,
                        lastMessageTime = "Now"
                    )
                } else thread
            }
            state.copy(chatThreads = updatedThreads)
        }

        viewModelScope.launch {
            delay(1200)
            val friendReplies = listOf(
                "Totally agree! That's awesome 🙌",
                "Haha wow, sounds so good!",
                "Can't wait to see more photos! ✨",
                "Let's definitely catch up soon ☕"
            )
            val replyMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = "friend",
                text = friendReplies.random(),
                timestamp = "Just now",
                isMe = false
            )
            _uiState.update { state ->
                val updatedThreads = state.chatThreads.map { thread ->
                    if (thread.id == threadId) {
                        thread.copy(
                            messages = thread.messages + replyMsg,
                            lastMessage = replyMsg.text,
                            lastMessageTime = "Now"
                        )
                    } else thread
                }
                state.copy(chatThreads = updatedThreads)
            }
        }
    }

    fun publishNewPost(caption: String, imageUrl: String, location: String) {
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            user = _uiState.value.currentUser,
            mediaUrls = listOf(imageUrl),
            caption = caption,
            likesCount = 1,
            commentsCount = 0,
            comments = emptyList(),
            isLiked = true,
            isSaved = false,
            timestamp = "JUST NOW",
            location = location.ifBlank { null }
        )
        _uiState.update { state ->
            val updatedPosts = listOf(newPost) + state.posts
            val updatedUser = state.currentUser.copy(postsCount = state.currentUser.postsCount + 1)
            state.copy(
                posts = updatedPosts,
                currentUser = updatedUser,
                isCreatePostOpen = false,
                currentTab = NavigationTab.HOME
            )
        }

        repository?.let { repo ->
            viewModelScope.launch {
                repo.createPost(caption, imageUrl, location)
            }
        }
    }

    fun updateProfile(name: String, username: String, bio: String, website: String) {
        _uiState.update { state ->
            val updatedUser = state.currentUser.copy(
                name = name,
                username = username,
                bio = bio,
                website = website
            )
            state.copy(
                currentUser = updatedUser,
                isEditProfileOpen = false
            )
        }

        repository?.let { repo ->
            viewModelScope.launch {
                repo.syncUserProfile(name, username, bio, website)
            }
        }
    }

    fun setExploreCategory(category: String) {
        _uiState.update { it.copy(selectedExploreCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
