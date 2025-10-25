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
    import android.widget.PopupMenu
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

        private lateinit var allMoods: List<MoodEntryModel>

        private var currentFilter: String = "All" // keep track of current filter applied


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMoodListBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.toolbarAdd.title = title
            setSupportActionBar(binding.toolbarAdd)
            app = application as MainApp

            // recycler view
            val layoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = layoutManager
            adapter = MoodAdapter(app.moodEntries.findAll().toMutableList(), this)
            binding.recyclerView.adapter = adapter
            allMoods = app.moodEntries.findAll() // load all moods from memstore
        }

        //toolbar menu
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
            return super.onCreateOptionsMenu(menu)
        }

        //toolbar menu options
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_add -> {
                    val intent = Intent(this, AddMoodActivity::class.java)
                    moodResultLaunch.launch(intent)
                    i("Add / edit mood button pressed")
                }

                R.id.item_filter -> {
                    showFilterPopup(findViewById(R.id.item_filter))
                    i("Filter button pressed")
                }
            }

            return super.onOptionsItemSelected(item)
        }

        //filter menu
        private fun showFilterPopup(view: View) {
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.filter_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem ->
                val selectedLabel = menuItem.title.toString()
                filterMoods(selectedLabel)
                true
            }

            popup.show()
        }

        override fun onEditClick(mood: MoodEntryModel) {
            val intent = Intent(this, AddMoodActivity::class.java)
            intent.putExtra("mood_edit", mood)
            moodResultLaunch.launch(intent)
        }

        override fun onDeleteClick(mood: MoodEntryModel) {
            app.moodEntries.delete(mood)
            allMoods = app.moodEntries.findAll()
            adapter.updateList(allMoods)
            filterMoods(currentFilter)
            i("Deleted mood ${mood}")
        }


        // handler for result from add / edit mood
        private val moodResultLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    allMoods = app.moodEntries.findAll()
                    filterMoods(currentFilter)
                    i("Mood list refreshed after add / edit")
                }
            }




        private fun filterMoods(label: String) {
            currentFilter = label
            val filtered = if (label == "All") {
                allMoods
            } else {
                allMoods.filter { it.moodLabel == label }
            }
            adapter.updateList(filtered)
        }


    }


