package de.lathspell.test.b.db

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.data.relational.core.mapping.event.AfterDeleteCallback
import org.springframework.stereotype.Component

/**
 * Lifecycle callback that acts when an AuthorEntity is deleted.
 *
 * Caveat: The callback function only gets called for AuthorsRepo.delete(authorEntity), not for AuthorsRepo.deleteById(authorId)!
 * *
 * The @Lazy annotation had to be used when injecting BooksRepo to avoid a circular dependency (which I can't explain).
 */
@Component
class AuthorEntityCallbacks(@Lazy private val booksRepo: BooksRepo) : AfterDeleteCallback<AuthorEntity> {

    val log = LoggerFactory.getLogger(AuthorEntityCallbacks::class.java)

    override fun onAfterDelete(entity: AuthorEntity): AuthorEntity {
        log.info("Received callback for deletion of author: {}", entity.name)
        entity.id?.let { booksRepo.deleteByAuthorId(it) }
        return entity
    }
}
