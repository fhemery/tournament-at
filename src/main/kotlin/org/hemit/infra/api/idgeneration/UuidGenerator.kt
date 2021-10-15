package org.hemit.infra.api.idgeneration

import org.hemit.domain.ports.output.IdGeneration
import java.util.*

class UuidGenerator : IdGeneration {
    override fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}