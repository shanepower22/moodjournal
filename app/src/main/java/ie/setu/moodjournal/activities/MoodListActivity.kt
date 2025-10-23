package ie.setu.moodjournal.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.moodjournal.R
import ie.setu.moodjournal.main.MainApp
import ie.setu.moodjournal.databinding.ActivityMoodListBinding
import ie.setu.moodjournal.databinding.CardMoodBinding
import ie.setu.moodjournal.models.MoodEntryModel
import timber.log.Timber.i

class MoodListActivity : AppCompatActivity() {


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
        binding.recyclerView.adapter = MoodAdapter(app.moodEntries)
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
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.moodEntries.size)
            }
        }

}
class MoodAdapter (private var moodEntries: List<MoodEntryModel>)
    : RecyclerView.Adapter<MoodAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val mood = moodEntries[holder.adapterPosition]
        holder.bind(mood)
    }

    override fun getItemCount(): Int = moodEntries.size

    class MainHolder(private val binding: CardMoodBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(mood: MoodEntryModel) {
            binding.moodLabel.text = binding.root.context.getString(
                R.string.moodColorLabel,
                mood.moodLabel
            )
            binding.moodColorIndicator.setBackgroundColor(mood.moodColor)
            binding.moodNotes.text = mood.notes
            binding.moodDate.text = mood.date.toString()
        }
            }
}