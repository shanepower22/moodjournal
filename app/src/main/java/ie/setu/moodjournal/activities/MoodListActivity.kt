package ie.setu.moodjournal.activities

import MoodAdapter
import MoodDropdownListener
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.moodjournal.R
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.databinding.ActivityMoodListBinding
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i

class MoodListActivity : AppCompatActivity(), MoodDropdownListener {


    private lateinit var app: MainApp

    private lateinit var binding: ActivityMoodListBinding

    private lateinit var adapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MoodAdapter(app.moodEntries.findAll(), this)
        binding.recyclerView.adapter = adapter
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
}


