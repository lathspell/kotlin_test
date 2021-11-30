package de.lathspell.test.jpa.a1

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonRepo : JpaRepository<Person, UUID>
