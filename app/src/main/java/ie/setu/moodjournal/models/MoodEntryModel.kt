package ie.setu.moodjournal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import kotlinx.serialization.Serializable
@Parcelize
@Serializable
data class MoodEntryModel(
    var id: Long = 0,
    var moodColor: Int = 0,
    var moodLabel: String = "",
    var notes: String = "",
    @Serializable(with = LocalDateSerializer::class) //local date not serializable, using custom class
    var date: LocalDate = LocalDate.now() ) : Parcelable

