package ie.setu.moodjournal.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ie.setu.moodjournal.R
import ie.setu.moodjournal.main.MainApp

class MoodListActivity : AppCompatActivity() {

    private lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_list)
        app = application as MainApp
        }
    }