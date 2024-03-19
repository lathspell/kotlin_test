package de.lathspell.test.jpa.d1

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "d1_info")
class D1Info(

    @Id
    var uuid: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY /* , cascade =[CascadeType.ALL] */)
    @JoinColumn(name = "task_uuid", nullable = false)
    var task: D1Task? = null,

    var info: String

)
