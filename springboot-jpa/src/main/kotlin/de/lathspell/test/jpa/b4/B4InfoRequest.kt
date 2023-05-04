package de.lathspell.test.jpa.b4

import java.util.*
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
@Table(name = "b4_info_requests")
class B4InfoRequest(

    @Id
    var id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    var case: B4InfoCase? = null,

    var comment: String
)
