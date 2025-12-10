package com.shubhamdev.waterreminder.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shubhamdev.waterreminder.ui.theme.WaterBlue
import com.shubhamdev.waterreminder.ui.theme.WaterBlueLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController? = null,
    viewModel: SettingsViewModel = viewModel()
) {
    val dailyGoalMl by viewModel.dailyGoalMl.collectAsStateWithLifecycle()
    val remindersEnabled by viewModel.remindersEnabled.collectAsStateWithLifecycle()
    val reminderIntervalMinutes by viewModel.reminderIntervalMinutes.collectAsStateWithLifecycle()
    val unitIsMl by viewModel.unitIsMl.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var showGoalDialog by remember { mutableStateOf(false) }
    var goalInput by remember { mutableStateOf(dailyGoalMl.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Water Intake Settings
            item {
                SettingsGroup(
                    items = listOf(
                        SettingItem(
                            icon = "🎯",
                            title = "Daily Goal",
                            subtitle = "Set your daily water intake target",
                            value = "$dailyGoalMl ml",
                            type = SettingType.VALUE,
                            onClick = { showGoalDialog = true }
                        ),
                        SettingItem(
                            icon = "💧",
                            title = "Unit",
                            subtitle = "Choose between ml and oz",
                            value = if (unitIsMl) "ml" else "oz",
                            type = SettingType.TOGGLE_VALUE,
                            toggleValue = unitIsMl,
                            onToggleChange = { viewModel.setUnitIsMl(it) }
                        )
                    )
                )
            }

            // Reminder Settings
            item {
                SettingsGroup(
                    items = listOf(
                        SettingItem(
                            icon = "🔔",
                            title = "Notifications",
                            subtitle = null,
                            value = null,
                            type = SettingType.TOGGLE,
                            toggleValue = remindersEnabled,
                            onToggleChange = { viewModel.setRemindersEnabled(it) }
                        ),
                        SettingItem(
                            icon = "⏰",
                            title = "Reminder Interval",
                            subtitle = "How often to remind (minutes)",
                            value = "$reminderIntervalMinutes min",
                            type = SettingType.VALUE,
                            onClick = { /* TODO: Show interval picker */ }
                        )
                    )
                )
            }

            // App Information & Support
            item {
                SettingsGroup(
                    items = listOf(
                        SettingItem(
                            icon = "⭐",
                            title = "Rate App",
                            subtitle = null,
                            value = null,
                            type = SettingType.ACTION,
                            onClick = { /* TODO: Open Play Store */ }
                        ),
                        SettingItem(
                            icon = "❓",
                            title = "Help & Support",
                            subtitle = null,
                            value = null,
                            type = SettingType.ACTION,
                            onClick = { /* TODO: Open help */ }
                        ),
                        SettingItem(
                            icon = "🛡️",
                            title = "Privacy Policy",
                            subtitle = null,
                            value = null,
                            type = SettingType.ACTION,
                            onClick = { /* TODO: Open privacy policy */ }
                        )
                    )
                )
            }
        }
    }
    
    // Daily Goal Dialog
    if (showGoalDialog) {
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            confirmButton = {},
            title = {},
            text = {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // Header Icon + Title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(WaterBlue.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎯", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                            }

                            Column {
                                Text(
                                    "Set Daily Goal",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Adjust your hydration target for better tracking",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Input field background
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(12.dp)
                        ) {
                            OutlinedTextField(
                                value = goalInput,
                                onValueChange = { new ->
                                    if (new.all { it.isDigit() }) goalInput = new
                                },
                                placeholder = { Text("Enter goal (ml)") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                onClick = { showGoalDialog = false }
                            ) {
                                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Button(
                                onClick = {
                                    goalInput.toIntOrNull()?.let {
                                        viewModel.setDailyGoal(it)
                                        showGoalDialog = false
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WaterBlue)
                            ) {
                                Text("Save Goal", color = Color.White)
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SettingsGroup(
    items: List<SettingItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items.forEachIndexed { index, item ->
                SettingRow(item = item)
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.type != SettingType.TOGGLE && item.onClick != null) {
                item.onClick?.invoke()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
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
                    text = item.icon,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (item.subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (item.value != null && item.subtitle == null && item.type != SettingType.TOGGLE_VALUE) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.value,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        when (item.type) {
            SettingType.VALUE -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.value != null) {
                        Text(
                            text = item.value,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            SettingType.TOGGLE -> {
                Switch(
                    checked = item.toggleValue ?: false,
                    onCheckedChange = item.onToggleChange ?: {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = WaterBlue
                    )
                )
            }
            SettingType.TOGGLE_VALUE -> {
                Switch(
                    checked = item.toggleValue ?: false,
                    onCheckedChange = item.onToggleChange ?: {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = WaterBlue
                    )
                )
            }
            SettingType.ACTION -> {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

data class SettingItem(
    val icon: String,
    val title: String,
    val subtitle: String?,
    val value: String?,
    val type: SettingType,
    val toggleValue: Boolean? = null,
    val onToggleChange: ((Boolean) -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)

enum class SettingType {
    VALUE,
    TOGGLE,
    TOGGLE_VALUE,
    ACTION
}

