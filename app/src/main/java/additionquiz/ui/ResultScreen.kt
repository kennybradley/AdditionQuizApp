package additionquiz.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import additionquiz.data.HighScoreManager

@Composable
fun ResultScreen(
    navController: NavController,
    score: Int,
    selectedNumber: Int,
    operation: String,
    timeInSeconds: Int,
    incorrectProblems: List<Problem>
) {
    val context = LocalContext.current
    val highScoreManager = remember { HighScoreManager(context) }

    var previousBest by remember { mutableStateOf<Int?>(null) }
    var isNewRecord by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val best = highScoreManager.getHighScore(selectedNumber, operation)
        previousBest = best
        if (incorrectProblems.isEmpty()) {
            if (best == null || timeInSeconds < best) {
                isNewRecord = true
                highScoreManager.updateHighScore(selectedNumber, operation, timeInSeconds)
                Toast.makeText(context, "🎉 New Record!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineLarge)
        Text("Score: $score / 20", fontSize = 28.sp)
        Text("Your Time: ${timeInSeconds}s", fontSize = 24.sp)
        Text("Best Time: ${previousBest ?: "—"}s", fontSize = 24.sp)

        if (isNewRecord) {
            Text(
                text = "🎉 New Record!",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (incorrectProblems.isNotEmpty()) {
            Text("Incorrect Answers:", style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                incorrectProblems.forEach { problem ->
                    val symbol = if (operation == "Addition") "+" else "-"
                    Text("${problem.a} $symbol ${problem.b} = ${problem.answer}")
                }
            }
        }

        Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
            Text("Play Again")
        }
    }
}
