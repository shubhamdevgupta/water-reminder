package com.shubhamdev.waterreminder.ui.screens.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.ui.screens.navigation.Screen
import com.shubhamdev.waterreminder.ui.theme.ProgressBlue
import com.shubhamdev.waterreminder.ui.theme.ProgressGray
import com.shubhamdev.waterreminder.ui.theme.WaterBlue
import com.shubhamdev.waterreminder.ui.theme.WaterBlueLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
//aquapulse
@Composable
fun DashboardScreen(
    navController: NavController? = null,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val todayIntakes by viewModel.todayIntakes.collectAsStateWithLifecycle(initialValue = emptyList())
    val quickAddOptions = listOf(200, 300, 500)

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    )
    { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
        ) {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // Water drop icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(WaterBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💧", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Title with gradient text
                        Text(
                            text = "Aqua Pulse",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.Blue,
                            modifier = Modifier.graphicsLayer(alpha = 0.99f)
                                .drawWithContent {
                                    val gradient = Brush.linearGradient(
                                        colors = listOf(
                                            WaterBlue,
                                            WaterBlueLight
                                        )
                                    )
                                    drawContent()
                                    drawRect(gradient, blendMode = androidx.compose.ui.graphics.BlendMode.SrcAtop)
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Subtitle
                    Text(
                        text = "Track. Hydrate. Thrive.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Circular Progress
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = uiState.progressPercent,
                        currentIntake = uiState.todayTotalMl,
                        dailyGoal = uiState.dailyGoalMl
                    )
                }
            }

            // Quick Add Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    quickAddOptions.forEach { amount ->
                        QuickAddButton(
                            amount = amount,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.quickAdd(amount) }
                        )
                    }
                }
            }

            // Today's Log
            item {
                Text(
                    "Today's Log",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
// If no logs → show hydration educational section
            if (todayIntakes.isEmpty()) {
                item {
                    EmptyHydrationInfo()
                }
            } else {
                items(todayIntakes) { intake ->
                    WaterLogItem(intake)
                }
            }
        }
    }
}


@Composable
fun CircularProgressIndicator(
    progress: Float,
    currentIntake: Int,
    dailyGoal: Int,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(900)
    )

    val size = 280.dp
    val strokeWidth = 24.dp

    // Glow when 100%
    val outerGlowScale = remember { Animatable(1f) }
    val celebrationActive = animatedProgress >= 1f

    if (celebrationActive) {
        LaunchedEffect(Unit) {
            outerGlowScale.animateTo(
                1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // Wave animation
    val infinite = rememberInfiniteTransition()
    val waveOffset by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        // ✨ Outer celebration ring
        if (celebrationActive) {
            Canvas(
                modifier = Modifier
                    .size(size * outerGlowScale.value)
            ) {
                drawCircle(
                    color = WaterBlue.copy(alpha = 0.35f),
                    radius = size.toPx() / 2f
                )
            }
        }

        // Background ring
        Canvas(Modifier.fillMaxSize()) {
            drawArc(
                color = ProgressGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Progress ring
        Canvas(Modifier.fillMaxSize()) {
            drawArc(
                color = ProgressBlue,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Water wave (inner)
        // Water wave (inner clipped to circle)
// Water wave (inner clipped to circle)
        Canvas(modifier = Modifier.size(230.dp)) {

            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val radius = this.size.minDimension / 2f
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f

            // Circular clip path
            val circlePath = androidx.compose.ui.graphics.Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        centerX - radius,
                        centerY - radius,
                        centerX + radius,
                        centerY + radius
                    )
                )
            }

            // Water level (rises with progress)
            val waterLevel = centerY + radius - (animatedProgress * 2f * radius)

            // Wave path
            val wavePath = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, waterLevel)

                for (x in 0..canvasWidth.toInt()) {
                    val y = waterLevel + (12 * kotlin.math.sin((x + waveOffset) * 0.04f))
                    lineTo(x.toFloat(), y)
                }

                lineTo(canvasWidth, canvasHeight)
                lineTo(0f, canvasHeight)
                close()
            }

            // Clip wave inside circle
            clipPath(circlePath) {
                drawPath(
                    wavePath,
                    WaterBlueLight.copy(alpha = 0.35f)
                )
            }
        }


        // Drop icon
        Box(
            modifier = Modifier
                .offset(y = (-size.value / 2 + 24).dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(WaterBlue),
            contentAlignment = Alignment.Center
        ) {
            Text("💧", style = MaterialTheme.typography.titleMedium)
        }

        // Text inside
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${currentIntake}ml",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Goal: ${dailyGoal}ml",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyHydrationInfo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(WaterBlueLight.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text("💧", style = MaterialTheme.typography.displayMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Stay Hydrated!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Drinking enough water boosts energy, improves focus,\n" +
                    "and supports a healthy lifestyle.\n\n" +
                    "Start logging your intake today!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* maybe open Add Intake popup later */ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WaterBlue)
        ) {
            Text("Add Your First Drink", color = Color.White)
        }
    }
}

@Composable
fun QuickAddButton(
    amount: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = WaterBlue)
    ) {
        Text("+${amount}ml", color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun WaterLogItem(intake: WaterIntake) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(intake.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(WaterBlueLight.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💧",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = "${intake.amountMl}ml",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = timeStr,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController?) {
    var selectedRoute by remember { mutableStateOf(Screen.Dashboard.route) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        NavigationBar(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))              // smoother curvature
                .background(WaterBlueLight.copy(alpha = 0.20f))
                .height(72.dp),                               // increased height (fix icon cut)
            containerColor = Color.White.copy(alpha = 0.95f),
            tonalElevation = 10.dp
        ) {

            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = "Dashboard",
                        modifier = Modifier.size(26.dp)       // bigger, centered icon
                    )
                },
                label = { Text("Dashboard") },
                selected = selectedRoute == Screen.Dashboard.route,
                onClick = {
                    selectedRoute = Screen.Dashboard.route
                    navController?.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WaterBlue,
                    selectedTextColor = WaterBlue,
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "History",
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = { Text("History") },
                selected = selectedRoute == Screen.History.route,
                onClick = {
                    selectedRoute = Screen.History.route
                    navController?.navigate(Screen.History.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WaterBlue,
                    selectedTextColor = WaterBlue,
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = { Text("Settings") },
                selected = selectedRoute == Screen.Settings.route,
                onClick = {
                    selectedRoute = Screen.Settings.route
                    navController?.navigate(Screen.Settings.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WaterBlue,
                    selectedTextColor = WaterBlue,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}


