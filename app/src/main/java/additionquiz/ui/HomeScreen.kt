package additionquiz.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import additionquiz.data.HighScoreManager

@Composable
fun HomeScreen(navController: NavController) {
    var selectedNumber by remember { mutableStateOf(1) }
    var operation by remember { mutableStateOf("Addition") }

    val context = LocalContext.current
    val highScoreManager = remember { HighScoreManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose Operation", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf("Addition", "Subtraction").forEach { op ->
                val isSelected = operation == op
                Button(
                    onClick = { operation = op },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(op)
                }
            }
        }

        Text("Select a Number to Practice", style = MaterialTheme.typography.headlineSmall)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(9) { index ->
                val number = index + 1
                val isSelected = selectedNumber == number

                Button(
                    onClick = { selectedNumber = number },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(number.toString())
                }
            }
        }

        Text("Selected Number: $selectedNumber", style = MaterialTheme.typography.bodyLarge)

        Button(
            onClick = {
                navController.navigate("quiz/$selectedNumber/$operation")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Quiz")
        }

        Button(
            onClick = {
                highScoreManager.clearAllScores()
                Toast.makeText(context, "All records cleared!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Clear Records")
        }
    }
}
