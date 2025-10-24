import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.moodjournal.R
import ie.setu.moodjournal.databinding.CardMoodBinding
import ie.setu.moodjournal.models.MoodEntryModel


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