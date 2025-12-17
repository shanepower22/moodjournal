package ie.setu.moodjournal.models

import android.net.Uri
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
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var imageUri: String? = null,
    @Serializable(with = LocalDateSerializer::class) //local date not serializable, using custom class
    var date: LocalDate = LocalDate.now() ) : Parcelable {

    fun validate(): Boolean {
        if (moodLabel.isBlank()) return false
        if (moodColor == 0) return false // 0 means unselected
        if (date.isAfter(LocalDate.now())) return false
        if (lat == 0.0) return false
        if (lng == 0.0) return false

        return true
    }
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable