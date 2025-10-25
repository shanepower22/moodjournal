package ie.setu.moodjournal.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class MoodMemStore : MoodStore {

    val moodEntries = ArrayList<MoodEntryModel>()

    override fun findAll(): List<MoodEntryModel> {
        return moodEntries
    }

    override fun create(moodEntry: MoodEntryModel) {
        moodEntry.id = getId()
        moodEntries.add(moodEntry)
        logAll()
    }

    override fun update(moodEntry: MoodEntryModel) {
        var foundMood: MoodEntryModel? = moodEntries.find { m -> m.id == moodEntry.id }
        if (foundMood != null) {
            foundMood.notes = moodEntry.notes
            foundMood.moodColor = moodEntry.moodColor
            foundMood.date = moodEntry.date
            foundMood.moodLabel = moodEntry.moodLabel

            logAll()
        }
    }

    fun logAll() {
        moodEntries.forEach{ i("$it") }
    }
}