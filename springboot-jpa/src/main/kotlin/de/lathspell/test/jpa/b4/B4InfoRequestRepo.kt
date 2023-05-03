package de.lathspell.test.jpa.b4

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface B4InfoRequestRepo : JpaRepository<B4InfoRequest, String>
