package de.lathspell.test

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EnumTest {

    enum class Person {
        KNOWN,
        UNKNOWN;

        @JsonValue // For enums, @JsonValue is used for both, serializing and deserializing!
        fun toJson(): String {
            return this.name.lowercase()
        }
    }

    private val om = ObjectMapper().registerKotlinModule()

    @Test
    fun `deserialize person enum`() {
        assertEquals(Person.KNOWN, om.readValue<Person>("\"known\""))
    }

    @Test
    fun `serialize person enum`() {
        assertEquals("\"unknown\"", om.writeValueAsString(Person.UNKNOWN))
    }

}
