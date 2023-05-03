package de.lathspell.test.jpa.b3

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "b3_info_cases")
data class B3InfoCase(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val customerId: String,
)
