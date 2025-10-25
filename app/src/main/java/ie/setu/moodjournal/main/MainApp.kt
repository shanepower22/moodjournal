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

    lateinit var moodEntries: MoodMemStore

    override fun onCreate() {
        super.onCreate()
        moodEntries = MoodMemStore(this)
        Timber.plant(Timber.DebugTree())
        i("Mood Entry App started")


    }
}
