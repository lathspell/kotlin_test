package de.lathspell.test.jpa.b2

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface B2InfoRequestRepo : JpaRepository<B2InfoRequest, UUID>
