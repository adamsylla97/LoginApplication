package com.example.loginapplication.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.OffsetDateTime
import java.util.*

@Entity
data class User(
    val token: String,
    val last_used: OffsetDateTime,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity
data class Subscription(
    val user_id: Int,
    val start: OffsetDateTime,
    val ending: OffsetDateTime,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

data class UserAndSubscription(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    ) val subscription: Subscription
)