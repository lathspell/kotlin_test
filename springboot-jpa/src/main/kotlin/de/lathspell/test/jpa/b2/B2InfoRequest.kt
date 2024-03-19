package de.lathspell.test.jpa.b2

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.util.*

@Entity
@Table(name = "b2_info_requests")
data class B2InfoRequest(

    @Id
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "caseId", nullable = false)
    val case: B2InfoCase, // automatically uses "case_id"

    val comment: String
)
