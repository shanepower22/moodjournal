package ie.setu.moodjournal.models
import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}
class MoodMemStore(private val context: Context) : MoodStore {

    val moodEntries = ArrayList<MoodEntryModel>()
    val filename = "moods.json"

    init {
        load()
    }

    override fun findAll(): List<MoodEntryModel> {
        return moodEntries
    }

    override fun create(moodEntry: MoodEntryModel) {
        require(moodEntry.validate()) {
            "Invalid mood entry data: $moodEntry"
        }
        if (moodEntries.any { it.date == moodEntry.date }) {
            throw IllegalStateException("Duplicate mood entry for date: ${moodEntry.date}")
        }
        moodEntry.id = getId()
        moodEntries.add(moodEntry)
        logAll()
        save()
    }

    override fun update(moodEntry: MoodEntryModel) {
        require(moodEntry.validate()) {
            "Invalid mood entry data: $moodEntry"
        }


        var foundMood: MoodEntryModel? = moodEntries.find { m -> m.id == moodEntry.id }
        if (foundMood != null) {
            foundMood.notes = moodEntry.notes
            foundMood.moodColor = moodEntry.moodColor
            foundMood.date = moodEntry.date
            foundMood.moodLabel = moodEntry.moodLabel

            logAll()
            save()
        }
    }



    override fun delete(moodEntry: MoodEntryModel) {
        moodEntries.removeIf { it.id == moodEntry.id }
        i("Deleted mood entry: $moodEntry")
        logAll()
        save()
        }

    fun logAll() {
        moodEntries.forEach{ i("$it") }
    }

    private fun save() {
        val jsonString = Json.encodeToString(moodEntries)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
        i("Moods saved to JSON")
    }

    private fun load() {
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            val jsonString = file.readText()
            moodEntries.addAll(Json.decodeFromString(jsonString))
            i("Moods loaded from JSON")
        }
    }
}