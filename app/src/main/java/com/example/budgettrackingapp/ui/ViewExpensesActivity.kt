package com.example.budgettrackingapp.ui



import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivityViewExpensesBinding
import com.example.budgettrackingapp.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ViewExpensesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewExpensesBinding
    private lateinit var db: AppDatabase
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        setupDatePickers()

        binding.btnFetchExpenses.setOnClickListener {
            fetchExpenses()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupDatePickers() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        binding.etStartDate.setText(dateFormat.format(startCalendar.time))
        binding.etEndDate.setText(dateFormat.format(endCalendar.time))

        binding.etStartDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                startCalendar.set(year, month, dayOfMonth)
                binding.etStartDate.setText(dateFormat.format(startCalendar.time))
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.etEndDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                endCalendar.set(year, month, dayOfMonth)
                binding.etEndDate.setText(dateFormat.format(endCalendar.time))
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun fetchExpenses() {
        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesBetween(startCalendar.time, endCalendar.time)
            }
            displayExpenses(expenses)
        }
    }

    private fun displayExpenses(expenses: List<Expense>) {
        if (expenses.isEmpty()) {
            binding.listExpenses.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOf("No expenses found"))
            return
        }

        val expenseDescriptions = expenses.map { expense ->
            if (expense.photo != null) {
                "${expense.date}: ${expense.description} - $${expense.amount} (Photo attached)"
            } else {
                "${expense.date}: ${expense.description} - $${expense.amount}"
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenseDescriptions)
        binding.listExpenses.adapter = adapter

        binding.listExpenses.setOnItemClickListener { _, _, position, _ ->
            val selectedExpense = expenses[position]
            selectedExpense.photo?.let { photoUriString ->
                showPhotoDialog(photoUriString)
            }
        }
    }

    private fun showPhotoDialog(photoUriString: String) {
        val imageView = ImageView(this)
        val photoUri = Uri.parse(photoUriString)

        try {
            contentResolver.takePersistableUriPermission(
                photoUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageView.setImageURI(photoUri)
        } catch (e: SecurityException) {
            e.printStackTrace()
            showErrorDialog("Cannot open this image. It may not be available anymore.")
            return
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorDialog("Something went wrong opening the image.")
            return
        }

        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        AlertDialog.Builder(this)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}