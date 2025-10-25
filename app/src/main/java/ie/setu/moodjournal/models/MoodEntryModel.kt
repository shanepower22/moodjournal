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
    var date: LocalDate = LocalDate.now() ) : Parcelable {

    fun validate(): Boolean {
        if (moodLabel.isBlank()) return false
        if (notes.isBlank()) return false
        if (moodColor == 0) return false // 0 means unselected
        if (date.isAfter(LocalDate.now())) return false
        return true
    }
}
