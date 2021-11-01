package org.hemit.infra.storage.mongo.dao

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity

@MongoEntity(collection = "tournament")
class TournamentDao() : PanacheMongoEntity() {
    companion object : PanacheMongoCompanion<TournamentDao> {
        fun findById(id: String) = find("identifier", id).firstResult()
    }

    lateinit var identifier: String
    lateinit var name: String
    lateinit var phases: List<TournamentPhaseDao>
    lateinit var participants: List<ParticipantDao>
    var maxParticipants = Int.MAX_VALUE
    lateinit var status: String

    constructor(
        id: String,
        name: String,
        phases: List<TournamentPhaseDao> = emptyList(),
        participants: List<ParticipantDao> = emptyList(),
        maxParticipants: Int = Int.MAX_VALUE,
        status: String
    ) : this() {
        this.identifier = id
        this.name = name
        this.phases = phases
        this.participants = participants
        this.maxParticipants = maxParticipants
        this.status = status
    }
}