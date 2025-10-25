package ie.setu.moodjournal.models

import timber.log.Timber.i

class MoodMemStore : MoodStore {

    val moodEntries = ArrayList<MoodEntryModel>()

    override fun findAll(): List<MoodEntryModel> {
        return moodEntries
    }

    override fun create(moodEntry: MoodEntryModel) {
        moodEntries.add(moodEntry)
        logAll()
    }

    fun logAll() {
        moodEntries.forEach{ i("$it") }
    }
}