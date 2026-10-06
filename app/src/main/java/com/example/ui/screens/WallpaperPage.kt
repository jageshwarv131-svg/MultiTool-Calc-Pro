package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.Wallpapers

@Composable
fun WallpaperPage(
    initialIndex: Int,
    isDark: Boolean,
    onWallpaperSelected: (Int) -> Unit,
    onClose: () -> Unit
) {
    // Intercept back button to close the wallpaper page
    BackHandler {
        onClose()
    }

    var selectedIndex by remember { mutableIntStateOf(initialIndex) }
    val context = LocalContext.current

    val pageBg = if (isDark) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val titleColor = if (isDark) Color.White else Color(0xFF1E293B)
    val closeColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header matching HTML (.wallpaper-header)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Close Button on the left
            Text(
                text = "✕",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                color = closeColor,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable(onClick = onClose)
                    .padding(4.dp)
                    .testTag("wallpaper_close_btn")
            )

            // Centered Title "wallpaper"
            Text(
                text = "wallpaper",
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp,
                color = titleColor,
                modifier = Modifier.testTag("wallpaper_title")
            )
        }

        // 3-Column Wallpaper Grid matching HTML (.wallpaper-grid)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(2.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(Wallpapers.items, key = { it.id }) { item ->
                val isSelected = item.id == selectedIndex
                val thumbShape = RoundedCornerShape(6.dp)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .shadow(elevation = 2.dp, shape = thumbShape)
                        .clip(thumbShape)
                        .then(
                            if (isSelected) {
                                Modifier.border(BorderStroke(3.dp, Color(0xFF06B6D4)), thumbShape)
                            } else {
                                Modifier
                            }
                        )
                        .clickable { selectedIndex = item.id }
                        .testTag("wallpaper_thumb_${item.id}")
                ) {
                    // Fallback gradient while loading or offline
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(item.fallbackColors))
                    )

                    // Image thumbnail
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(item.url)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Footer with SET Button matching HTML (.btn-set-wallpaper)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(30.dp),
                        spotColor = Color(0x6606B6D4)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF38BDF8), Color(0xFF06B6D4))
                        )
                    )
                    .clickable {
                        onWallpaperSelected(selectedIndex)
                        onClose()
                    }
                    .padding(horizontal = 38.dp, vertical = 12.dp)
                    .testTag("btn_set_wallpaper"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SET",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = Color.White
                )
            }
        }
    }
}
