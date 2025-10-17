package ie.setu.moodjournal.main

import android.app.Application
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {
    val moodEntries = ArrayList<MoodEntryModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Mood Entry App started")
    }
}
