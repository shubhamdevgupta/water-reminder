package com.shubhamdev.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shubhamdev.waterreminder.data.datastore.PreferencesDataSource
import com.shubhamdev.waterreminder.data.datastore.dataStore
import com.shubhamdev.waterreminder.data.local.DatabaseModule
import com.shubhamdev.waterreminder.data.repository.OnboardingRepositoryImpl
import com.shubhamdev.waterreminder.data.repository.SettingsRepositoryImpl
import com.shubhamdev.waterreminder.data.repository.WaterRepositoryImpl
import com.shubhamdev.waterreminder.domain.repository.OnboardingRepository
import com.shubhamdev.waterreminder.domain.usecase.AddWaterIntakeUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetAllIntakesUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetSettingsUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayIntakesUseCase
import com.shubhamdev.waterreminder.domain.usecase.GetTodayTotalUseCase
import com.shubhamdev.waterreminder.domain.usecase.SetSettingsUseCase
import com.shubhamdev.waterreminder.ui.screens.dashboard.DashboardScreen
import com.shubhamdev.waterreminder.ui.screens.dashboard.DashboardViewModel
import com.shubhamdev.waterreminder.ui.screens.dashboard.DashboardViewModelFactory
import com.shubhamdev.waterreminder.ui.screens.history.HistoryScreen
import com.shubhamdev.waterreminder.ui.screens.history.HistoryViewModel
import com.shubhamdev.waterreminder.ui.screens.history.HistoryViewModelFactory
import com.shubhamdev.waterreminder.ui.screens.navigation.Screen
import com.shubhamdev.waterreminder.ui.screens.onboarding.OnboardingScreen
import com.shubhamdev.waterreminder.ui.screens.onboarding.OnboardingViewModel
import com.shubhamdev.waterreminder.ui.screens.settings.SettingsScreen
import com.shubhamdev.waterreminder.ui.screens.settings.SettingsViewModel
import com.shubhamdev.waterreminder.ui.screens.settings.SettingsViewModelFactory
import com.shubhamdev.waterreminder.ui.screens.splash.SplashScreen
import com.shubhamdev.waterreminder.ui.screens.splash.SplashViewModel
import com.shubhamdev.waterreminder.ui.screens.welcome.WelcomeScreen
import com.shubhamdev.waterreminder.ui.theme.WaterReminderTheme

class MainActivity : ComponentActivity() {

    // Repositories
    private lateinit var onboardingRepository: OnboardingRepository
    private lateinit var waterRepository: WaterRepositoryImpl
    private lateinit var settingsRepository: SettingsRepositoryImpl
    
    // Use cases
    private lateinit var addWaterIntakeUseCase: AddWaterIntakeUseCase
    private lateinit var getTodayTotalUseCase: GetTodayTotalUseCase
    private lateinit var getTodayIntakesUseCase: GetTodayIntakesUseCase
    private lateinit var getSettingsUseCase: GetSettingsUseCase
    private lateinit var setSettingsUseCase: SetSettingsUseCase

    class SplashVMFactory(private val onboardingRepository: OnboardingRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(onboardingRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    class OnboardingVMFactory(private val onboardingRepository: OnboardingRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OnboardingViewModel(onboardingRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        val database = DatabaseModule.provideDatabase(applicationContext)
        
        // Initialize repositories
        onboardingRepository = OnboardingRepositoryImpl(applicationContext.dataStore)
        waterRepository = WaterRepositoryImpl(database.waterIntakeDao())
        val preferencesDataSource = PreferencesDataSource(applicationContext.dataStore)
        settingsRepository = SettingsRepositoryImpl(preferencesDataSource)
        
        // Initialize use cases
        addWaterIntakeUseCase = AddWaterIntakeUseCase(waterRepository)
        getTodayTotalUseCase = GetTodayTotalUseCase(waterRepository)
        getTodayIntakesUseCase = GetTodayIntakesUseCase(waterRepository)
        getSettingsUseCase = GetSettingsUseCase(settingsRepository)
        setSettingsUseCase = SetSettingsUseCase(settingsRepository)

        setContent {
            WaterReminderTheme {
                val navController = rememberNavController()

                // Splash ViewModel
                val splashViewModel: SplashViewModel = viewModel(
                    factory = SplashVMFactory(onboardingRepository)
                )

                // Onboarding ViewModel
                val onboardingViewModel: OnboardingViewModel = viewModel(
                    factory = OnboardingVMFactory(onboardingRepository)
                )

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController = navController, viewModel = splashViewModel)
                    }

                    composable(Screen.Welcome.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(navController = navController, viewModel = onboardingViewModel)
                    }

                    composable(Screen.Dashboard.route) {
                        DashboardScreen(
                            navController = navController,
                            viewModel = viewModel(
                                factory = DashboardViewModelFactory(
                                    addWaterIntakeUseCase,
                                    getTodayTotalUseCase,
                                    getTodayIntakesUseCase,
                                    getSettingsUseCase
                                )
                            )
                        )
                    }

                    composable(Screen.History.route) {
                        HistoryScreen(
                            navController = navController,
                            viewModel = viewModel(
                                factory = HistoryViewModelFactory(waterRepository)
                            )
                        )
                    }

                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            navController = navController,
                            viewModel = viewModel(
                                factory = SettingsViewModelFactory(
                                    getSettingsUseCase,
                                    setSettingsUseCase,
                                    applicationContext
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
