package sana.ghiblimori.ui.home

import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import sana.ghiblimori.R

class HomeActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val gifBackground = findViewById<ImageView>(R.id.gifBackground)
        val fadeText = findViewById<TextView>(R.id.fadeText)

        // Load GIF
        Glide.with(this)
            .asGif()
            .load(R.drawable.home)
            .into(gifBackground)

        // Play fade animation
        val fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_out)
        fadeText.visibility = TextView.VISIBLE
        fadeText.startAnimation(fadeAnim)

        // Play sound
        mediaPlayer = MediaPlayer.create(this, R.raw.intro)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }



}
