package de.lathspell.test.jpa.a1

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "persons", uniqueConstraints = [UniqueConstraint(name = "uc_persons_name", columnNames = ["name"])])
data class Person(
    @Id
    val id: UUID = UUID.randomUUID(),
    val gid: Int,
    val name: String,
)
