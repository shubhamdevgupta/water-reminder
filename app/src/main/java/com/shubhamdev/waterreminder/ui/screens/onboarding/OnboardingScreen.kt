package com.shubhamdev.waterreminder.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Skip button (top-right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    viewModel.markOnboardingComplete {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                }) {
                    Text("Skip", color = WaterBlue)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SWIPEABLE PAGER
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->

                when (page) {
                    0 -> OnboardingPage(
                        title = "Never Forget to Hydrate 💧",
                        desc = "Receive friendly reminders throughout the day and stay refreshed.",
                    )

                    1 -> OnboardingPage(
                        title = "Track Your Daily Intake 📈",
                        desc = "Monitor your hydration habits and achieve your daily goals.",
                    )

                    2 -> OnboardingPage(
                        title = "Stay Healthy, Stay Active ❤️",
                        desc = "Hydration boosts energy, improves focus, and supports wellness.",
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PAGE INDICATORS
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == index)
                                    WaterBlue
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // NEXT or ENABLE NOTIFICATIONS button
            val coroutineScope = rememberCoroutineScope()

            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        viewModel.markOnboardingComplete {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Onboarding.route) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WaterBlue
                )
            ) {
                Text(
                    text = if (pagerState.currentPage < 2) "Next" else "Enable Notifications",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun OnboardingPage(title: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Icon
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            WaterBlueLight.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(WaterBlue, WaterBlueLight)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("💧", style = MaterialTheme.typography.displayLarge)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = desc,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
