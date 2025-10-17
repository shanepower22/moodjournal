package ie.setu.moodjournal.activities

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.setu.moodjournal.databinding.ActivityAddmoodentryBinding
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i
import java.time.LocalDate

class AddMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddmoodentryBinding
    private lateinit var app : MainApp
    var moodEntry = MoodEntryModel()
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddmoodentryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp
        i("Add Mood activity started")

        binding.dateText.text = selectedDate.toString()
        binding.dateText.setOnClickListener {
            showDatePicker()
        }


        binding.btnAdd.setOnClickListener {
            moodEntry.notes = binding.moodNotes.text.toString()
            moodEntry.moodColor = 0
            moodEntry.date = selectedDate
            app.moodEntries.add(moodEntry.copy())

            i("add Button Pressed: ${moodEntry}")
        }


    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                binding.dateText.text = selectedDate.toString()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
}