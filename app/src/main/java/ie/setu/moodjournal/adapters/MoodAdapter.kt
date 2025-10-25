import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ie.setu.moodjournal.R
import ie.setu.moodjournal.databinding.CardMoodBinding
import ie.setu.moodjournal.models.MoodEntryModel



interface MoodDropdownListener { //AI was used here to help implement the popup menu functionality
    fun onEditClick(mood: MoodEntryModel)
    fun onDeleteClick(mood: MoodEntryModel)
}

class MoodAdapter (private var moodEntries: MutableList<MoodEntryModel>,
    private val listener: MoodDropdownListener)
    : RecyclerView.Adapter<MoodAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val mood = moodEntries[holder.adapterPosition]
        holder.bind(mood, listener)
    }

    override fun getItemCount(): Int = moodEntries.size

    fun updateList(newList: List<MoodEntryModel>) {
        this.moodEntries.clear()
        this.moodEntries.addAll(newList)
        notifyDataSetChanged()
    }
    class MainHolder(
        private val binding: CardMoodBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mood: MoodEntryModel, listener: MoodDropdownListener) {
            binding.moodLabel.text = binding.root.context.getString(
                R.string.moodColorLabel,
                mood.moodLabel
            )
            binding.moodColorIndicator.setBackgroundColor(mood.moodColor)
            binding.moodNotes.text = mood.notes
            binding.moodDate.text = mood.date.toString()

            //AI was used here to help implement the popup menu functionality
            binding.moodOptionsMenu.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.inflate(R.menu.menu_mood_options)
                popup.setOnMenuItemClickListener { item ->

                    when (item.itemId) {
                        R.id.action_edit -> {
                            listener.onEditClick(mood)
                            true
                        }

                        R.id.action_delete -> {
                            listener.onDeleteClick(mood)
                            true
                        }

                        else -> false
                    }

                }
                popup.show()
            }

        }
    }
}
