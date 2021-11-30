package de.lathspell.test.jpa.b3

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*
import javax.persistence.FetchType.EAGER

@Entity
@Table(name = "b3_info_requests")
data class B3InfoRequest(

    @Id
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "caseId")
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val case: B3InfoCase,

    val comment: String
)
