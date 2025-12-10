package com.shubhamdev.waterreminder.ui.screens.history

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.shubhamdev.waterreminder.domain.model.WaterIntake
import com.shubhamdev.waterreminder.ui.theme.WaterBlue
import com.shubhamdev.waterreminder.ui.theme.WaterBlueLight
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController? = null,
    viewModel: HistoryViewModel = viewModel()
) {
    val allIntakes by viewModel.allIntakes.collectAsStateWithLifecycle(initialValue = emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // We'll show two pages: Weekly (index 0) and Monthly (index 1)
    val pagerState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = pagerState)

    // Observe visible page and map to Period
    var selectedIndex by remember { mutableStateOf(0) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { idx ->
                selectedIndex = idx
            }
    }

    val selectedPeriod = if (selectedIndex == 0) Period.WEEKLY else Period.MONTHLY

    // Filtered intakes for selected period
    val filteredIntakes = remember(allIntakes, selectedPeriod) {
        filterIntakesForPeriod(allIntakes, selectedPeriod)
    }

    // Group by date string for display
    val groupedIntakes = remember(filteredIntakes) {
        filteredIntakes.groupBy { intake ->
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            sdf.format(Date(intake.timestamp))
        }.toSortedMap(compareByDescending { // keep newest date first
            // parse back date to sort
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            sdf.parse(it)?.time ?: 0L
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "History & Analytics",
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
                ),
                actions = {
                    TextButton(onClick = { viewModel.showDeleteConfirmation() }) {
                        Text("Clear History")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Pager (LazyRow) for Weekly / Monthly with segmented control above
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Segmented control
                    SegmentedPeriodControl(
                        selectedIndex = selectedIndex,
                        onSelect = { idx ->
                            scope.launch {
                                // animate scroll to page
                                pagerState.animateScrollToItem(idx)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Stats for selected period
                    val stats = calculateStats(filteredIntakes)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = "💧",
                            value = stats.avgIntake,
                            label = "Avg. Intake",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = "✓",
                            value = stats.completion,
                            label = "Completion",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            icon = "🏆",
                            value = stats.bestDay,
                            label = "Best Day",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            icon = "🔥",
                            value = stats.longestStreak,
                            label = "Longest Streak",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // The pager content is two full-width pages; we use LazyRow to allow swipe left/right
            item {
                LazyRow(
                    state = pagerState,
                    flingBehavior = snapBehavior,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()

                ) {
                    items(2) { pageIndex ->
                        // pageIndex 0 -> Weekly; 1 -> Monthly
                        val pagePeriod = if (pageIndex == 0) Period.WEEKLY else Period.MONTHLY
                        val pageIntakes = filterIntakesForPeriod(allIntakes, pagePeriod)
                        Column(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(end = 12.dp)
                        ) {
                            // A small chart placeholder could go here in future
                            // For now, show the daily log header and first N items for that period
                            Text(
                                text = "Daily Log",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                            )

                            val grouped = pageIntakes.groupBy { intake ->
                                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                sdf.format(Date(intake.timestamp))
                            }.toSortedMap(compareByDescending {
                                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                sdf.parse(it)?.time ?: 0L
                            })

                            if (grouped.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No entries yet for this period.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                grouped.forEach { (date, intakes) ->
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    intakes.forEach { intake ->
                                        IntakeRow(intake = intake)
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (uiState.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeleteConfirmation() },
                title = { Text("Clear History") },
                text = { Text("Are you sure you want to delete all water intake history? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.deleteAll() }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDeleteConfirmation() }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

/**
 * Simple segmented control with pill UI and animated color transition.
 * selectedIndex = 0 => Weekly, 1 => Monthly
 */
@Composable
private fun SegmentedPeriodControl(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val containerShape = RoundedCornerShape(30.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(containerShape)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Weekly button
            val weeklyBg by animateColorAsState(
                targetValue = if (selectedIndex == 0) WaterBlue else Color.Transparent
            )
            val weeklyTextColor by animateColorAsState(
                targetValue = if (selectedIndex == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(weeklyBg)
                    .clickable { onSelect(0) }
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Weekly",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (selectedIndex == 0) FontWeight.Bold else FontWeight.Medium,
                    color = weeklyTextColor
                )
            }

            // Monthly button
            val monthlyBg by animateColorAsState(
                targetValue = if (selectedIndex == 1) WaterBlue else Color.Transparent
            )
            val monthlyTextColor by animateColorAsState(
                targetValue = if (selectedIndex == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(monthlyBg)
                    .clickable { onSelect(1) }
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Monthly",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (selectedIndex == 1) FontWeight.Bold else FontWeight.Medium,
                    color = monthlyTextColor
                )
            }
        }
    }
}

// ---------- Re-used composables (stat card & intake row) ----------

@Composable
fun StatCard(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WaterBlueLight.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun IntakeRow(intake: WaterIntake) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(intake.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(WaterBlueLight.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💧", style = MaterialTheme.typography.bodyLarge)
                }
                Column {
                    Text(
                        text = "${intake.amountMl}ml",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!intake.note.isNullOrBlank()) {
                        Text(
                            text = intake.note ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Text(
                text = timeStr,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ---------- Utilities ----------

private fun filterIntakesForPeriod(intakes: List<WaterIntake>, period: Period): List<WaterIntake> {
    if (intakes.isEmpty()) return emptyList()
    val now = System.currentTimeMillis()
    val startMillis = when (period) {
        Period.WEEKLY -> now - 7L * 24 * 60 * 60 * 1000 // last 7 days
        Period.MONTHLY -> now - 30L * 24 * 60 * 60 * 1000 // last 30 days
    }
    return intakes.filter { it.timestamp >= startMillis && it.timestamp <= now }
}

private fun calculateStats(intakes: List<WaterIntake>): Statistics {
    if (intakes.isEmpty()) {
        return Statistics("0 L / day", "0%", "N/A", "0 Days")
    }

    // total per day
    val totalsByDay = intakes.groupBy { intake ->
        val cal = Calendar.getInstance()
        cal.timeInMillis = intake.timestamp
        // group by day of year + year to avoid collisions
        "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.DAY_OF_YEAR)}"
    }.mapValues { entry -> entry.value.sumOf { it.amountMl } }

    val days = totalsByDay.size.coerceAtLeast(1)
    val avgMl = totalsByDay.values.sum() / days
    val avgL = String.format("%.1f", avgMl / 1000.0)

    // completion relative to goal (use 2000 if unavailable; consider wiring to repo later)
    val goal = 2000
    val avgDaily = totalsByDay.values.average().toInt()
    val completion = ((avgDaily.toFloat() / goal * 100).coerceIn(0f, 100f)).toInt()

    val bestDayMl = totalsByDay.values.maxOrNull() ?: 0
    val bestDayL = String.format("%.1f", bestDayMl / 1000.0)

    // Longest streak in days with at least one entry (simple consecutive days calculation)
    val dayKeysSorted = totalsByDay.keys.map { key ->
        val parts = key.split("-")
        val year = parts[0].toInt()
        val dayOfYear = parts[1].toInt()
        Pair(year, dayOfYear)
    }.sortedWith(compareBy({ it.first }, { it.second }))

    var longestStreak = 0
    var currentStreak = 0
    var prevDayTotal = -1
    var prevYear = -1
    var prevDayOfYear = -1
    dayKeysSorted.forEach { (y, d) ->
        if (prevYear == -1) {
            currentStreak = 1
        } else {
            // check if consecutive day
            val isConsecutive = if (y == prevYear) {
                d == prevDayOfYear + 1
            } else {
                // crude cross-year consecutive (not perfect for leap years)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, prevYear)
                calendar.set(Calendar.DAY_OF_YEAR, prevDayOfYear)
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val nextYear = calendar.get(Calendar.YEAR)
                val nextDay = calendar.get(Calendar.DAY_OF_YEAR)
                y == nextYear && d == nextDay
            }
            currentStreak = if (isConsecutive) currentStreak + 1 else 1
        }
        longestStreak = max(longestStreak, currentStreak)
        prevYear = y
        prevDayOfYear = d
    }

    return Statistics(
        avgIntake = "$avgL L / day",
        completion = "$completion%",
        bestDay = "$bestDayL L",
        longestStreak = "$longestStreak Days"
    )
}

data class Statistics(
    val avgIntake: String,
    val completion: String,
    val bestDay: String,
    val longestStreak: String
)

enum class Period { WEEKLY, MONTHLY }
