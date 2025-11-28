package sana.ghiblimori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import sana.ghiblimori.databinding.ItemFigureBinding

class ItemDetailFragment : Fragment() {

    private var _binding: ItemFigureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemFigureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button click
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        // Add to Bag button - Navigate to Cart
        binding.addToBagButton.setOnClickListener {
            // Show a quick message
            Toast.makeText(requireContext(), "Added to bag!", Toast.LENGTH_SHORT).show()

            // Navigate to Cart Fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CartFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}