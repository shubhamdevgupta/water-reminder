package com.shubhamdev.waterreminder.ui.screens.navigation


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Settings : Screen("settings")
    object History : Screen("history")
}
