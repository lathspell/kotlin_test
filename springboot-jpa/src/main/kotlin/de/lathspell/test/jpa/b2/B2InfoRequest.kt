package de.lathspell.test.jpa.b2

import java.util.*
import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "b2_info_requests")
data class B2InfoRequest(

    @Id
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "caseId")
    val case: B2InfoCase, // automatically uses "case_id"

    val comment: String
)
