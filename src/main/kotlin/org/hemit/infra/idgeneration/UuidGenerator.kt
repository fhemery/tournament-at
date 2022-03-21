package org.hemit.infra.idgeneration

import org.hemit.domain.ports.output.IdGeneration
import java.util.UUID
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UuidGenerator : IdGeneration {
    override fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}
