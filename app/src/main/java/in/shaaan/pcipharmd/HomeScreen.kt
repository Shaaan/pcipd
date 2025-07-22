// app/src/main/java/in/shaaan/pcipharmd/ui/screen/HomeScreen.kt (New File)

package `in`.shaaan.pcipharmd.ui.screen

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.R
import `in`.shaaan.pcipharmd.ui.components.NativeAdComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // What: A Scaffold provides the basic Material Design layout structure.
    // Why: It simplifies the placement of top bars, floating action buttons, and snackbars.
    // How: It takes composables for topBar, floatingActionButton, and content.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate("about") }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "market://details?id=${context.packageName}".toUri())
                context.startActivity(intent)
            }) {
                Icon(Icons.Default.Star, contentDescription = "Rate App")
            }
        }
    ) { paddingValues ->
        // What: A LazyVerticalGrid to display items in a grid.
        // Why: It's the Compose equivalent of a GridView or RecyclerView with a GridLayoutManager, efficiently rendering only visible items.
        // How: It takes a column definition and a lambda to render items from a list.
        val years = (1..6).toList()
        val adPositions = listOf(2, 5) // Positions after which to show an ad

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var adCounter = 0
            itemsIndexed(years) { index, year ->
                Column {
                    YearCard(year = year, onClick = {
                        navController.navigate("year/$year")
                    })
                    if (adPositions.contains(index + 1)) {
                        // What: The reusable NativeAdComposable.
                        // Why: Encapsulates ad loading and display logic.
                        // How: Placed directly in the lazy grid layout.
                        Spacer(modifier = Modifier.height(8.dp))
                        NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                        adCounter++
                    }
                }
            }
        }
    }
}

@Composable
fun YearCard(year: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Year $year",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}