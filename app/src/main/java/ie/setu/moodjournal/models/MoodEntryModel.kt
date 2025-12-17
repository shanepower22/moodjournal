package ie.setu.moodjournal.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import kotlinx.serialization.Serializable

data class MoodEntryModel(
    var id: Long = 0,
    var moodColor: Int = 0,
    var moodLabel: String = "",
    var notes: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var imageUri: String? = null,
    var date: String = "") {

    fun validate(): Boolean {
        if (moodLabel.isBlank()) return false
        if (moodColor == 0) return false // 0 means unselected
        if (lat == 0.0) return false
        if (lng == 0.0) return false

        return true
    }
}

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable