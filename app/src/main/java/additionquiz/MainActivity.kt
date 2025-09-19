package additionquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import additionquiz.ui.*
import additionquiz.ui.theme.AdditionQuizTheme
import java.util.ArrayList // Added import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdditionQuizTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {

                    // Home screen
                    composable("home") {
                        HomeScreen(navController)
                    }

                    // Quiz screen with selected number and operation
                    composable(
                        route = "quiz/{number}/{operation}",
                        arguments = listOf(
                            navArgument("number") { type = NavType.IntType },
                            navArgument("operation") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val number = backStackEntry.arguments?.getInt("number") ?: 0
                        val operation = backStackEntry.arguments?.getString("operation") ?: "Addition"

                        QuizScreen(
                            selectedNumber = number,
                            operation = operation,
                            onQuizComplete = { score, time, incorrectList ->
                                // Save incorrect problems to back stack
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("incorrectProblems", ArrayList(incorrectList)) // Changed to ArrayList

                                navController.navigate("result/$score/$number/$operation/$time")
                            }
                        )
                    }

                    // Result screen with score, time, and incorrect answers
                    composable(
                        route = "result/{score}/{number}/{operation}/{time}",
                        arguments = listOf(
                            navArgument("score") { type = NavType.IntType },
                            navArgument("number") { type = NavType.IntType },
                            navArgument("operation") { type = NavType.StringType },
                            navArgument("time") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val score = backStackEntry.arguments?.getInt("score") ?: 0
                        val number = backStackEntry.arguments?.getInt("number") ?: 0
                        val operation = backStackEntry.arguments?.getString("operation") ?: "Addition"
                        val time = backStackEntry.arguments?.getInt("time") ?: 0

                        // Retrieve the ArrayList and handle it as a List
                        val incorrect = navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<ArrayList<Problem>>("incorrectProblems") // Get as ArrayList
                            ?.toList() // Convert to List for the Composable
                            ?: emptyList()

                        ResultScreen(
                            navController = navController,
                            score = score,
                            selectedNumber = number,
                            operation = operation,
                            timeInSeconds = time,
                            incorrectProblems = incorrect
                        )
                    }
                }
            }
        }
    }
}
