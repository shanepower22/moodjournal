package ie.setu.moodjournal.models

import org.junit.Assert.*
import org.junit.Test

class MoodEntryModelTest {

    @Test
    fun validMoodEntry_ReturnsTrue() {
        val mood = MoodEntryModel(
            id = 1,
            moodColor = 0xFF00FF,
            moodLabel = "Happy",
            notes = "Good day",
            lat = 52.245,
            lng = -7.139,
            zoom = 15f,
            imageUri = null,
            date = "2025-03-10"
        )

        assertTrue(mood.validate())
    }

    @Test
    fun blankMoodLabel_ReturnsFalse() {
        val mood = MoodEntryModel(
            moodColor = 123,
            moodLabel = "",
            lat = 52.0,
            lng = -7.0
        )

        assertFalse(mood.validate())
    }

    @Test
    fun zeroMoodColor_ReturnsFalse() {
        val mood = MoodEntryModel(
            moodColor = 0,
            moodLabel = "Sad",
            lat = 52.0,
            lng = -7.0
        )

        assertFalse(mood.validate())
    }

    @Test
    fun zeroLatitude_ReturnsFalse() {
        val mood = MoodEntryModel(
            moodColor = 123,
            moodLabel = "Calm",
            lat = 0.0,
            lng = -7.0
        )

        assertFalse(mood.validate())
    }

    @Test
    fun zeroLongitude_ReturnsFalse() {
        val mood = MoodEntryModel(
            moodColor = 123,
            moodLabel = "Angry",
            lat = 52.0,
            lng = 0.0
        )

        assertFalse(mood.validate())
    }

    @Test
    fun notesAreOptional_ValidationStillPasses() {
        val mood = MoodEntryModel(
            moodColor = 123,
            moodLabel = "Neutral",
            notes = "",
            lat = 52.0,
            lng = -7.0
        )

        assertTrue(mood.validate())
    }
}
