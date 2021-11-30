package de.lathspell.test.jpa.b3

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "b3_info_cases")
data class B3InfoCase(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val customerId: String,
)
