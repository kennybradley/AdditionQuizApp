package additionquiz.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun QuizScreen(
    selectedNumber: Int,
    operation: String,
    onQuizComplete: (score: Int, time: Int, incorrect: List<Problem>) -> Unit
) {
    val problems = remember { generateProblems(selectedNumber, operation) }
    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var timer by remember { mutableStateOf(0) }
    val incorrectProblems = remember { mutableStateListOf<Problem>() }

    // Timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            timer++
        }
    }

    val currentProblem = problems[currentIndex]
    val correctAnswer = currentProblem.answer
    val options = remember(currentProblem) {
        generateAnswerOptions(correctAnswer)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Time: ${timer}s",
            modifier = Modifier.align(Alignment.TopEnd),
            style = MaterialTheme.typography.bodyLarge
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Problem layout
            Box(modifier = Modifier.width(120.dp), contentAlignment = Alignment.CenterEnd) {
                Column(horizontalAlignment = Alignment.End) {
                    val style = TextStyle(fontSize = 48.sp, fontFamily = FontFamily.Monospace)
                    Text("${currentProblem.a}", style = style)
                    Text("${if (operation == "Addition") "+" else "-"} ${currentProblem.b}", style = style)
                    Text("———", style = style)
                    Text("?", style = style)
                }
            }

            // Answer options
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().height(300.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(options) { option ->
                    Button(
                        onClick = {
                            if (option == correctAnswer) {
                                score++
                            } else {
                                incorrectProblems.add(currentProblem)
                            }

                            if (currentIndex < problems.lastIndex) {
                                currentIndex++
                            } else {
                                onQuizComplete(score, timer, incorrectProblems.toList())
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Text(option.toString(), fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

fun generateProblems(selectedNumber: Int, operation: String): List<Problem> {
    val problems = mutableListOf<Problem>()

    for (i in 0..9) {
        // constant + variable
        val a1 = selectedNumber
        val b1 = i
        val answer1 = if (operation == "Addition") a1 + b1 else a1 - b1
        if (operation != "Subtraction" || answer1 >= 0) {
            problems += Problem(a1, b1, answer1)
        }

        // variable + constant
        val a2 = i
        val b2 = selectedNumber
        val answer2 = if (operation == "Addition") a2 + b2 else a2 - b2
        if (operation != "Subtraction" || answer2 >= 0) {
            problems += Problem(a2, b2, answer2)
        }
    }

    return problems.shuffled()
}


// Generates 6 answer options including correct one, all non-negative
fun generateAnswerOptions(correct: Int): List<Int> {
    val options = mutableSetOf(correct)
    while (options.size < 6) {
        val fake = (correct - 10..correct + 10).random()
        if (fake != correct && fake >= 0) {
            options.add(fake)
        }
    }
    return options.sorted()
}
