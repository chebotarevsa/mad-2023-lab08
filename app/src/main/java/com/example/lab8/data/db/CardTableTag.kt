package com.example.lab8.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardTableTag(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "card_table_id") val cardTableId: String,
    @ColumnInfo(name = "tag_id") val tagId: String
)