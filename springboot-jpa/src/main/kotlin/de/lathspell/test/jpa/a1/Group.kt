package de.lathspell.test.jpa.a1

import java.util.*
import jakarta.persistence.Entity
import jakarta.persistence.UniqueConstraint
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "groups", uniqueConstraints = [UniqueConstraint(name = "uc_groups_name", columnNames = ["name"])])
data class Group(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
)
