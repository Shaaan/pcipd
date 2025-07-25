package `in`.shaaan.pcipharmd.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * WHAT: A sealed class that defines all navigation destinations in the app.
 * WHY: To provide type-safety for navigation, preventing runtime crashes from typos in route strings
 * and making the navigation graph easier to manage and refactor.
 * HOW: Each object/class represents a screen. Routes with arguments have helper functions
 * to build the route string correctly.
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object About : Screen("about")

    // Represents the screen that shows subjects for a specific year.
    data object Year : Screen("year/{yearId}") {
        // Function to create the route with the actual year ID for navigation.
        fun createRoute(id: Int) = "year/$id"

        // Define the arguments this screen expects.
        val arguments = listOf(
            navArgument("yearId") { type = NavType.IntType }
        )
    }
}