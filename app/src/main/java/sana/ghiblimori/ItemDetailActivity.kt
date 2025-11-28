package sana.ghiblimori

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sana.ghiblimori.databinding.ItemFigureBinding   // auto-generated binding class from item_figure.xml

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var binding: ItemFigureBinding   // auto-generated from item_figure.xml

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemFigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Quantity logic
        var quantity = 1
        binding.quantityText.text = quantity.toString()

        binding.increaseButton.setOnClickListener {
            quantity++
            binding.quantityText.text = quantity.toString()
        }

        binding.decreaseButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.quantityText.text = quantity.toString()
            }
        }

        // Add to Bag button
        binding.addToBagButton.setOnClickListener {
            Toast.makeText(
                this,
                "${binding.figurePrice.text} added to bag (x$quantity)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}