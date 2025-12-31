package sana.ghiblimori.data
import com.google.firebase.firestore.FirebaseFirestore
import sana.ghiblimori.data.Fire.db
import sana.ghiblimori.model.Category

class CategoryController {
    fun getCategories(onResult: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                val categories = result.map { it.toObject(Category::class.java) }
                onResult(categories)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}