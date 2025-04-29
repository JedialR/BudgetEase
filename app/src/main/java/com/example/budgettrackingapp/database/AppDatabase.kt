package com.example.budgettrackingapp.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgettrackingapp.model.*

@Database(
    entities = [User::class, Category::class, Expense::class, Goal::class],
    version = 2, // Updated version to fix Room schema mismatch
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracking_db"
                ).fallbackToDestructiveMigration() // Allows clearing old db if schema mismatched
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
