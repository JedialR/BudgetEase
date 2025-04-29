package com.example.budgettrackingapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgettrackingapp.model.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>
}
