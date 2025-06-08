package de.lathspell.test.b.db

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "authors")
data class AuthorEntity(

    @Id
    val id: Long? = null,

    val name: String
)
