package com.shubhamdev.waterreminder.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shubhamdev.waterreminder.ui.screens.navigation.Screen
import com.shubhamdev.waterreminder.ui.theme.WaterBlue
import com.shubhamdev.waterreminder.ui.theme.WaterBlueLight

@Composable
fun WelcomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Water glass image placeholder (circular)
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFE8F4F8),
                                Color(0xFFD0E8F0)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Water drop icon
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    WaterBlue,
                                    WaterBlueLight
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💧",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Welcome text
            Text(
                text = "Welcome to Aqua Pulse",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Let's help you reach your hydration goals and feel your best, one sip at a time.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Get Started button
            Button(
                onClick = {
                    navController.navigate(Screen.Onboarding.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WaterBlue
                )
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}

