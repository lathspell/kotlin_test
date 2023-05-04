package de.lathspell.test.jpa.c1

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface C1InfoCaseRepo : JpaRepository<C1InfoCase, String>
