package sana.ghiblimori

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signup)



        val loginLink: TextView = findViewById(R.id.loginLink)

        loginLink.setOnClickListener {
            // Open Login Activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // 1️⃣ Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 2️⃣ Link XML views
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        signupBtn = findViewById(R.id.signupBtn)

        // 3️⃣ Button click
        signupBtn.setOnClickListener {
            signupUser()
        }
    }

    private fun signupUser() {
        val userEmail = email.text.toString().trim()
        val userPassword = password.text.toString().trim()

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (userPassword.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    finish() // or go to Login/Home
                } else {
                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
