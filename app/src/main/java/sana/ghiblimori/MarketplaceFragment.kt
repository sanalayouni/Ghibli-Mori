package sana.ghiblimori

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import sana.ghiblimori.databinding.FragmentMarketplaceBinding

class MarketplaceFragment : Fragment() {

    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val colors = listOf("#FFD700", "#F8BBD0", "#BBDEFB", "#C5E1A5")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup product card click
        binding.productCard1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ItemDetailFragment())
                .addToBackStack(null)
                .commit()
        }

        // Load categories
        addAllButton()
        fetchCategories()
    }

    // Add the "All" button
    private fun addAllButton() {
        val allButton = Button(requireContext())
        allButton.text = "All"
        allButton.setTextColor(Color.BLACK)
        allButton.setBackgroundColor(Color.parseColor("#FFD700"))

        // Set button size and margin
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.rightMargin = 24
        allButton.layoutParams = params

        // When clicked
        allButton.setOnClickListener {
            Toast.makeText(requireContext(), "Showing all items", Toast.LENGTH_SHORT).show()
            // TODO: Show all products
        }

        // Add button to the container
        binding.categoriesContainer.addView(allButton)
    }

    // Get categories from Firestore
    private fun fetchCategories() {
        val db = FirebaseFirestore.getInstance()

        db.collection("categories")
            .get()
            .addOnSuccessListener { documents ->

                // Loop through each category
                var colorIndex = 0
                for (document in documents) {
                    // Get the category name
                    val categoryName = document.getString("name") ?: "Unknown"

                    // Get color (or use default)
                    val color = document.getString("color") ?: colors[colorIndex % colors.size]

                    // Create button
                    createCategoryButton(categoryName, color)

                    colorIndex++
                }

                Toast.makeText(
                    requireContext(),
                    "✅ Loaded ${documents.size()} categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(
                    requireContext(),
                    "❌ Error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("MarketplaceFragment", "Error getting categories", error)
            }
    }

    // Create one category button
    private fun createCategoryButton(name: String, colorHex: String) {
        val button = Button(requireContext())
        button.text = name
        button.setTextColor(Color.BLACK)

        // Set button color
        try {
            button.setBackgroundColor(Color.parseColor(colorHex))
        } catch (e: Exception) {
            button.setBackgroundColor(Color.parseColor("#F8BBD0"))
        }

        // Set button size and margin
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.rightMargin = 24
        button.layoutParams = params

        // When clicked
        button.setOnClickListener {
            Toast.makeText(requireContext(), "Selected: $name", Toast.LENGTH_SHORT).show()
            // TODO: Filter products by this category
        }

        // Add button to the container
        binding.categoriesContainer.addView(button)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}