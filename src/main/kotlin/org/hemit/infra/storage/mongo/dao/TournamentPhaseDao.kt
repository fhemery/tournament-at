package org.hemit.infra.storage.mongo.dao

class TournamentPhaseDao() {
    lateinit var type: String

    constructor(type: String) : this() {
        this.type = type
    }
}
