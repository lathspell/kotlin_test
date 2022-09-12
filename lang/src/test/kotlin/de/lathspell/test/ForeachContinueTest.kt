package de.lathspell.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ForeachContinueTest {

    /** There is no continue in forEach() as it's a function (the example would usually use filter/map not forEach!) */
    @Test
    fun foreachContinue() {
        val list = listOf(1, 2, 3, 4)

        // skipping loop with forEach()1
        val even = mutableListOf<Int>()
        list.forEach {
            if ((it % 2) == 1) {
                return@forEach
            }
            even.add(it)
        }
        assertThat(even).containsExactly(2, 4)
    }

    /** There is no continue in map() as it's a function (the example would usually use filter/map not forEach!) */
    @Test
    fun mapContinue() {
        val list = listOf(1, 2, 3, 4)

        // skipping loop with map()
        val odd = mutableListOf<Int>()
        list.map {
            if ((it % 2) == 0) {
                return@map
            }
            odd.add(it)
        }
        assertThat(odd).containsExactly(1, 3)
    }

}
