package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val wordId: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definitionPreview: String,
    val language: String,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
