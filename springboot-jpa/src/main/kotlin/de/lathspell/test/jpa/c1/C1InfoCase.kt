package de.lathspell.test.jpa.c1

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "c1_info_cases")
class C1InfoCase(
    @Id
    var id: String = UUID.randomUUID().toString(),

    var customerId: String,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "comment", column = Column(name = "request_comment"))
    )
    var request: C1InfoRequest? = null
)
