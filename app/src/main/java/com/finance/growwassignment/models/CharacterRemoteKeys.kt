package com.finance.growwassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
