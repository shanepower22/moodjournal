package ie.setu.moodjournal.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.moodjournal.R
import ie.setu.moodjournal.databinding.ActivityAddmoodentryBinding
import ie.setu.moodjournal.helpers.showImagePicker
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.models.Location
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i
import java.time.LocalDate
import androidx.core.net.toUri

class AddMoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddmoodentryBinding
    private lateinit var app: MainApp
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            //location = result.data!!.extras?.getParcelable("location",Location::class.java)!!
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    var location = Location(52.245696, -7.139102, 15f)

    var moodEntry = MoodEntryModel()

    private var selectedDate: LocalDate = LocalDate.now()
    private var selectedColor: Int = 0
    private var selectedLabel: String = ""
    private var edit = false
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddmoodentryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerMapCallback()
        registerImagePickerCallback()

        //setup image picker
        binding.setMoodImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }


        app = application as MainApp
        i("Add Mood activity started")
        // set text for initial date
        binding.dateText.text = selectedDate.toString()
        binding.dateText.setOnClickListener {
            showDatePicker() //show date picker when clicked
        }
        setMoodColorListeners()

        setSupportActionBar(binding.toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //check if editing and populate the fields if so
        val moodId = intent.extras?.getLong("mood_id")
        if (moodId != null) {
            edit = true
            app.moodEntries.findById(moodId) { mood ->
                if (mood != null) {
                    moodEntry = mood
                    binding.moodNotes.setText(moodEntry.notes)
                    binding.dateText.setText(moodEntry.date)
                    binding.setMoodImage.setText(R.string.button_editImage)
                    binding.btnAdd.setText(R.string.menu_moodEdit)
                    Picasso.get()
                        .load(moodEntry.imageUri?.toUri())
                        .into(binding.moodImage)
                    selectedDate = LocalDate.parse(moodEntry.date)
                    selectedColor = moodEntry.moodColor
                    selectedLabel = moodEntry.moodLabel
                    location = Location(moodEntry.lat, moodEntry.lng, moodEntry.zoom)
                }
        }

        }

        //when set location pressed
        binding.moodLocation.setOnClickListener {
            i("Set Location Pressed")
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        //when add or edit button pressed
        binding.btnAdd.setOnClickListener {
            moodEntry.notes = binding.moodNotes.text.toString()
            moodEntry.moodColor = selectedColor
            moodEntry.moodLabel = selectedLabel
            moodEntry.date = selectedDate.toString()
            moodEntry.lat = location.lat
            moodEntry.lng = location.lng
            moodEntry.zoom = location.zoom
            moodEntry.imageUri = moodEntry.imageUri


            if (selectedDate > LocalDate.now()) {
                Snackbar.make(it, R.string.mood_future_date, Snackbar.LENGTH_LONG).show()
                i("Mood not added, selected date ${selectedDate} is in the future")
            } else if (moodEntry.moodColor == 0) {
                Snackbar.make(it, R.string.mood_not_selected, Snackbar.LENGTH_LONG)
                    .show()
                i("Mood not added as mood color not selected:  ${moodEntry} ")

            } else {
                if (edit) {
                    app.moodEntries.update(moodEntry)
                    i("Edit Button Pressed: ${moodEntry}")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    val duplicate =
                        app.moodEntries.findAll()
                            .find { moodEntry -> moodEntry.date == selectedDate.toString() }
                    if (duplicate != null) {
                        Snackbar.make(it, R.string.mood_duplicated_date, Snackbar.LENGTH_LONG)
                            .show()
                        i("Duplicated mood not added for date: ${selectedDate}")
                    } else {
                        app.moodEntries.create(moodEntry)
                        i("Add Button Pressed: ${moodEntry}")
                        setResult(RESULT_OK)
                        finish()
                    }
                }

            }
        }
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            val uri = result.data!!.data!!
                            moodEntry.imageUri = uri.toString()
                            Picasso.get()
                                .load(uri)
                                .into(binding.moodImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    //toolbar back button
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        android.R.id.home -> {
            finish() //close add activity
            return true
        }

    }
    return super.onOptionsItemSelected(item)
}

    //listeners for color options
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