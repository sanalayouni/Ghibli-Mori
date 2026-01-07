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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import sana.ghiblimori.adapter.ProductAdapter
import sana.ghiblimori.databinding.FragmentMarketplaceBinding
import sana.ghiblimori.model.Product

class MarketplaceFragment : Fragment() {

    companion object {
        private const val TAG = "MarketplaceFragment"
    }

    private var _binding: FragmentMarketplaceBinding? = null
    private val binding get() = _binding!!

    private val colors = listOf("#FFD700", "#F8BBD0", "#BBDEFB", "#C5E1A5")
    private lateinit var db: FirebaseFirestore

    private lateinit var productAdapter: ProductAdapter
    private var allProducts = listOf<Product>()

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

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()

        // Setup RecyclerView
        setupRecyclerView()

        // Load categories and products
        addAllButton()
        fetchCategories()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())
        binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productsRecyclerView.adapter = productAdapter
    }

    private fun fetchProducts() {
        showLoading(true)

        db.collection("Products")
            .get()
            .addOnSuccessListener { documents ->
                val products = mutableListOf<Product>()

                Log.d(TAG, "✅ Fetched ${documents.size()} products from Firebase")

                for (document in documents) {
                    try {
                        // Handle price as either String or Number
                        val priceValue = when (val price = document.get("price")) {
                            is Number -> price.toDouble()
                            is String -> price.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }

                        val product = Product(
                            id = document.id,
                            name = document.getString("name") ?: "",
                            description = document.getString("description") ?: "",
                            price = priceValue,
                            imageUrl = document.getString("imageUrl") ?: "",
                            categoryId = document.getString("categoryId") ?: ""
                        )
                        products.add(product)

                        Log.d(TAG, "📦 ${product.name} - ${product.price}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error parsing product: ${e.message}")
                        e.printStackTrace()
                    }
                }

                allProducts = products
                Log.d(TAG, "📊 About to display ${products.size} products")
                displayProducts(products)
                showLoading(false)

            }
            .addOnFailureListener { e ->
                Log.e(TAG, "❌ Error fetching products: ${e.message}")
                showLoading(false)
                showEmptyState(true)
                Toast.makeText(requireContext(), "Error loading products", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchProductsByCategory(categoryId: String) {
        showLoading(true)

        db.collection("Products")
            .whereEqualTo("categoryId", categoryId)
            .get()
            .addOnSuccessListener { documents ->
                val products = documents.mapNotNull { doc ->
                    try {
                        // Handle price as either String or Number
                        val priceValue = when (val price = doc.get("price")) {
                            is Number -> price.toDouble()
                            is String -> price.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }

                        Product(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = priceValue,
                            imageUrl = doc.getString("imageUrl") ?: "",
                            categoryId = doc.getString("categoryId") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                Log.d(TAG, "✅ Found ${products.size} products in category")
                displayProducts(products)
                showLoading(false)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error filtering: ${e.message}")
                showLoading(false)
            }
    }

    private fun displayProducts(products: List<Product>) {
        Log.d(TAG, "📱 displayProducts() called with ${products.size} products")

        if (products.isEmpty()) {
            Log.d(TAG, "⚠️ Products list is EMPTY - showing empty state")
            showEmptyState(true)
        } else {
            Log.d(TAG, "✅ Updating adapter with ${products.size} products")
            showEmptyState(false)
            productAdapter.updateData(products)
            Log.d(TAG, "✅ Adapter updated successfully")
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.productsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateText.visibility = if (show) View.VISIBLE else View.GONE
        binding.productsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun addAllButton() {
        val allButton = Button(requireContext())
        allButton.text = "All"
        allButton.setTextColor(Color.BLACK)
        allButton.setBackgroundColor(Color.parseColor("#FFD700"))

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.rightMargin = 24
        allButton.layoutParams = params

        allButton.setOnClickListener {
            Toast.makeText(requireContext(), "Showing all items", Toast.LENGTH_SHORT).show()
            displayProducts(allProducts)
        }

        binding.categoriesContainer.addView(allButton)
    }

    private fun fetchCategories() {
        db.collection("categories")
            .get()
            .addOnSuccessListener { documents ->
                var colorIndex = 0
                for (document in documents) {
                    val categoryName = document.getString("name") ?: "Unknown"
                    val categoryId = document.id
                    val color = document.getString("color") ?: colors[colorIndex % colors.size]

                    createCategoryButton(categoryName, categoryId, color)
                    colorIndex++
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error getting categories", error)
            }
    }

    private fun createCategoryButton(name: String, categoryId: String, colorHex: String) {
        val button = Button(requireContext())
        button.text = name
        button.setTextColor(Color.BLACK)

        try {
            button.setBackgroundColor(Color.parseColor(colorHex))
        } catch (e: Exception) {
            button.setBackgroundColor(Color.parseColor("#F8BBD0"))
        }

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.rightMargin = 24
        button.layoutParams = params

        button.setOnClickListener {
            Toast.makeText(requireContext(), "Selected: $name", Toast.LENGTH_SHORT).show()
            fetchProductsByCategory(categoryId)
        }

        binding.categoriesContainer.addView(button)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}