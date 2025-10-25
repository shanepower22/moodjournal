package ie.setu.moodjournal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class MoodEntryModel(
    var moodColor: Int = 0,
    var moodLabel: String = "",
    var notes: String = "",
    var date: LocalDate = LocalDate.now()) : Parcelable

