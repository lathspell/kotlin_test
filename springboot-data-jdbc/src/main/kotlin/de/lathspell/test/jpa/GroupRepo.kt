package de.lathspell.test.jpa

import de.lathspell.test.model.Group
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GroupRepo : CrudRepository<Group, UUID> {

    // @Query("SELECT * FROM Group g WHERE g.name = :name")
    fun findByName(@Param("name") name: String): Group
}
