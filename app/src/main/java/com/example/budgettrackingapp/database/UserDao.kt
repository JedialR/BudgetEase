package com.example.budgettrackingapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgettrackingapp.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM User WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?
}
