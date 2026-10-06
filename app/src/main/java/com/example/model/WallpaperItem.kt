package com.example.model

import androidx.compose.ui.graphics.Color

data class WallpaperItem(
    val id: Int,
    val name: String,
    val url: String,
    val fallbackColors: List<Color>
)

object Wallpapers {
    val items = listOf(
        WallpaperItem(
            id = 0,
            name = "Floral Bokeh",
            url = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF2E384D), Color(0xFF1B2332))
        ),
        WallpaperItem(
            id = 1,
            name = "Sunlit Forest",
            url = "https://images.unsplash.com/photo-1513836279014-a89f7a76ae86?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF1B3B2B), Color(0xFF0F2319))
        ),
        WallpaperItem(
            id = 2,
            name = "Teal Gradient",
            url = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF007991), Color(0xFF78FFD6))
        ),
        WallpaperItem(
            id = 3,
            name = "Geometric Fluid",
            url = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF1F1C2C), Color(0xFF928DAB))
        ),
        WallpaperItem(
            id = 4,
            name = "Red Sunburst",
            url = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
        ),
        WallpaperItem(
            id = 5,
            name = "Purple Sunset",
            url = "https://images.unsplash.com/photo-1579546929662-711aa81148cf?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF4A0E4E), Color(0xFF2C0B30))
        ),
        WallpaperItem(
            id = 6,
            name = "Soft Beach Ocean",
            url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF2193B0), Color(0xFF6DD5ED))
        ),
        WallpaperItem(
            id = 7,
            name = "Pastel Pink Bokeh",
            url = "https://images.unsplash.com/photo-1522383225653-ed111181a951?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFFDE6262), Color(0xFFFFB88C))
        ),
        WallpaperItem(
            id = 8,
            name = "Deep Space Galaxy",
            url = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
        ),
        WallpaperItem(
            id = 9,
            name = "Misty Mountain Peaks",
            url = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF3E5151), Color(0xFFDECBA4))
        ),
        WallpaperItem(
            id = 10,
            name = "Cyberpunk Neon",
            url = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF141E30), Color(0xFF243B55))
        ),
        WallpaperItem(
            id = 11,
            name = "Mountain Valley River",
            url = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF2C3E50), Color(0xFF3498DB))
        ),
        WallpaperItem(
            id = 12,
            name = "Aurora Northern Lights",
            url = "https://images.unsplash.com/photo-1534447677768-be436bb09401?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF0B486B), Color(0xFFF56217))
        ),
        WallpaperItem(
            id = 13,
            name = "Starry Night Sky",
            url = "https://images.unsplash.com/photo-1519681393784-d120267933ba?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF141E30), Color(0xFF243B55))
        ),
        WallpaperItem(
            id = 14,
            name = "Golden Sunlight Leaves",
            url = "https://images.unsplash.com/photo-1518495973542-4542c06a5843?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFFF3904F), Color(0xFF3B4371))
        ),
        WallpaperItem(
            id = 15,
            name = "Minimal Green Leaves",
            url = "https://images.unsplash.com/photo-1502082553048-f009c37129b9?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF134E5E), Color(0xFF71B280))
        ),
        WallpaperItem(
            id = 16,
            name = "Nature Fog Morning",
            url = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF485563), Color(0xFF29323C))
        ),
        WallpaperItem(
            id = 17,
            name = "Cherry Blossom Sakura",
            url = "https://images.unsplash.com/photo-1516617442634-75371039cb3a?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFFFFB199), Color(0xFFFF0844))
        ),
        WallpaperItem(
            id = 18,
            name = "Blue Calm Ocean Wave",
            url = "https://images.unsplash.com/photo-1518837695005-2083093ee35b?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFF36D1DC), Color(0xFF5B86E5))
        ),
        WallpaperItem(
            id = 19,
            name = "Minimalist Desert Dunes",
            url = "https://images.unsplash.com/photo-1531685250784-7569952593d2?q=75&w=900&auto=format&fit=crop",
            fallbackColors = listOf(Color(0xFFBA8B02), Color(0xFF181818))
        )
    )
}
