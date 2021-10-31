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

    constructor(id: String, name: String) : this() {
        this.identifier = id
        this.name = name
    }
}