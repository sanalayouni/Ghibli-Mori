package sana.ghiblimori.ui.home


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sana.ghiblimori.ItemDetailActivity
import sana.ghiblimori.R
import sana.ghiblimori.databinding.ActivityMarketplaceBinding

class MarketplaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMarketplaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketplaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example: click on first product card
        binding.itemCard1.setOnClickListener {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra("ITEM_NAME", "Totoro Plush")
                putExtra("ITEM_PRICE", "$34.99")
                putExtra("ITEM_IMAGE_RES", R.drawable.figure1)
            }
            startActivity(intent)
        }
    }
}