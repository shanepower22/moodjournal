package ie.setu.moodjournal.models

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class MoodEntryModelTest {

    @Test
    fun validMoodEntry_PassesValidation() {
        val mood = MoodEntryModel(
            moodColor = 123456,
            moodLabel = "Happy",
            notes = "Had a great day",
            date = LocalDate.now()
        )
        assertTrue(mood.validate())
    }

    @Test
    fun invalidMoodEntry_FailsValidation() {
        val mood = MoodEntryModel(
            moodColor = 0,
            moodLabel = "",
            notes = "",
            date = LocalDate.now()
        )
        assertFalse(mood.validate())
    }

    @Test
    fun moodEntry_DefaultDate_IsToday() {
        val mood = MoodEntryModel(
            moodColor = 123,
            moodLabel = "Calm",
            notes = "Just chilling"
        )
        assertEquals(LocalDate.now(), mood.date)
    }
}
