package ie.setu.moodjournal.activities

import MoodAdapter
import MoodListener
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

class MoodListActivity : AppCompatActivity(), MoodListener {


    private lateinit var app: MainApp

    private lateinit var binding: ActivityMoodListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MoodAdapter(app.moodEntries.findAll(),this)
        MoodAdapter(app.moodEntries.findAll(),this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, AddMoodActivity::class.java)
                getResult.launch(launcherIntent)
                i("Add mood button pressed")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.moodEntries.findAll().size)
            }
        }
    override fun onMoodClick(mood: MoodEntryModel) {
        val launcherIntent = Intent(this, AddMoodActivity::class.java)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.moodEntries.findAll().size)
            }
        }
}
