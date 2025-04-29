package com.example.budgettrackingapp.ui


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivitySetGoalBinding
import com.example.budgettrackingapp.model.Goal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SetGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetGoalBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.btnSaveGoal.setOnClickListener {
            saveGoal()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun saveGoal() {
        val minText = binding.etMinGoal.text.toString().trim()
        val maxText = binding.etMaxGoal.text.toString().trim()

        if (minText.isEmpty() || maxText.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return
        }

        val minAmount = minText.toDoubleOrNull()
        val maxAmount = maxText.toDoubleOrNull()

        if (minAmount == null || maxAmount == null) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.goalDao().insert(
                    Goal(
                        userId = 1, // Placeholder user ID, update if you implement real user sessions
                        minAmount = minAmount,
                        maxAmount = maxAmount
                    )
                )
            }
            Toast.makeText(this@SetGoalActivity, "Goal saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
