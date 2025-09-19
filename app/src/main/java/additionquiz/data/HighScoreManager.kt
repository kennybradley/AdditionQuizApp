package additionquiz.data

import android.content.Context

class HighScoreManager(context: Context) {
    private val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)

    fun getHighScore(number: Int, operation: String): Int? {
        val key = "$operation-$number"
        return if (prefs.contains(key)) prefs.getInt(key, Int.MAX_VALUE) else null
    }

    fun updateHighScore(number: Int, operation: String, time: Int) {
        val key = "$operation-$number"
        val current = getHighScore(number, operation)
        if (current == null || time < current) {
            prefs.edit().putInt(key, time).apply()
        }
    }

    fun clearAllScores() {
        prefs.edit().clear().apply()
    }
}
