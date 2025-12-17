package ie.setu.moodjournal.main

import android.app.Application
import android.graphics.Color
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber
import timber.log.Timber.i
import java.time.LocalDate
import androidx.core.graphics.toColorInt
import ie.setu.moodjournal.models.MoodFirestoreStore

class MainApp : Application() {

    lateinit var moodEntries: MoodFirestoreStore

    override fun onCreate() {
        super.onCreate()
        moodEntries = MoodFirestoreStore()
        Timber.plant(Timber.DebugTree())
        i("Mood Entry App started")


    }
}
