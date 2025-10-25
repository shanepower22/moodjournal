package ie.setu.moodjournal.models


interface MoodStore {
    fun findAll(): List<MoodEntryModel>
    fun create(moodEntry: MoodEntryModel)

    fun update(moodEntry: MoodEntryModel)

    fun delete(moodEntry: MoodEntryModel)
}
