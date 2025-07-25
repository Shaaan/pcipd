package `in`.shaaan.pcipharmd.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    // WHAT: Removed the Scaffold and TopAppBar.
    // WHY: This is now handled by the central Scaffold in MainActivity.
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Version 4.0", // This could be dynamic
            style = MaterialTheme.typography.bodyMedium
        )
        // Add more about text here
    }
}
