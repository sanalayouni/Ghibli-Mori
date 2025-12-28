package sana.ghiblimori

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartActivity : AppCompatActivity() {
    private lateinit var textureView: TextureView
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val title = findViewById<TextView>(R.id.titleText)
        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            // Create an Intent to move from StartActivity to SignupActivity
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }


// Fade ONLY the title
        title.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade)
        )

// Float ONLY the button
        startButton.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.floating)
        )

        textureView = findViewById(R.id.textureView)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {

            override fun onSurfaceTextureAvailable(surfaceTexture: android.graphics.SurfaceTexture, width: Int, height: Int) {

                val videoUri = Uri.parse(
                    "android.resource://$packageName/${R.raw.welcome}"
                )

                mediaPlayer = MediaPlayer()
                mediaPlayer.setSurface(Surface(surfaceTexture))
                mediaPlayer.setDataSource(this@StartActivity, videoUri)

                mediaPlayer.setOnPreparedListener { mp ->
                    scaleVideo(mp.videoWidth, mp.videoHeight, width, height)
                    mp.isLooping = true
                    mp.start()
                }

                mediaPlayer.prepareAsync()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: android.graphics.SurfaceTexture,
                width: Int,
                height: Int
            ) {}

            override fun onSurfaceTextureDestroyed(surface: android.graphics.SurfaceTexture): Boolean {
                mediaPlayer.release()
                return true
            }

            override fun onSurfaceTextureUpdated(surface: android.graphics.SurfaceTexture) {}
        }
    }

    private fun scaleVideo(videoWidth: Int, videoHeight: Int, viewWidth: Int, viewHeight: Int) {
        val videoRatio = videoWidth.toFloat() / videoHeight
        val viewRatio = viewWidth.toFloat() / viewHeight

        val scaleX: Float
        val scaleY: Float

        if (videoRatio > viewRatio) {
            scaleX = videoRatio / viewRatio
            scaleY = 1f
        } else {
            scaleX = 1f
            scaleY = viewRatio / videoRatio
        }

        textureView.scaleX = scaleX
        textureView.scaleY = scaleY
    }
}