package com.example.loginapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserAndSubscriptionDao {

    @Insert
    fun insertUser(user: User): Long

    @Insert
    fun insertSubscription(subscription: Subscription): Long

    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithSubscription(): List<UserAndSubscription>

}