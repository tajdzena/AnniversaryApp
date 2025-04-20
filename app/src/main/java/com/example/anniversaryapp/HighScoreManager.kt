package com.example.anniversaryapp
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("high_scores")

class HighScoreManager(private val context: Context) {

    private fun keyForDuration(duration: Int): Preferences.Key<Int> =
        intPreferencesKey("high_score_$duration")

    fun getHighScore(duration: Int): Int {
        val key = keyForDuration(duration)
        return runBlocking {
            val prefs = context.dataStore.data.first()
            prefs[key] ?: 0
        }
    }

    fun saveHighScore(duration: Int, score: Int) {
        val key = keyForDuration(duration)
        runBlocking {
            context.dataStore.edit { prefs ->
                if ((prefs[key] ?: 0) < score) {
                    prefs[key] = score
                }
            }
        }
    }
}
