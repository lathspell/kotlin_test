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

    /** Caveat: The cached collection will not be automatically synchronized with the database changes! */
    @OneToMany(fetch = EAGER, orphanRemoval = false /* could not yet find any effect */, mappedBy = "case")
    val requests: List<B4InfoRequest> = emptyList()
)
