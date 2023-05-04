package de.lathspell.test.jpa.c1

import jakarta.persistence.*

@Embeddable
class C1InfoRequest(
    var comment: String? = null
)
