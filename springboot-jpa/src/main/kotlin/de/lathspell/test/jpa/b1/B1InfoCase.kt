package de.lathspell.test.jpa.b1

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "b1_info_cases")
data class B1InfoCase(
    @Id
    val id: UUID = UUID.randomUUID(),

    val customerId: String
)

