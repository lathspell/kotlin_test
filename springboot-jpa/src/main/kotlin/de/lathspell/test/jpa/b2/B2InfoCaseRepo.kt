package de.lathspell.test.jpa.b2

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface B2InfoCaseRepo : JpaRepository<B2InfoCase, UUID>
