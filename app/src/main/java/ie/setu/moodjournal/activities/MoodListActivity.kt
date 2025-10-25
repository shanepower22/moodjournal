package ie.setu.moodjournal.activities

import MoodAdapter
import MoodDropdownListener
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.moodjournal.R
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.databinding.ActivityMoodListBinding
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i

class MoodListActivity : AppCompatActivity(), MoodDropdownListener, AdapterView.OnItemSelectedListener {


    private lateinit var app: MainApp

    private lateinit var binding: ActivityMoodListBinding

    private lateinit var adapter: MoodAdapter

    private lateinit var allMoods: List<MoodEntryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MoodAdapter(app.moodEntries.findAll().toMutableList(), this)
        binding.recyclerView.adapter = adapter
        allMoods = app.moodEntries.findAll()
        setupFilterSpinner()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val intent = Intent(this, AddMoodActivity::class.java)
                moodResultLaunch.launch(intent)
                i("Add / edit mood button pressed")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onEditClick(mood: MoodEntryModel) {
        val intent = Intent(this, AddMoodActivity::class.java)
        intent.putExtra("mood_edit", mood)
        moodResultLaunch.launch(intent)
    }

    override fun onDeleteClick(mood: MoodEntryModel) {
        app.moodEntries.delete(mood)
        adapter.notifyDataSetChanged()
        i("Deleted mood ${mood}")
    }


    private val moodResultLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                adapter.notifyDataSetChanged()
                i("Mood list refreshed after add / edit")
            }
        }

    //source for spinner implementation: https://developer.android.com/develop/ui/views/components/spinner#kotlin
    private fun setupFilterSpinner() {
        val spinner: Spinner = binding.moodFilterSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.moodLabelsSpinner,
            android.R.layout.simple_spinner_item
        )

        .also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val selectedLabel = parent.getItemAtPosition(position).toString()
        filterMoods(selectedLabel)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // if nothing selected , show all moods
        filterMoods("All")
    }

    private fun filterMoods(label: String) {
        val filtered = if (label == "All") {
            allMoods
        } else {
            allMoods.filter { it.moodLabel == label }
        }
        adapter.updateList(filtered)
    }


}


