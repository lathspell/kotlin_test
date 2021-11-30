package de.lathspell.test.jpa.a1

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "groups", uniqueConstraints = [UniqueConstraint(name = "uc_groups_name", columnNames = ["name"])])
data class Group(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
)
