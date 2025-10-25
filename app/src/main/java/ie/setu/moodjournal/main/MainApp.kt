package ie.setu.moodjournal.main

import android.app.Application
import android.graphics.Color
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber
import timber.log.Timber.i
import java.time.LocalDate
import androidx.core.graphics.toColorInt
import ie.setu.moodjournal.models.MoodMemStore

class MainApp : Application() {

    val moodEntries = MoodMemStore()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Mood Entry App started")
        moodEntries.create(MoodEntryModel("#2E7D32".toColorInt(), "great", "Passed exam", LocalDate.now()))
        moodEntries.create(MoodEntryModel("#C62828".toColorInt(), "awful", "Car crash", LocalDate.of(2025, 10,20)))

    }
}
