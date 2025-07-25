package `in`.shaaan.pcipharmd.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * WHAT: A sealed class that defines all navigation destinations in the app.
 * WHY: To provide type-safety for navigation, preventing runtime crashes from typos in route strings
 * and making the navigation graph easier to manage and refactor.
 * HOW: Each object/class represents a screen. Routes with arguments have helper functions
 * to build the route string correctly.
 */
sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    // Main app sections for the Bottom Navigation Bar
    data object Syllabus : Screen("syllabus", "Syllabus", Icons.AutoMirrored.Filled.LibraryBooks)
    data object QuestionPapers : Screen("question_papers", "Q. Papers", Icons.Default.Description)

    // Screens within the Syllabus flow
    data object Home : Screen("home")
    data object Year : Screen("year/{yearId}") {
        fun createRoute(id: Int) = "year/$id"
        val arguments = listOf(navArgument("yearId") { type = NavType.IntType })
    }

    // Screens within the Question Papers flow
    data object QuestionPaperList : Screen("question_paper_list/{yearId}") {
        fun createRoute(id: Int) = "question_paper_list/$id"
        val arguments = listOf(navArgument("yearId") { type = NavType.IntType })
    }

    // Other screens
    data object About : Screen("about")
}
