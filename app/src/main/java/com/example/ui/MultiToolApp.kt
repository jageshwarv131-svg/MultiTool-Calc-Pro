package com.example.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.PreferencesManager
import com.example.model.CalcMode
import com.example.model.CalculationRecord
import com.example.model.Wallpapers
import com.example.ui.components.GlassTopBar
import com.example.ui.dialogs.HistorySheet
import com.example.ui.screens.WallpaperPage
import com.example.ui.screens.CurrencyConverterScreen
import com.example.ui.screens.DiscountCalcScreen
import com.example.ui.screens.MassConverterScreen
import com.example.ui.screens.ScientificCalcScreen
import com.example.ui.screens.StandardCalcScreen
import com.example.ui.screens.TipCalcScreen
import kotlinx.coroutines.launch

@Composable
fun MultiToolApp() {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var currentMode by remember { mutableStateOf(CalcMode.STANDARD) }
    var wallpaperIndex by remember { mutableIntStateOf(prefs.wallpaperIndex) }
    var isDarkMode by remember { mutableStateOf(prefs.isDarkModeEnabled) }

    var showWallpaperPage by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showSettingsInDrawer by remember { mutableStateOf(false) }

    var historyItems by remember { mutableStateOf<List<CalculationRecord>>(emptyList()) }

    val activeWallpaper = remember(wallpaperIndex) {
        Wallpapers.items.getOrElse(wallpaperIndex) { Wallpapers.items[0] }
    }

    // Handle system back button: close wallpaper page, close drawer, or return to Standard Calculator
    BackHandler(enabled = showWallpaperPage || drawerState.isOpen || currentMode != CalcMode.STANDARD) {
        if (showWallpaperPage) {
            showWallpaperPage = false
        } else if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            currentMode = CalcMode.STANDARD
        }
    }

    fun openHistory() {
        val isSci = currentMode == CalcMode.SCIENTIFIC
        historyItems = prefs.getHistory(isSci)
        showHistoryDialog = true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = if (isDarkMode) Color(0xF20F172A) else Color(0xF2FFFFFF),
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.width(310.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (showSettingsInDrawer) "Settings" else "MultiTool Calc Pro",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) Color.White else Color(0xFF0F172A)
                        )
                        IconButton(onClick = { scope.launch { drawerState.close() } }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Drawer",
                                tint = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }
                    }

                    HorizontalDivider(color = if (isDarkMode) Color(0x2EFFFFFF) else Color(0x2E000000))

                    if (!showSettingsInDrawer) {
                        // Main Menu Items
                        DrawerMenuItem(
                            title = "Standard Calculator",
                            icon = Icons.Default.Calculate,
                            isSelected = currentMode == CalcMode.STANDARD,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.STANDARD
                                scope.launch { drawerState.close() }
                            }
                        )
                        DrawerMenuItem(
                            title = "Scientific Calculator",
                            icon = Icons.Default.Science,
                            isSelected = currentMode == CalcMode.SCIENTIFIC,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.SCIENTIFIC
                                scope.launch { drawerState.close() }
                            }
                        )
                        DrawerMenuItem(
                            title = "Currency Converter",
                            icon = Icons.Default.CurrencyExchange,
                            isSelected = currentMode == CalcMode.CURRENCY,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.CURRENCY
                                scope.launch { drawerState.close() }
                            }
                        )
                        DrawerMenuItem(
                            title = "Mass Converter",
                            icon = Icons.Default.Scale,
                            isSelected = currentMode == CalcMode.MASS,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.MASS
                                scope.launch { drawerState.close() }
                            }
                        )
                        DrawerMenuItem(
                            title = "Tip Calculator",
                            icon = Icons.Default.LocalAtm,
                            isSelected = currentMode == CalcMode.TIP,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.TIP
                                scope.launch { drawerState.close() }
                            }
                        )
                        DrawerMenuItem(
                            title = "Discount Calculator",
                            icon = Icons.Default.Percent,
                            isSelected = currentMode == CalcMode.DISCOUNT,
                            isDark = isDarkMode,
                            onClick = {
                                currentMode = CalcMode.DISCOUNT
                                scope.launch { drawerState.close() }
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = if (isDarkMode) Color(0x2EFFFFFF) else Color(0x2E000000)
                        )

                        // Share App
                        DrawerMenuItem(
                            title = "Share App",
                            icon = Icons.Default.Share,
                            isSelected = false,
                            isDark = isDarkMode,
                            onClick = {
                                scope.launch { drawerState.close() }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "MultiTool Calc Pro: All-in-one Standard, Scientific, Currency & Measurement Calculator with glass themes!")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share MultiTool Calc Pro"))
                            }
                        )

                        // Settings View Toggle
                        DrawerMenuItem(
                            title = "Settings",
                            icon = Icons.Default.Settings,
                            isSelected = false,
                            isDark = isDarkMode,
                            showChevron = true,
                            onClick = { showSettingsInDrawer = true }
                        )
                    } else {
                        // Settings View Inside Drawer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showSettingsInDrawer = false }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "‹ Back to Menu", fontSize = 14.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                        }

                        // Dark Mode Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Dark Mode",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkMode) Color.White else Color(0xFF0F172A)
                                )
                                Text(
                                    text = "Toggle glass appearance",
                                    fontSize = 12.sp,
                                    color = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                                )
                            }
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = {
                                    isDarkMode = it
                                    prefs.isDarkModeEnabled = it
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFFEF4444)
                                )
                            )
                        }

                        // Wallpaper Picker
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch { drawerState.close() }
                                    showWallpaperPage = true
                                }
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Wallpaper,
                                    contentDescription = null,
                                    tint = if (isDarkMode) Color.White else Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Wallpaper (20 Themes)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isDarkMode) Color.White else Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = activeWallpaper.name,
                                        fontSize = 12.sp,
                                        color = Color(0xFFEF4444)
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)
                            )
                        }

                        // App Version Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "App Version",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkMode) Color.White else Color(0xFF0F172A)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x33EF4444))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "1.0.0 Pro",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fullscreen Wallpaper Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(activeWallpaper.fallbackColors))
            )

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(activeWallpaper.url)
                    .crossfade(300)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Frosted / Dim Overlay for Glassmorphic readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isDarkMode) Color(0x40000000) else Color(0x1F000000)
                    )
            )

            // Main Scaffold
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    GlassTopBar(
                        title = "",
                        onMenuClick = {
                            showSettingsInDrawer = false
                            scope.launch { drawerState.open() }
                        },
                        onHistoryClick = { openHistory() },
                        showHistory = currentMode == CalcMode.STANDARD || currentMode == CalcMode.SCIENTIFIC,
                        isDark = isDarkMode
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentMode) {
                        CalcMode.STANDARD -> StandardCalcScreen(
                            isDark = isDarkMode,
                            onCalculationSuccess = { record ->
                                prefs.addHistoryItem(isScientific = false, record = record)
                            }
                        )
                        CalcMode.SCIENTIFIC -> ScientificCalcScreen(
                            isDark = isDarkMode,
                            onCalculationSuccess = { record ->
                                prefs.addHistoryItem(isScientific = true, record = record)
                            }
                        )
                        CalcMode.CURRENCY -> CurrencyConverterScreen(isDark = isDarkMode)
                        CalcMode.MASS -> MassConverterScreen(isDark = isDarkMode)
                        CalcMode.TIP -> TipCalcScreen(isDark = isDarkMode)
                        CalcMode.DISCOUNT -> DiscountCalcScreen(isDark = isDarkMode)
                    }
                }
            }
        }
    }

    // Full-page Wallpaper Selector (matching HTML .wallpaper-modal)
    if (showWallpaperPage) {
        WallpaperPage(
            initialIndex = wallpaperIndex,
            isDark = isDarkMode,
            onWallpaperSelected = { newIdx ->
                wallpaperIndex = newIdx
                prefs.wallpaperIndex = newIdx
            },
            onClose = { showWallpaperPage = false }
        )
    }

    // History Dialog
    if (showHistoryDialog) {
        val isSci = currentMode == CalcMode.SCIENTIFIC
        HistorySheet(
            title = if (isSci) "Scientific History" else "Standard History",
            historyList = historyItems,
            isDark = isDarkMode,
            onClear = {
                prefs.clearHistory(isSci)
                historyItems = emptyList()
            },
            onDismiss = { showHistoryDialog = false }
        )
    }
}

@Composable
fun DrawerMenuItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    isDark: Boolean,
    showChevron: Boolean = false,
    onClick: () -> Unit
) {
    val activeBg = Color(0x2EEF4444)
    val activeText = Color(0xFFEF4444)
    val normalText = if (isDark) Color.White else Color(0xFF0F172A)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) activeBg else Color.Transparent)
            .then(
                if (isSelected) {
                    Modifier.drawBehind {
                        drawRect(
                            color = Color(0xFFE11D48),
                            topLeft = androidx.compose.ui.geometry.Offset.Zero,
                            size = androidx.compose.ui.geometry.Size(4.dp.toPx(), size.height)
                        )
                    }
                } else Modifier
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) activeText else normalText
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) activeText else normalText
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
            )
        }
    }
}
