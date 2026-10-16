package com.example

import com.example.model.NavigationTab
import com.example.viewmodel.InstaViewModel
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testInitialFeedAndLike() {
        val viewModel = InstaViewModel()
        val initialPosts = viewModel.uiState.value.posts
        assertTrue("Posts should not be empty", initialPosts.isNotEmpty())

        val firstPost = initialPosts[0]
        val initialLikes = firstPost.likesCount
        val initialLikedState = firstPost.isLiked

        viewModel.togglePostLike(firstPost.id)
        val updatedPost = viewModel.uiState.value.posts.first { it.id == firstPost.id }

        assertEquals(!initialLikedState, updatedPost.isLiked)
        val expectedLikes = if (initialLikedState) initialLikes - 1 else initialLikes + 1
        assertEquals(expectedLikes, updatedPost.likesCount)
    }

    @Test
    fun testPublishPost() {
        val viewModel = InstaViewModel()
        val initialCount = viewModel.uiState.value.posts.size

        viewModel.publishNewPost("Testing new sunset photo! #goldenhour", "https://example.com/test.jpg", "California")
        val updatedPosts = viewModel.uiState.value.posts

        assertEquals(initialCount + 1, updatedPosts.size)
        assertEquals("Testing new sunset photo! #goldenhour", updatedPosts[0].caption)
        assertEquals(NavigationTab.HOME, viewModel.uiState.value.currentTab)
    }

    @Test
    fun testAddComment() {
        val viewModel = InstaViewModel()
        val post = viewModel.uiState.value.posts[0]
        val initialCommentCount = post.commentsCount

        viewModel.addComment(post.id, "Awesome picture! 🔥")
        val updatedPost = viewModel.uiState.value.posts.first { it.id == post.id }

        assertEquals(initialCommentCount + 1, updatedPost.commentsCount)
        assertTrue(updatedPost.comments.any { it.text == "Awesome picture! 🔥" })
    }

    @Test
    fun testTabSelection() {
        val viewModel = InstaViewModel()
        viewModel.selectTab(NavigationTab.EXPLORE)
        assertEquals(NavigationTab.EXPLORE, viewModel.uiState.value.currentTab)

        viewModel.selectTab(NavigationTab.PROFILE)
        assertEquals(NavigationTab.PROFILE, viewModel.uiState.value.currentTab)
    }
}
