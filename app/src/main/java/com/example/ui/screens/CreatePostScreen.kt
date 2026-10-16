package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.User
import com.example.ui.theme.*

data class FilterOption(val name: String, val colorMatrix: ColorMatrix?)

@Composable
fun CreatePostScreen(
    currentUser: User,
    onClose: () -> Unit,
    onPublish: (caption: String, imageUrl: String, location: String) -> Unit
) {
    BackHandler { onClose() }

    val samplePhotos = listOf(
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1080&q=80",
        "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=1080&q=80",
        "https://images.unsplash.com/photo-1532274402911-5a369e4c4bb5?w=1080&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1080&q=80",
        "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=1080&q=80",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=1080&q=80"
    )

    var selectedImageUrl by remember { mutableStateOf(samplePhotos[0]) }
    var caption by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val filters = listOf(
        FilterOption("Normal", null),
        FilterOption("Clarendon", ColorMatrix().apply { setToSaturation(1.3f) }),
        FilterOption("Warm", ColorMatrix(floatArrayOf(
            1.2f, 0f, 0f, 0f, 0f,
            0f, 1.05f, 0f, 0f, 0f,
            0f, 0f, 0.85f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))),
        FilterOption("Cool", ColorMatrix(floatArrayOf(
            0.9f, 0f, 0f, 0f, 0f,
            0f, 1.0f, 0f, 0f, 0f,
            0f, 0f, 1.25f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))),
        FilterOption("B&W", ColorMatrix().apply { setToSaturation(0f) })
    )
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(LiquidDeepSpace)
            .testTag("create_post_screen")
    ) {
        // Top Navigation Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "New Post",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = { onPublish(caption, selectedImageUrl, location) },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(LiquidCyan.copy(alpha = 0.25f))
                    .border(1.dp, LiquidCyan, RoundedCornerShape(12.dp))
                    .testTag("share_post_button")
            ) {
                Text(
                    text = "Share",
                    color = LiquidCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        HorizontalDivider(color = Color(0x26FFFFFF))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Main Photo Preview with Filter applied
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .aspectRatio(1.1f)
                    .border(1.dp, LiquidGlassBorderBrush, RoundedCornerShape(20.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = selectedImageUrl,
                    contentDescription = "Selected photo",
                    contentScale = ContentScale.Crop,
                    colorFilter = selectedFilter.colorMatrix?.let { ColorFilter.colorMatrix(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Filter Selector
            Text(
                text = "Filters",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LiquidCyan,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { filter ->
                    val isSelected = filter.name == selectedFilter.name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedFilter = filter }
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 0.5.dp,
                                    color = if (isSelected) LiquidCyan else Color(0x33FFFFFF),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            AsyncImage(
                                model = selectedImageUrl,
                                contentDescription = filter.name,
                                contentScale = ContentScale.Crop,
                                colorFilter = filter.colorMatrix?.let { ColorFilter.colorMatrix(it) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = filter.name,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) LiquidCyan else Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gallery Photos Selector
            Text(
                text = "Select from Gallery",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LiquidCyan,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(samplePhotos) { photoUrl ->
                    val isSelected = photoUrl == selectedImageUrl
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isSelected) 2.5.dp else 0.5.dp,
                                color = if (isSelected) LiquidCyan else Color(0x33FFFFFF),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedImageUrl = photoUrl }
                    ) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Thumbnail",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0x26FFFFFF))

            // Caption Field in Frosted Capsule
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0x24FFFFFF))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(18.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                AsyncImage(
                    model = currentUser.avatarUrl,
                    contentDescription = "My avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0x66FFFFFF), CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = {
                        Text(
                            text = "Write a caption or add #hashtags...",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("caption_input_field")
                )
            }

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = LiquidCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    placeholder = {
                        Text(text = "Add location", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(color = Color(0x26FFFFFF))

            // Quick metadata rows
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* tag */ }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = LiquidCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Tag people", fontSize = 14.sp, color = Color.White)
            }

            HorizontalDivider(color = Color(0x26FFFFFF))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* music */ }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = LiquidCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Add music", fontSize = 14.sp, color = Color.White)
            }
        }
    }
}
