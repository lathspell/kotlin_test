package de.lathspell.test.b.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorsRepo : CrudRepository<AuthorEntity, Long> {
}
