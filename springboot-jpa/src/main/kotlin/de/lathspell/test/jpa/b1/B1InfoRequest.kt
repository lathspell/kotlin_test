package de.lathspell.test.jpa.b1

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "b1_info_requests")
data class B1InfoRequest(

    @Id
    val id: UUID = UUID.randomUUID(),

    val caseId: UUID,

    val comment: String
)
