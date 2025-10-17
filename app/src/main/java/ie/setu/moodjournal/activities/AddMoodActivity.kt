package ie.setu.moodjournal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.moodjournal.databinding.ActivityAddmoodentryBinding
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i

class AddMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddmoodentryBinding
    private lateinit var app : MainApp
    var moodEntry = MoodEntryModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddmoodentryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp
        i("Add Mood activity started")

        binding.btnAdd.setOnClickListener {
            i("Add Button pressed")
        }
    }

}