package com.example.budgettrackingapp.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budgettrackingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example: Setup button listeners for navigation (to be added)
        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        binding.btnSetGoal.setOnClickListener {
            val intent = Intent(this, SetGoalActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewExpenses.setOnClickListener {
            val intent = Intent(this, ViewExpensesActivity::class.java)
            startActivity(intent)
        }

        binding.btnViewCategoryTotals.setOnClickListener {
            val intent = Intent(this, ViewCategoryTotalsActivity::class.java)
            startActivity(intent)
        }
    }
}