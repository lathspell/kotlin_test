package de.lathspell.test.jpa.b4

import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import java.util.*

@Entity
@Table(name = "b4_info_cases")
data class B4InfoCase(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val customerId: String,

    @OneToMany(fetch = EAGER, orphanRemoval = true /* ??? */, mappedBy = "case")
    val requests: List<B4InfoRequest> = emptyList()
)
