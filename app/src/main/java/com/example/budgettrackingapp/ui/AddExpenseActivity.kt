package com.example.budgettrackingapp.ui


import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivityAddExpenseBinding
import com.example.budgettrackingapp.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var db: AppDatabase
    private val calendar = Calendar.getInstance()
    private var selectedCategoryId: Int? = null
    private var selectedPhotoUri: Uri? = null

    private val selectPhotoLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedPhotoUri = it
            binding.imagePreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        setupCategorySpinner()
        setupDatePicker()

        binding.btnSaveExpense.setOnClickListener {
            saveExpense()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSelectPhoto.setOnClickListener {
            selectPhotoFromGallery()
        }
    }

    private fun setupCategorySpinner() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getAllCategories()
            }
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter

            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCategoryId = categories[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCategoryId = null
                }
            }
        }
    }

    private fun setupDatePicker() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        binding.etDate.setText(dateFormat.format(calendar.time))

        binding.etDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding.etDate.setText(dateFormat.format(calendar.time))
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun selectPhotoFromGallery() {
        selectPhotoLauncher.launch(arrayOf("image/*"))
    }

    private fun saveExpense() {
        val description = binding.etDescription.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()
        val date = calendar.time

        if (description.isEmpty() || amountText.isEmpty() || selectedCategoryId == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.expenseDao().insert(
                    Expense(
                        amount = amount,
                        date = date,
                        startTime = date,
                        endTime = date,
                        description = description,
                        categoryId = selectedCategoryId!!,
                        photo = selectedPhotoUri?.toString()
                    )
                )
            }
            Toast.makeText(this@AddExpenseActivity, "Expense saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

