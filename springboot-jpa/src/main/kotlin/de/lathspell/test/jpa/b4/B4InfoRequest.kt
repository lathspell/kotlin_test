package de.lathspell.test.jpa.b4

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import jakarta.persistence.*
import jakarta.persistence.FetchType.EAGER

@Entity
@Table(name = "b4_info_requests")
data class B4InfoRequest(

    @Id
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "case_id", referencedColumnName = "id", nullable = false)
    @Fetch(FetchMode.JOIN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val case: B4InfoCase? = null,

    val comment: String
)
