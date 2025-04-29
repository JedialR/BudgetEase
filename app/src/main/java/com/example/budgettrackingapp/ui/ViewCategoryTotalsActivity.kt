package com.example.budgettrackingapp.ui



import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettrackingapp.database.AppDatabase
import com.example.budgettrackingapp.databinding.ActivityViewCategoryTotalsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ViewCategoryTotalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewCategoryTotalsBinding
    private lateinit var db: AppDatabase
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewCategoryTotalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        setupDatePickers()

        binding.btnFetchTotals.setOnClickListener {
            fetchCategoryTotals()
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

    private fun fetchCategoryTotals() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getAllCategories()
            }

            val totalsList = withContext(Dispatchers.IO) {
                categories.map { category ->
                    val total = db.expenseDao().getTotalByCategory(category.id, startCalendar.time, endCalendar.time) ?: 0.0
                    "${category.name}: \$${"%.2f".format(total)}"
                }
            }

            val adapter = ArrayAdapter(this@ViewCategoryTotalsActivity, android.R.layout.simple_list_item_1, totalsList)
            binding.listCategoryTotals.adapter = adapter
        }
    }
}
