package com.example.loginapplication.db

import androidx.room.*

@Dao
interface UserAndSubscriptionDao {

    @Insert
    fun insertUser(user: User): Long

    @Insert
    fun insertSubscription(subscription: Subscription): Long

    @Delete
    fun deleteUser(user: User)

    @Delete
    fun deleteSubscription(subscription: Subscription)

    @Transaction
    @Query("SELECT * FROM User")
    fun getUsersWithSubscription(): List<UserAndSubscription>

}