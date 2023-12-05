package de.lathspell.test.jpa

import org.springframework.data.jdbc.core.JdbcAggregateTemplate

class WithInsertImpl<T : Any>(private val template: JdbcAggregateTemplate) : WithInsert<T> {

    override fun insert(t: T): T {
        return template.insert(t)
    }
}
