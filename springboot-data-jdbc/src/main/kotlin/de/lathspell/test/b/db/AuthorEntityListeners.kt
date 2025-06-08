package de.lathspell.test.b.db

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.data.relational.core.mapping.event.AbstractRelationalEventListener
import org.springframework.data.relational.core.mapping.event.AfterDeleteEvent
import org.springframework.stereotype.Component

/**
 * Lifecycle listener that acts when an AuthorEntity is deleted.
 *
 * The @Lazy annotation had to be used when injecting BooksRepo to avoid a circular dependency (which I can't explain).
 */
@Component
class AuthorEntityListeners(@Lazy private val booksRepo: BooksRepo) : AbstractRelationalEventListener<AuthorEntity>() {

    private val log = LoggerFactory.getLogger(AuthorEntityListeners::class.java)

    override fun onAfterDelete(event: AfterDeleteEvent<AuthorEntity>) {
        super.onAfterDelete(event)
        log.info("Received event for deletion of author: {}", event.id)
        booksRepo.deleteByAuthorId(event.id.value as Long)
    }

}
