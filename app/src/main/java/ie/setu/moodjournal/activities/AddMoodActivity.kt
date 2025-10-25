package ie.setu.moodjournal.activities

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ie.setu.moodjournal.R
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
    private var selectedColor: Int = 0
    private var selectedLabel: String = ""

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
        setMoodColorListeners()

        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent.hasExtra("mood_edit")) {
            moodEntry = intent.extras?.getParcelable("mood_edit")!!
            binding.moodNotes.setText(moodEntry.notes)
            binding.dateText.setText(moodEntry.date.toString())

        }

        binding.btnAdd.setOnClickListener {
            moodEntry.notes = binding.moodNotes.text.toString()
            moodEntry.moodColor = selectedColor
            moodEntry.moodLabel = selectedLabel
            moodEntry.date = selectedDate
            val duplicate = app.moodEntries.findAll().find { moodEntry -> moodEntry.date == selectedDate }
            if (duplicate != null) {
                Snackbar.make(it, "Mood was already added for this date!", Snackbar.LENGTH_LONG)
                    .show()
                i("Duplicated mood not added for date: ${selectedDate}")
            } else if (selectedDate > LocalDate.now()) {
                Snackbar.make(it, "Cannot add a mood in the future!", Snackbar.LENGTH_LONG).show()
                i("Mood not added, selected date ${selectedDate} is in the future")
            } else if (moodEntry.moodColor == 0) {
                Snackbar.make(it, "Please Select Your Mood", Snackbar.LENGTH_LONG)
                    .show()
                i("Mood not added as mood color not selected:  ${moodEntry} ")

            } else {
                app.moodEntries.create(moodEntry.copy())
                i("add Button Pressed: ${moodEntry}")

                setResult(RESULT_OK)
                finish()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun setMoodColorListeners() {
        val moodViews = listOf(
            binding.colorAwful,
            binding.colorBad,
            binding.colorNeutral,
            binding.colorGood,
            binding.colorGreat
        )

        val moodColors = listOf(
            R.color.awful,
            R.color.bad,
            R.color.neutral,
            R.color.good,
            R.color.great
        )

        val moodLabels = listOf(
            "Awful",
            "Bad",
            "Neutral",
            "Good",
            "Great"
        )

        moodViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                selectedColor = ContextCompat.getColor(this, moodColors[index])
                selectedLabel = moodLabels[index]

                highlightSelectedMood(view, moodViews)
                i("Mood selected: $selectedLabel with color $selectedColor")
            }
        }
    }

    private fun highlightSelectedMood(selectedView: View, allViews: List<View>) {
        allViews.forEach { it.alpha = 0.4f } // dim all
        selectedView.alpha = 1.0f // highlight selected
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