package de.lathspell.test.b.db

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.data.relational.core.mapping.event.AfterDeleteCallback
import org.springframework.stereotype.Component

/**
 * Lifecycle callback listener that acts when an AuthorEntity is deleted.
 *
 * The @Lazy annotation had to be used when injecting BooksRepo to avoid a circular dependency (which I can't explain).
 */
@Component
class AuthorEntityListener(@Lazy private val booksRepo: BooksRepo) : AfterDeleteCallback<AuthorEntity> {

    val log = LoggerFactory.getLogger(AuthorEntityListener::class.java)

    override fun onAfterDelete(entity: AuthorEntity): AuthorEntity {
        log.info("Deleting all books from author: {}", entity.name)
        entity.id?.let { booksRepo.deleteByAuthorId(it) }
        return entity
    }
}
