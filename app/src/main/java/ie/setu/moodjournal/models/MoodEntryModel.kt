package ie.setu.moodjournal.models

import java.time.LocalDate

data class MoodEntryModel(
    var moodColor: Int = 0,
    var moodLabel: String = "",
    var notes: String = "",
    var date: LocalDate = LocalDate.now())

