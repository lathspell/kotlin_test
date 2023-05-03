package de.lathspell.test.jpa.b1

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "b1_info_cases")
data class B1InfoCase(
    @Id
    val id: UUID = UUID.randomUUID(),

    val customerId: String
)

