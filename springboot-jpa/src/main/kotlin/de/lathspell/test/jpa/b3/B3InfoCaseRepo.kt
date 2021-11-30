package de.lathspell.test.jpa.b3

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface B3InfoCaseRepo : JpaRepository<B3InfoCase, UUID>
