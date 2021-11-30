package de.lathspell.test.jpa.b1

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface B1InfoCaseRepo : JpaRepository<B1InfoCase, UUID>
