package de.lathspell.test.jpa.d1

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface D1TaskRepo : JpaRepository<D1Task, UUID>
