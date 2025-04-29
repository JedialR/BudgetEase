package com.example.budgettrackingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: Date,
    val startTime: Date,
    val endTime: Date,
    val description: String,
    val categoryId: Int,
    val photo: String? = null // Save photo as URI string
)