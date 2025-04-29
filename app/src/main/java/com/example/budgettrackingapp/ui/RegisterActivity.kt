package com.example.budgettrackingapp.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivityRegisterBinding
import com.example.budgettrackingapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(username, password)
            }
        }

        binding.btnBackToLogin.setOnClickListener {
            finish() // Go back to login screen
        }
    }

    private fun registerUser(username: String, password: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.userDao().insert(User(username = username, password = password))
            }
            Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }
}