package de.lathspell.test.jpa.d1

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "d1_info")
class D1Info(

    @Id
    var uuid: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_uuid", referencedColumnName = "uuid")
    @JsonBackReference
    var task: D1Task? = null,

    var info: String

)
