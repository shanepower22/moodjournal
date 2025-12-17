package ie.setu.moodjournal.models
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}


class MoodFirestoreStore : MoodStore {

    private val db = Firebase.firestore
    private val moodsRef = db.collection("moods")

    private val moodEntries = ArrayList<MoodEntryModel>()

    override fun findAll(): List<MoodEntryModel> {
        return moodEntries
    }

    fun load(onComplete: () -> Unit) {
        moodsRef.get()
            .addOnSuccessListener { result ->
                moodEntries.clear()
                for (doc in result) {
                    val mood = doc.toObject(MoodEntryModel::class.java)
                    moodEntries.add(mood)
                }
                onComplete()
            }
            .addOnFailureListener {
                onComplete()
            }
    }

    override fun create(moodEntry: MoodEntryModel) {
        require(moodEntry.validate()) {
            "Invalid mood entry data: $moodEntry"
        }
        if (moodEntries.any { it.date == moodEntry.date }) {
            throw IllegalStateException("Duplicate mood entry for date: ${moodEntry.date}")
        }
        moodEntry.id = System.currentTimeMillis()

        moodsRef.document(moodEntry.id.toString())
            .set(moodEntry)

        moodEntries.add(moodEntry)
    }

    override fun update(moodEntry: MoodEntryModel) {
        require(moodEntry.validate()) {
            "Invalid mood entry data: $moodEntry"
        }
        moodsRef.document(moodEntry.id.toString())
            .set(moodEntry)

        val index = moodEntries.indexOfFirst { it.id == moodEntry.id }
        if (index != -1) {
            moodEntries[index] = moodEntry
        }
    }

    override fun delete(moodEntry: MoodEntryModel) {
        moodsRef.document(moodEntry.id.toString())
            .delete()

        moodEntries.removeIf { it.id == moodEntry.id }
    }

    override fun findById(id: Long?, callback: (MoodEntryModel?) -> Unit){
        moodsRef.document(id.toString())
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    callback(doc.toObject(MoodEntryModel::class.java))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

}
