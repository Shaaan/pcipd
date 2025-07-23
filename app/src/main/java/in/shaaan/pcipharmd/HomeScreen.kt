package `in`.shaaan.pcipharmd

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Looks3
import androidx.compose.material.icons.filled.Looks4
import androidx.compose.material.icons.filled.Looks5
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.LooksTwo
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.ui.components.NativeAdComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val years = remember { (1..6).toList() }
    val adPositions = remember { setOf(2, 4, 6) } // Positions after which to show an ad

    // What: A Scaffold provides the basic Material Design layout structure.
    // Why: It simplifies the placement of top bars, floating action buttons, and snackbars.
    // How: It takes composables for topBar, floatingActionButton, and content.
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate("about") }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent =
                    Intent(Intent.ACTION_VIEW, "market://details?id=${context.packageName}".toUri())
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Star, contentDescription = "Rate App")
            }
        }
    ) { paddingValues ->
        // What: A LazyVerticalGrid to display items in a grid.
        // Why: It's the Compose equivalent of a GridView or RecyclerView with a GridLayoutManager, efficiently rendering only visible items.
        // How: It takes a column definition and a lambda to render items from a list.

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            years.forEach { year ->
                item(key = year) {
                    YearCard(
                        year = year,
                        subjectCount = SyllabusRepository.getDataForYear(year)?.subjects?.size ?: 0,
                        onClick = { navController.navigate("year/$year") },
                        // What: Animate item placement.
                        // Why: Adds a polished, fluid feel when the grid appears.
                        modifier = Modifier.animateItem()
                    )
                }
                if (adPositions.contains(year)) {
                    item(key = "ad_for_$year") {
                        // What: Style the ad card to be distinct but themed.
                        // Why: Differentiates ads from content while maintaining visual harmony.
                        Card(
                            modifier = Modifier.animateItem().padding(horizontal = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = MaterialTheme.shapes.large
                        ) {
                            NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                        }
                    }
                }
            }
        }
    }
}

    @Composable
    fun YearCard(year: Int, onClick: () -> Unit, modifier: Modifier = Modifier, subjectCount: Int) {
        val icon = when (year) {
            1 -> Icons.Default.LooksOne
            2 -> Icons.Default.LooksTwo
            3 -> Icons.Default.Looks3
            4 -> Icons.Default.Looks4
            5 -> Icons.Default.Looks5
            6 -> Icons.Default.Looks6
            else -> Icons.AutoMirrored.Filled.List
        }
        val text = when (year) {
            1 -> "First Year"
            2 -> "Second Year"
            3 -> "Third Year"
            4 -> "Fourth Year"
            5 -> "Fifth Year"
            6 -> "Sixth Year"
            else -> ""
        }
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // What: Add an Icon to the card.
                // Why: To provide a strong visual anchor.
                // How: Using the Icon composable with a larger size.
                Icon(
                    imageVector = icon,
                    contentDescription = "Year $year",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$subjectCount Subjects",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
