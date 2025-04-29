package com.example.budgettrackingapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivityAddCategoryBinding
import com.example.budgettrackingapp.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.btnSaveCategory.setOnClickListener {
            val categoryName = binding.etCategoryName.text.toString().trim()

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            } else {
                saveCategory(categoryName)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun saveCategory(name: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.categoryDao().insert(Category(name = name))
            }
            Toast.makeText(this@AddCategoryActivity, "Category saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
