package com.example.loginapplication.db

import android.content.Context
import androidx.room.*
import androidx.room.TypeConverters

@Database(entities = [User::class, Subscription::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun userAndSubscriptionDao() : UserAndSubscriptionDao

    companion object {
        var db: LoginDatabase? = null

        fun create(context: Context): LoginDatabase {
            if(db == null) {
                db = Room.databaseBuilder(context.applicationContext, LoginDatabase::class.java, "db").build()
            }
            return db as LoginDatabase
        }
    }

}