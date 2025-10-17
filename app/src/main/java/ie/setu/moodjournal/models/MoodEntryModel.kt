package ie.setu.moodjournal.models

import java.time.LocalDate

data class MoodEntryModel(
    var id: Long = 0,
    var moodColor: Int = 0,
    var notes: String = "",
    var date: LocalDate = LocalDate.now())

