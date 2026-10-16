package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.auth.AuthManager
import com.example.data.FirestoreRepository
import com.example.model.NavigationTab
import com.example.ui.components.CommentsBottomSheet
import com.example.ui.components.InstaBottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.InstaViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private val viewModel: InstaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = FirestoreRepository(this)

        setContent {
            MyApplicationTheme {
                var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

                DisposableEffect(Unit) {
                    val listener = FirebaseAuth.AuthStateListener { auth ->
                        currentUser = auth.currentUser
                        if (auth.currentUser != null) {
                            viewModel.bindRepository(repository, auth.currentUser)
                        }
                    }
                    Firebase.auth.addAuthStateListener(listener)
                    onDispose {
                        Firebase.auth.removeAuthStateListener(listener)
                    }
                }

                if (currentUser == null) {
                    SignInScreen(
                        onAuthSuccess = {
                            currentUser = Firebase.auth.currentUser
                            if (currentUser != null) {
                                viewModel.bindRepository(repository, currentUser)
                            }
                        }
                    )
                } else {
                    com.example.ui.theme.LiquidAmbientBackground {
                        InstaApp(
                            viewModel = viewModel,
                            onSignOut = {
                                AuthManager.signOut(
                                    context = this@MainActivity,
                                    credentialManager = CredentialManager.create(this@MainActivity),
                                    onSignOutComplete = {
                                        currentUser = null
                                    },
                                    scope = lifecycleScope
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InstaApp(
    viewModel: InstaViewModel,
    onSignOut: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Comments Sheet
    val activeCommentsPost = uiState.posts.find { it.id == uiState.activeCommentsPostId }
        ?: if (uiState.activePostDetail?.id == uiState.activeCommentsPostId) uiState.activePostDetail else null
    if (activeCommentsPost != null) {
        CommentsBottomSheet(
            post = activeCommentsPost,
            currentUser = uiState.currentUser,
            onDismiss = { viewModel.closeComments() },
            onAddComment = { text -> viewModel.addComment(activeCommentsPost.id, text) },
            onToggleCommentLike = { commentId -> viewModel.toggleCommentLike(activeCommentsPost.id, commentId) }
        )
    }

    // Create Post Full Screen Overlay
    if (uiState.isCreatePostOpen) {
        CreatePostScreen(
            currentUser = uiState.currentUser,
            onClose = { viewModel.closeCreatePost() },
            onPublish = { caption, imageUrl, location ->
                viewModel.publishNewPost(caption, imageUrl, location)
                Toast.makeText(context, "Post shared successfully! ✨", Toast.LENGTH_SHORT).show()
            }
        )
        return
    }

    // Edit Profile Modal
    if (uiState.isEditProfileOpen) {
        EditProfileDialog(
            user = uiState.currentUser,
            onDismiss = { viewModel.closeEditProfile() },
            onSave = { name, username, bio, website ->
                viewModel.updateProfile(name, username, bio, website)
                Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Story Viewer Full Screen Overlay
    if (uiState.activeStoryIndex != null) {
        StoryViewerScreen(
            stories = uiState.stories,
            currentStoryIndex = uiState.activeStoryIndex!!,
            onNextStory = { viewModel.nextStory() },
            onPreviousStory = { viewModel.previousStory() },
            onClose = { viewModel.closeStory() }
        )
        return
    }

    // Direct Messages Screen
    if (uiState.isDirectMessagesOpen) {
        DirectMessagesScreen(
            currentUser = uiState.currentUser,
            threads = uiState.chatThreads,
            activeChatId = uiState.activeChatId,
            onOpenThread = { threadId -> viewModel.openChat(threadId) },
            onCloseChat = { viewModel.closeChat() },
            onBack = { viewModel.closeDirectMessages() },
            onSendMessage = { threadId, text -> viewModel.sendChatMessage(threadId, text) }
        )
        return
    }

    // Notifications Screen
    if (uiState.isNotificationsOpen) {
        NotificationsScreen(
            notifications = uiState.notifications,
            onFollowToggle = { userId -> viewModel.toggleUserFollow(userId) },
            onBack = { viewModel.closeNotifications() }
        )
        return
    }

    // Post Detail Screen
    if (uiState.activePostDetail != null) {
        PostDetailScreen(
            post = uiState.activePostDetail!!,
            onBack = { viewModel.closePostDetail() },
            onLikeClick = { viewModel.togglePostLike(uiState.activePostDetail!!.id) },
            onDoubleTapLike = { viewModel.doubleTapLikePost(uiState.activePostDetail!!.id) },
            onSaveClick = { viewModel.togglePostSave(uiState.activePostDetail!!.id) },
            onCommentClick = { viewModel.openComments(uiState.activePostDetail!!.id) },
            onShareClick = {
                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            },
            onUserClick = { user -> viewModel.inspectUser(user) }
        )
        return
    }

    // Other User Inspected Profile Screen
    if (uiState.inspectedUser != null) {
        BackHandler { viewModel.closeInspectedUser() }
        val inspected = uiState.inspectedUser!!
        val userPosts = uiState.posts.filter { it.user.id == inspected.id }
        ProfileScreen(
            user = inspected,
            isCurrentUser = false,
            userPosts = userPosts.ifEmpty { uiState.posts },
            highlights = uiState.highlights,
            onEditProfileClick = {},
            onFollowToggle = { viewModel.toggleUserFollow(inspected.id) },
            onPostClick = { post -> viewModel.openPostDetail(post) },
            onBackClick = { viewModel.closeInspectedUser() }
        )
        return
    }

    // Base Scaffold with Bottom Navigation
    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            InstaBottomNavBar(
                currentTab = uiState.currentTab,
                currentUser = uiState.currentUser,
                onTabSelected = { tab -> viewModel.selectTab(tab) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    NavigationTab.HOME -> {
                        HomeScreen(
                            posts = uiState.posts,
                            stories = uiState.stories,
                            currentUser = uiState.currentUser,
                            unreadMessagesCount = uiState.chatThreads.sumOf { it.unreadCount },
                            unreadNotificationsCount = 2,
                            onStoryClick = { index -> viewModel.openStory(index) },
                            onLikePost = { postId -> viewModel.togglePostLike(postId) },
                            onDoubleTapLikePost = { postId -> viewModel.doubleTapLikePost(postId) },
                            onSavePost = { postId -> viewModel.togglePostSave(postId) },
                            onCommentClick = { postId -> viewModel.openComments(postId) },
                            onSharePost = {
                                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            onUserClick = { user ->
                                if (user.id == uiState.currentUser.id) {
                                    viewModel.selectTab(NavigationTab.PROFILE)
                                } else {
                                    viewModel.inspectUser(user)
                                }
                            },
                            onOpenDirectMessages = { viewModel.openDirectMessages() },
                            onOpenNotifications = { viewModel.openNotifications() }
                        )
                    }

                    NavigationTab.EXPLORE -> {
                        ExploreScreen(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
                            selectedCategory = uiState.selectedExploreCategory,
                            onCategorySelect = { category -> viewModel.setExploreCategory(category) },
                            gridImages = uiState.exploreGrid,
                            onMediaClick = { post -> viewModel.openPostDetail(post) }
                        )
                    }

                    NavigationTab.CREATE -> {
                        // Managed by state overlay
                    }

                    NavigationTab.REELS -> {
                        ReelsScreen(
                            reels = uiState.reels,
                            onLikeClick = { reelId -> viewModel.toggleReelLike(reelId) },
                            onSaveClick = { reelId -> viewModel.toggleReelSave(reelId) },
                            onFollowClick = { reelId -> viewModel.toggleReelFollow(reelId) },
                            onCommentClick = { /* open reel comments */ }
                        )
                    }

                    NavigationTab.PROFILE -> {
                        val myPosts = uiState.posts.filter { it.user.id == uiState.currentUser.id }
                        ProfileScreen(
                            user = uiState.currentUser,
                            isCurrentUser = true,
                            userPosts = myPosts.ifEmpty { uiState.posts },
                            highlights = uiState.highlights,
                            onEditProfileClick = { viewModel.openEditProfile() },
                            onFollowToggle = {},
                            onPostClick = { post -> viewModel.openPostDetail(post) },
                            onSignOut = onSignOut
                        )
                    }
                }
            }
        }
    }
}
