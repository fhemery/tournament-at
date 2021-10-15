package org.hemit.utils.ports

import org.hemit.domain.ports.output.IdGeneration

class IdGeneratorStub : IdGeneration {
    private var sequence = 0

    override fun generateId(): String {
        return (++sequence).toString()
    }
}