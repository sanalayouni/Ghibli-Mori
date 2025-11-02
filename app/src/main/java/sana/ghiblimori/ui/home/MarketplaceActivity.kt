package sana.ghiblimori.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sana.ghiblimori.R

class MarketplaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This connects the Kotlin file to your XML design
        setContentView(R.layout.activity_marketplace)

    }
}
