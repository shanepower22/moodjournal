package ie.setu.moodjournal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.moodjournal.R
import ie.setu.moodjournal.databinding.ActivityAddmoodentryBinding
import timber.log.Timber
import timber.log.Timber.i

class AddMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddmoodentryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddmoodentryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Timber.plant(Timber.DebugTree())
        i("Add Mood activity started")

        binding.btnAdd.setOnClickListener {
            i("Add Button pressed")
        }
    }

}