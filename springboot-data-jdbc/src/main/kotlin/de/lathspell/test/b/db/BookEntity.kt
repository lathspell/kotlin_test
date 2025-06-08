package de.lathspell.test.b.db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "books")
data class BookEntity(
    @Id
    val id: Long? = null,

    val title: String,

    val authorId: Long
)
