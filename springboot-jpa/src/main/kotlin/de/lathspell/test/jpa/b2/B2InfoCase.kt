package de.lathspell.test.jpa.b2

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "b2_info_cases")
data class B2InfoCase(
    @Id
    val id: String = UUID.randomUUID().toString(),

    val customerId: String,
) {
    @OneToMany(cascade = [CascadeType.REMOVE], orphanRemoval = false /* default */)
    @OnDelete(action = OnDeleteAction.CASCADE) // prevents individual DELETE statements for all B2InfoRequests
    @JoinColumn(name = "caseId")
    val requests: List<B2InfoRequest> = emptyList()
}

