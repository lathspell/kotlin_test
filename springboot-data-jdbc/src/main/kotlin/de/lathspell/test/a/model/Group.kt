package de.lathspell.test.a.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("groups")
data class Group(

    @Id
    val id: UUID? = null,

    val name: String
)
