package com.example.budgettrackingapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgettrackingapp.model.Expense
import java.util.*

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getExpensesBetween(startDate: Date, endDate: Date): List<Expense>

    @Query("SELECT SUM(amount) FROM expenses WHERE categoryId = :categoryId AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalByCategory(categoryId: Int, startDate: Date, endDate: Date): Double?
}