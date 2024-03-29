package de.lathspell.test.jpa.b4

import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER
import java.util.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "b4_info_cases")
class B4InfoCase(
    @Id
    var id: String = UUID.randomUUID().toString(),

    var customerId: String,

    @OneToMany(fetch = EAGER, mappedBy = "case", orphanRemoval = true, cascade = [CascadeType.ALL])
    var requests: MutableList<B4InfoRequest> = ArrayList()
) {

    /** This method adds children and sets this object as their parent reference. */
    fun withRequests(vararg reqs: B4InfoRequest): B4InfoCase {
        reqs.forEach { it.case = this }
        requests = reqs.toMutableList()
        return this
    }
}
