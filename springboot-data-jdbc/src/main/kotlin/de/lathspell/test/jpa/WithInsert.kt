package de.lathspell.test.jpa

/** As seen in https://github.com/spring-projects/spring-data-examples/pull/441 */
interface WithInsert<T> {
    fun insert(t: T): T
}
