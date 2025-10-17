package ie.setu.moodjournal.models


data class MoodEntryModel(
    var id: Long = 0,
    var moodColor: Int = 0,
    var notes: String = "",
    var date: String = ""
)
