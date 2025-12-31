package sana.ghiblimori.data

import com.google.firebase.firestore.FirebaseFirestore



object Fire {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}
