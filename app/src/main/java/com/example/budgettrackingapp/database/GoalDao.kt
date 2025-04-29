package com.example.budgettrackingapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgettrackingapp.model.Goal

@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal)

    @Query("SELECT * FROM Goal WHERE userId = :userId")
    suspend fun getGoalForUser(userId: Int): Goal?
}
