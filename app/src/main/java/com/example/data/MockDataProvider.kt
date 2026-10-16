package com.example.data

import com.example.model.*

object MockDataProvider {

    val currentUser = User(
        id = "user_me",
        username = "alex.snaps",
        name = "Alex Morgan",
        avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
        isVerified = true,
        bio = "✨ Visual story hunter & creative technologist\n📍 SF & Tokyo\n☕ Chasing light, neon & matcha\n⬇️ Latest series & presets below",
        website = "alexmorgan.design",
        postsCount = 42,
        followersCount = 12400,
        followingCount = 482,
        hasStory = true,
        hasUnseenStory = false
    )

    val sampleUsers = listOf(
        User(
            id = "user_1",
            username = "elena_wanderlust",
            name = "Elena Rostova",
            avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&q=80",
            isVerified = true,
            bio = "Mountains, sunsets and coffee ⛰️✈️",
            hasStory = true,
            hasUnseenStory = true
        ),
        User(
            id = "user_2",
            username = "neon_streets",
            name = "Kenji Sato",
            avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80",
            isVerified = false,
            bio = "Cyberpunk tones & Tokyo night walks 🏮",
            hasStory = true,
            hasUnseenStory = true
        ),
        User(
            id = "user_3",
            username = "chloe.arch",
            name = "Chloe Dubois",
            avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&q=80",
            isVerified = true,
            bio = "Architectural lines & minimalist spaces 📐",
            hasStory = true,
            hasUnseenStory = true
        ),
        User(
            id = "user_4",
            username = "foodie_diaries",
            name = "Mateo Silva",
            avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80",
            isVerified = false,
            bio = "Culinary adventures around the globe 🍜🍕",
            hasStory = true,
            hasUnseenStory = true
        ),
        User(
            id = "user_5",
            username = "sarah_visuals",
            name = "Sarah Jenkins",
            avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&q=80",
            isVerified = true,
            bio = "Portrait photographer & color grader 🎞️",
            hasStory = true,
            hasUnseenStory = false
        )
    )

    val sampleStories = listOf(
        Story(
            id = "story_me",
            user = currentUser,
            mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&q=80",
            caption = "Golden hour by the shore ✨🌊",
            timestamp = "1h",
            isSeen = false
        ),
        Story(
            id = "story_1",
            user = sampleUsers[0],
            mediaUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&q=80",
            caption = "Alpine sunrise at 3,000 meters 🏔️☕",
            timestamp = "2h",
            isSeen = false
        ),
        Story(
            id = "story_2",
            user = sampleUsers[1],
            mediaUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=800&q=80",
            caption = "Shinjuku in the rain, pure blade runner vibes 🌧️🏮",
            timestamp = "3h",
            isSeen = false
        ),
        Story(
            id = "story_3",
            user = sampleUsers[2],
            mediaUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=800&q=80",
            caption = "Geometry & afternoon shadows in Copenhagen 🏢",
            timestamp = "5h",
            isSeen = false
        ),
        Story(
            id = "story_4",
            user = sampleUsers[3],
            mediaUrl = "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=800&q=80",
            caption = "Fresh wood-fired sourdough pizza slice 🍕🔥",
            timestamp = "7h",
            isSeen = false
        )
    )

    val sampleHighlights = listOf(
        StoryHighlight(id = "hl_1", title = "Tokyo 🇯🇵", coverUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=400&q=80"),
        StoryHighlight(id = "hl_2", title = "Travel ✈️", coverUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=400&q=80"),
        StoryHighlight(id = "hl_3", title = "Coffee ☕", coverUrl = "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=400&q=80"),
        StoryHighlight(id = "hl_4", title = "Portraits 📸", coverUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80"),
        StoryHighlight(id = "hl_5", title = "Studio 🎨", coverUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400&q=80")
    )

    val samplePosts = listOf(
        Post(
            id = "post_1",
            user = sampleUsers[0],
            mediaUrls = listOf("https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1080&q=80"),
            caption = "Woke up above the cloud line this morning. Some places remind you just how vast the world is and how quiet your mind can become. 🌲✨ #wanderlust #alpinelife #morningvibes #landscape",
            likesCount = 3842,
            commentsCount = 128,
            comments = listOf(
                Comment(id = "c1", username = "neon_streets", userAvatarUrl = sampleUsers[1].avatarUrl, text = "The light hitting the valley is perfection!", timestamp = "2h", likesCount = 14),
                Comment(id = "c2", username = "chloe.arch", userAvatarUrl = sampleUsers[2].avatarUrl, text = "Need to pack my bags right now ✈️", timestamp = "1h", likesCount = 8),
                Comment(id = "c3", username = "alex.snaps", userAvatarUrl = currentUser.avatarUrl, text = "Breathtaking tones Elena! What lens did you shoot this on?", timestamp = "30m", likesCount = 4)
            ),
            isLiked = false,
            isSaved = false,
            timestamp = "3 HOURS AGO",
            location = "Dolomites, Italy",
            audioTrack = "Ludovico Einaudi • Nuvole Bianche"
        ),
        Post(
            id = "post_2",
            user = sampleUsers[1],
            mediaUrls = listOf("https://images.unsplash.com/photo-1514565131-fce0801e5785?w=1080&q=80"),
            caption = "Electric dreams under rain-soaked Shibuya neon. Reflection hunting is unmatched at 2 AM. 🌧️🔮 #cyberpunk #tokyonights #streetphotography #neonvibes",
            likesCount = 5910,
            commentsCount = 245,
            comments = listOf(
                Comment(id = "c4", username = "sarah_visuals", userAvatarUrl = sampleUsers[4].avatarUrl, text = "Insane reflections Kenji 🔥", timestamp = "4h", likesCount = 32),
                Comment(id = "c5", username = "foodie_diaries", userAvatarUrl = sampleUsers[3].avatarUrl, text = "Followed by late night ramen right? 🍜", timestamp = "3h", likesCount = 12)
            ),
            isLiked = true,
            isSaved = true,
            timestamp = "5 HOURS AGO",
            location = "Shibuya, Tokyo",
            audioTrack = "Kavinsky • Nightcall"
        ),
        Post(
            id = "post_3",
            user = currentUser,
            mediaUrls = listOf("https://images.unsplash.com/photo-1513694203232-719a280e022f?w=1080&q=80"),
            caption = "Minimalist angles and warm afternoon shadows. Design is about what you leave out, not what you pack in. 📐🤍 #minimalism #architecture #nordicdesign #creativestudio",
            likesCount = 2194,
            commentsCount = 64,
            comments = listOf(
                Comment(id = "c6", username = "chloe.arch", userAvatarUrl = sampleUsers[2].avatarUrl, text = "Those clean lines are so satisfying!", timestamp = "6h", likesCount = 19)
            ),
            isLiked = false,
            isSaved = false,
            timestamp = "1 DAY AGO",
            location = "San Francisco, California",
            audioTrack = "Tycho • Awake"
        ),
        Post(
            id = "post_4",
            user = sampleUsers[3],
            mediaUrls = listOf("https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=1080&q=80"),
            caption = "Handcrafted tonkotsu ramen with marinated egg and black garlic oil. Comfort food for the soul. 🍜✨ #foodstagram #ramenlover #tokyoeats #comfortfood",
            likesCount = 4120,
            commentsCount = 189,
            comments = listOf(
                Comment(id = "c7", username = "elena_wanderlust", userAvatarUrl = sampleUsers[0].avatarUrl, text = "I am officially hungry now 🤤", timestamp = "1d", likesCount = 15)
            ),
            isLiked = false,
            isSaved = false,
            timestamp = "2 DAYS AGO",
            location = "Ramen Alley, Shinjuku",
            audioTrack = "Lofi Fruits • Rainy Coffee"
        )
    )

    val sampleReels = listOf(
        Reel(
            id = "reel_1",
            user = sampleUsers[0],
            videoThumbnailUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=1080&q=80",
            caption = "POV: You hiked 5 hours for this view and forgot all your exhaustion 🏔️🎒 #hiking #alps #mountains #freedom",
            audioTitle = "Original Audio • elena_wanderlust",
            likesCount = 48200,
            commentsCount = 890,
            sharesCount = 12400,
            isLiked = true,
            isFollowing = true
        ),
        Reel(
            id = "reel_2",
            user = sampleUsers[1],
            videoThumbnailUrl = "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=1080&q=80",
            caption = "Tokyo after midnight hits different with rain reflections 🌧️✨ #tokyo #nightwalk #cinematic",
            audioTitle = "Synthwave Memories • Cyberboy",
            likesCount = 89400,
            commentsCount = 1430,
            sharesCount = 31200,
            isLiked = false,
            isFollowing = false
        ),
        Reel(
            id = "reel_3",
            user = sampleUsers[4],
            videoThumbnailUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=1080&q=80",
            caption = "3 tips for better natural lighting portraits on mobile! 📸✨ Save this for later!",
            audioTitle = "Trending Beats • Studio Vibe",
            likesCount = 112000,
            commentsCount = 2100,
            sharesCount = 45000,
            isLiked = false,
            isFollowing = true
        )
    )

    val exploreCategories = listOf(
        "For You",
        "Photography",
        "Architecture",
        "Travel",
        "Fashion",
        "Nature",
        "Food",
        "Design",
        "Art"
    )

    val exploreGridItems = listOf(
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&q=80",
        "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600&q=80",
        "https://images.unsplash.com/photo-1514565131-fce0801e5785?w=600&q=80",
        "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=600&q=80",
        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600&q=80",
        "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=600&q=80",
        "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=600&q=80",
        "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=600&q=80",
        "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=600&q=80",
        "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=600&q=80",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=600&q=80",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=600&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&q=80",
        "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=600&q=80",
        "https://images.unsplash.com/photo-1532274402911-5a369e4c4bb5?w=600&q=80"
    )

    val sampleNotes = listOf(
        UserNote(id = "n_me", user = currentUser, noteText = "Listening to Tycho 🎧"),
        UserNote(id = "n_1", user = sampleUsers[0], noteText = "Summit conquered! ⛰️"),
        UserNote(id = "n_2", user = sampleUsers[1], noteText = "Neon editing session 🔮"),
        UserNote(id = "n_3", user = sampleUsers[2], noteText = "Coffee time ☕")
    )

    val sampleChatThreads = listOf(
        ChatThread(
            id = "chat_1",
            user = sampleUsers[0],
            lastMessage = "Thanks for the feedback! See you at the photo walk this weekend 📷",
            lastMessageTime = "12m",
            unreadCount = 2,
            isOnline = true,
            messages = listOf(
                ChatMessage(id = "m1", senderId = sampleUsers[0].id, text = "Hey Alex! Loved your latest architecture post!", timestamp = "10:14 AM", isMe = false),
                ChatMessage(id = "m2", senderId = currentUser.id, text = "Thanks Elena! That morning light was unreal.", timestamp = "10:20 AM", isMe = true),
                ChatMessage(id = "m3", senderId = sampleUsers[0].id, text = "Thanks for the feedback! See you at the photo walk this weekend 📷", timestamp = "10:22 AM", isMe = false)
            )
        ),
        ChatThread(
            id = "chat_2",
            user = sampleUsers[1],
            lastMessage = "Sent a reel: Tokyo Neon Rain 🌧️",
            lastMessageTime = "1h",
            unreadCount = 0,
            isOnline = false,
            messages = listOf(
                ChatMessage(id = "m4", senderId = sampleUsers[1].id, text = "Yo, check out this spot in Shibuya!", timestamp = "9:00 AM", isMe = false),
                ChatMessage(id = "m5", senderId = currentUser.id, text = "That reflection angle is sick! What ISO?", timestamp = "9:15 AM", isMe = true),
                ChatMessage(id = "m6", senderId = sampleUsers[1].id, text = "Sent a reel: Tokyo Neon Rain 🌧️", timestamp = "9:20 AM", isMe = false)
            )
        ),
        ChatThread(
            id = "chat_3",
            user = sampleUsers[2],
            lastMessage = "Can you share the preset you used?",
            lastMessageTime = "3h",
            unreadCount = 0,
            isOnline = true,
            messages = listOf(
                ChatMessage(id = "m7", senderId = sampleUsers[2].id, text = "Can you share the preset you used?", timestamp = "7:45 AM", isMe = false)
            )
        ),
        ChatThread(
            id = "chat_4",
            user = sampleUsers[4],
            lastMessage = "Reacted ❤️ to your story",
            lastMessageTime = "1d",
            unreadCount = 0,
            isOnline = false,
            messages = listOf(
                ChatMessage(id = "m8", senderId = sampleUsers[4].id, text = "Reacted ❤️ to your story", timestamp = "Yesterday", isMe = false)
            )
        )
    )

    val sampleNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            user = sampleUsers[0],
            type = NotificationType.LIKE,
            message = "liked your photo.",
            timeAgo = "15m",
            postThumbnail = samplePosts[2].mediaUrls.first()
        ),
        NotificationItem(
            id = "notif_2",
            user = sampleUsers[1],
            type = NotificationType.COMMENT,
            message = "commented: 'Those clean lines are so satisfying!'",
            timeAgo = "1h",
            postThumbnail = samplePosts[2].mediaUrls.first()
        ),
        NotificationItem(
            id = "notif_3",
            user = sampleUsers[4],
            type = NotificationType.FOLLOW,
            message = "started following you.",
            timeAgo = "3h",
            isFollowing = true
        ),
        NotificationItem(
            id = "notif_4",
            user = sampleUsers[2],
            type = NotificationType.LIKE,
            message = "and 43 others liked your story.",
            timeAgo = "5h",
            postThumbnail = sampleStories[0].mediaUrl
        )
    )
}
