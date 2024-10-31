package de.lathspell.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** https://github.com/FasterXML/jackson-module-kotlin/issues/771. */
class SinglePropertyBug {

    data class Process(
        val processPart: ProcessPart
    )

    data class ProcessPart(
        val type: ProcessPartType
    )

    enum class ProcessPartType {
        PROJECT
    }

    val mapper = jacksonObjectMapper()

    @Test
    fun test() {
        val json = """
        {
          "processPart": {
            "type": "PROJECT"
          }
        }
        """.trimIndent()

        assertEquals(Process(ProcessPart(ProcessPartType.PROJECT)), mapper.readValue<Process>(json))
    }

}
