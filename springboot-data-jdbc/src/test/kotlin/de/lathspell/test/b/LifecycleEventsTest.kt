package de.lathspell.test.b

import de.lathspell.test.b.db.AuthorEntity
import de.lathspell.test.b.db.AuthorsRepo
import de.lathspell.test.b.db.BookEntity
import de.lathspell.test.b.db.BooksRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.transaction.annotation.Transactional

/** Demonstrates lifecycle events by implementing a callback handler that acts on deletions of "AuthorEntity" objects. */
@SpringBootTest(webEnvironment = NONE)
@Transactional
class LifecycleEventsTest(
    @Autowired private val authorsRepo: AuthorsRepo,
    @Autowired private val booksRepo: BooksRepo
) {

    @Test
    fun `delete child when parent is deleted by entity (triggers callback and event)`() {
        // create relation
        val gorwell = authorsRepo.save(AuthorEntity(name = "George Orwell"))
        booksRepo.save(BookEntity(title = "1984", authorId = gorwell.id!!))
        assertThat(booksRepo.findAll()).hasSize(1)

        // delete parent (author) and check if child (book) is deleted as well
        authorsRepo.delete(gorwell)
        assertThat(booksRepo.findAll()).isEmpty()
    }

    @Test
    fun `delete child when parent is deleted by id (triggers only event)`() {
        authorsRepo.save(AuthorEntity(name = "author1"))
        authorsRepo.save(AuthorEntity(name = "author2"))
        // create relation
        val orwell = authorsRepo.save(AuthorEntity(name = "George Orwell"))
        booksRepo.save(BookEntity(title = "1984", authorId = orwell.id!!))
        assertThat(booksRepo.findAll()).hasSize(1)

        // delete parent (author) and check if child (book) is deleted as well
        authorsRepo.deleteById(orwell.id)
        assertThat(booksRepo.findAll()).isEmpty()
    }
}
