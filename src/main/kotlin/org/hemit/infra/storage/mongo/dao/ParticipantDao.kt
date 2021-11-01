package org.hemit.infra.storage.mongo.dao

class ParticipantDao() {
    lateinit var name: String
    var elo: Int = 0

    constructor(name: String, elo: Int) : this() {
        this.name = name
        this.elo = elo
    }
}
