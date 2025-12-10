package com.shubhamdev.waterreminder.ui.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shubhamdev.waterreminder.ui.screens.navigation.Screen
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel,
    splashDelayMillis: Long = 1400L
) {
    val isOnboardingShown by viewModel.onboardingShown.collectAsState()

    // simple scale animation for logo
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(scale)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💧",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(text = "Aqua Pulse", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }

    LaunchedEffect(isOnboardingShown) {
        delay(splashDelayMillis)
        if (isOnboardingShown) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Welcome.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }
}

