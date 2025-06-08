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
class LifecycleEventsTest(
    @Autowired private val authorsRepo: AuthorsRepo,
    @Autowired private val booksRepo: BooksRepo
) {

    @Test
    @Transactional
    fun `delete child when parent is deleted`() {
        // create relation
        val gorwell = authorsRepo.save(AuthorEntity(name = "George Orwell"))
        val book1 = booksRepo.save(BookEntity(title = "1984", authorId = gorwell.id!!))
        assertThat(booksRepo.findAll()).hasSize(1)

        // delete parent (author) and check if child (book) is deleted as well
        authorsRepo.delete(gorwell)
        assertThat(booksRepo.findAll()).isEmpty()
    }
}
