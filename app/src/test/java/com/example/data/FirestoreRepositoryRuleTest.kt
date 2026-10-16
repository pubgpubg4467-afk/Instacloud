package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FirestoreRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun authenticatedUser_canCreateAndObservePost() = runBlocking {
        val uid = signInTestUser("user_creator@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val result = repository.createPost(
            caption = "Test caption from Robolectric",
            imageUrl = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=800",
            location = "Tokyo, Japan"
        )

        assertTrue("Post creation should succeed", result.isSuccess)
        val postId = result.getOrNull()
        assertNotNull(postId)

        val posts = repository.observePosts().first()
        val createdPost = posts.find { it.id == postId }
        assertNotNull("Created post should be present in flow", createdPost)
        assertEquals("Test caption from Robolectric", createdPost?.caption)
    }

    @Test
    fun authenticatedUser_canToggleLikeAndAddComment() = runBlocking {
        val uid = signInTestUser("user_interactor@test.com")
        val repository = FirestoreRepository(firestore, auth)

        val createResult = repository.createPost(
            caption = "Post for interaction",
            imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800",
            location = "Paris"
        )
        val postId = createResult.getOrThrow()

        // Like the post
        val likeResult = repository.togglePostLike(postId)
        assertTrue(likeResult.isSuccess)
        assertTrue(likeResult.getOrThrow()) // true = liked

        // Add a comment
        val commentResult = repository.addComment(postId, "Awesome shot!")
        assertTrue(commentResult.isSuccess)
        val commentId = commentResult.getOrNull()
        assertNotNull(commentId)
    }

    @Test(expected = IllegalStateException::class)
    fun unauthenticatedUser_cannotCreatePost() = runBlocking {
        auth.signOut()
        val repository = FirestoreRepository(firestore, auth)
        repository.createPost("Unauthenticated", "https://example.com/img.jpg", "")
        Unit
    }
}
