package de.lathspell.test.jpa.a1

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "persons", uniqueConstraints = [UniqueConstraint(name = "uc_persons_name", columnNames = ["name"])])
data class Person(
    @Id
    val id: UUID = UUID.randomUUID(),
    val gid: Int,
    val name: String,
)
