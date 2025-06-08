package de.lathspell.test.b.db

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface BooksRepo : CrudRepository<BookEntity, Long> {

    @Modifying
    @Query("DELETE FROM books WHERE author_id = :authorId")
    fun deleteByAuthorId(authorId: Long)

}
