package com.example.data

import android.content.Context
import com.example.R
import com.example.model.Comment
import com.example.model.Post
import com.example.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        Firebase.auth
    )

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observePosts(): Flow<List<Post>> = flow {
        val path = "posts"
        val query = db.collection(path).orderBy("createdAt", Query.Direction.DESCENDING)
        emitAll(
            query.snapshots()
                .map { snapshot ->
                    snapshot.documents.mapNotNull { doc ->
                        val entity = doc.toObject(FirestorePost::class.java) ?: return@mapNotNull null
                        val id = doc.id
                        Post(
                            id = id,
                            user = User(
                                id = entity.userId,
                                username = entity.username.ifBlank { "creator" },
                                name = entity.username.ifBlank { "Creator" },
                                avatarUrl = entity.userAvatarUrl.ifBlank {
                                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400"
                                }
                            ),
                            mediaUrls = if (entity.mediaUrls.isNotEmpty()) entity.mediaUrls else listOf("https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=800"),
                            caption = entity.caption,
                            likesCount = entity.likesCount.toInt(),
                            commentsCount = entity.commentsCount.toInt(),
                            location = entity.location.ifBlank { null },
                            timestamp = "Just now"
                        )
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun createPost(
        caption: String,
        imageUrl: String,
        location: String
    ): Result<String> {
        val uid = requireUserId()
        val user = auth.currentUser
        val username = user?.displayName ?: (user?.email?.substringBefore("@") ?: "user_${uid.take(4)}")
        val avatarUrl = user?.photoUrl?.toString()
            ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400"
        val postId = UUID.randomUUID().toString()
        val path = "posts/$postId"

        val payload = hashMapOf<String, Any>(
            "id" to postId,
            "userId" to uid,
            "username" to username,
            "userAvatarUrl" to avatarUrl,
            "mediaUrls" to listOf(imageUrl),
            "caption" to caption,
            "likesCount" to 0L,
            "commentsCount" to 0L,
            "location" to location,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        return try {
            db.collection("posts").document(postId).set(payload).await()
            // Increment user's post count
            db.collection("users").document(uid).update("postsCount", FieldValue.increment(1))
            Result.success(postId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun togglePostLike(postId: String): Result<Boolean> {
        val uid = requireUserId()
        val likeRef = db.collection("posts").document(postId).collection("likes").document(uid)
        val postRef = db.collection("posts").document(postId)

        return try {
            val likeDoc = likeRef.get().await()
            if (likeDoc.exists()) {
                likeRef.delete().await()
                postRef.update(
                    mapOf(
                        "likesCount" to FieldValue.increment(-1),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
                Result.success(false)
            } else {
                likeRef.set(
                    mapOf(
                        "userId" to uid,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                ).await()
                postRef.update(
                    mapOf(
                        "likesCount" to FieldValue.increment(1),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
                Result.success(true)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, "posts/$postId/likes/$uid")
            Result.failure(e)
        }
    }

    fun observeUserLikedPosts(postId: String): Flow<Boolean> = flow {
        val uid = requireUserId()
        val path = "posts/$postId/likes/$uid"
        emitAll(
            db.collection("posts").document(postId).collection("likes").document(uid)
                .snapshots()
                .map { it.exists() }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
                    emit(false)
                }
        )
    }

    fun observeComments(postId: String): Flow<List<Comment>> = flow {
        val path = "posts/$postId/comments"
        emitAll(
            db.collection("posts").document(postId).collection("comments")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .snapshots()
                .map { snapshot ->
                    snapshot.documents.mapNotNull { doc ->
                        val item = doc.toObject(FirestoreComment::class.java) ?: return@mapNotNull null
                        Comment(
                            id = doc.id,
                            username = item.username.ifBlank { "user" },
                            userAvatarUrl = item.userAvatarUrl.ifBlank {
                                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400"
                            },
                            text = item.text,
                            timestamp = "Just now",
                            likesCount = item.likesCount.toInt()
                        )
                    }
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.LIST, path)
                    throw error
                }
        )
    }

    suspend fun addComment(postId: String, text: String): Result<String> {
        val uid = requireUserId()
        val user = auth.currentUser
        val username = user?.displayName ?: (user?.email?.substringBefore("@") ?: "user")
        val avatarUrl = user?.photoUrl?.toString()
            ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400"
        val commentId = UUID.randomUUID().toString()
        val path = "posts/$postId/comments/$commentId"

        val payload = hashMapOf<String, Any>(
            "id" to commentId,
            "userId" to uid,
            "username" to username,
            "userAvatarUrl" to avatarUrl,
            "text" to text,
            "likesCount" to 0L,
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            db.collection("posts").document(postId).collection("comments").document(commentId)
                .set(payload).await()
            db.collection("posts").document(postId).update(
                mapOf(
                    "commentsCount" to FieldValue.increment(1),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(commentId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun syncUserProfile(name: String, username: String, bio: String, website: String): Result<Unit> {
        val uid = requireUserId()
        val user = auth.currentUser
        val avatarUrl = user?.photoUrl?.toString()
            ?: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400"
        val path = "users/$uid"

        val payload = hashMapOf<String, Any>(
            "id" to uid,
            "username" to username,
            "name" to name,
            "avatarUrl" to avatarUrl,
            "bio" to bio,
            "website" to website,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        return try {
            val userRef = db.collection("users").document(uid)
            val existing = userRef.get().await()
            if (!existing.exists()) {
                payload["postsCount"] = 0L
                payload["followersCount"] = 0L
                payload["followingCount"] = 0L
                payload["isVerified"] = false
                payload["createdAt"] = FieldValue.serverTimestamp()
                userRef.set(payload).await()
            } else {
                userRef.update(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    fun observeUserProfile(userId: String): Flow<User?> = flow {
        val path = "users/$userId"
        emitAll(
            db.collection("users").document(userId)
                .snapshots()
                .map { snapshot ->
                    if (!snapshot.exists()) return@map null
                    val entity = snapshot.toObject(FirestoreUser::class.java) ?: return@map null
                    User(
                        id = snapshot.id,
                        username = entity.username,
                        name = entity.name,
                        avatarUrl = entity.avatarUrl,
                        bio = entity.bio,
                        website = entity.website,
                        postsCount = entity.postsCount.toInt(),
                        followersCount = entity.followersCount.toInt(),
                        followingCount = entity.followingCount.toInt(),
                        isVerified = entity.isVerified
                    )
                }
                .catch { error ->
                    if (error is Exception) handleFirestoreError(error, OperationType.GET, path)
                    emit(null)
                }
        )
    }
}
