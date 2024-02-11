package com.example.lab8.data.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity
data class CardTableWithTags(
    @Embedded val cardTable: CardTable,
    @Relation(parentColumn = "id", entityColumn = "card_table_id", entity = CardTableTag::class, associateBy = Junction(CardTableTag::class))
    val tags: List<Tag>
)