package sana.ghiblimori

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class test : AppCompatActivity() {
    private val TAG = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_test)

        testFetchCategories()
    }

    private fun testFetchCategories() {
        val db = FirebaseFirestore.getInstance()

        db.collection("categories")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    Log.w(TAG, "Collection is empty")
                    Toast.makeText(
                        this,
                        "⚠️ Collection is EMPTY",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.d(TAG, "Fetched ${snapshot.size()} categories")

                    // Log each category
                    snapshot.documents.forEach { doc ->
                        Log.d(TAG, "Category ID: ${doc.id}, Data: ${doc.data}")
                    }

                    Toast.makeText(
                        this,
                        "✅ Fetched ${snapshot.size()} categories",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching categories", exception)
                Toast.makeText(
                    this,
                    "❌ Error: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}