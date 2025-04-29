package com.example.budgettrackingapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val minAmount: Double,
    val maxAmount: Double
)
