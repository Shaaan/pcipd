// app/src/main/java/in/shaaan/pcipharmd/ui/screen/HomeScreen.kt (New File)

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.ui.components.NativeAdComposable
import `in`.shaaan.pcipharmd.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
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
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(years.size) { index ->
                val year = years[index]
                // What: A Column to hold the YearCard and potentially an ad card.
                // Why: Ensures the ad appears directly below the preceding year card in the grid flow.
                // How: Wrap the card and the conditional ad in a Column.
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    YearCard(year = year, onClick = {
                        navController.navigate("year/$year")
                    })

                    if (adPositions.contains(year)) {
                        // What: Wrap the native ad in a Card.
                        // Why: To make the ad's presentation consistent with the rest of the UI elements.
                        // How: Place the NativeAdComposable inside a Card composable.
                        Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                            NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                        }
                    }
                    }
                }
            }
        }
    }

    @Composable
    fun YearCard(year: Int, onClick: () -> Unit) {
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            ) {
                // What: Add an Icon to the card.
                // Why: To provide a strong visual anchor.
                // How: Using the Icon composable with a larger size.
//                Icon(
//                    imageVector = icon,
//                    contentDescription = "Year $year",
//                    modifier = Modifier.size(48.dp),
//                    tint = MaterialTheme.colorScheme.primary
//                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$text",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "View Syllabus",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
