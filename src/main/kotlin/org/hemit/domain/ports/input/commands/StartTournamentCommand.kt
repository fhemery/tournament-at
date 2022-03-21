package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.exceptions.NotEnoughParticipantsException
import org.hemit.domain.model.exceptions.TournamentAlreadyStartedException
import org.hemit.domain.model.exceptions.TournamentHasNoPhaseException
import org.hemit.domain.model.tournament.RegisteredTournament
import org.hemit.domain.model.tournament.TournamentStatus
import org.hemit.domain.ports.output.GetTournamentResult
import org.hemit.domain.ports.output.TournamentStorage
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class StartTournamentCommand() {

    @Inject
    lateinit var tournamentStorage: TournamentStorage

    constructor(tournamentStorage: TournamentStorage) : this() {
        this.tournamentStorage = tournamentStorage
    }

    fun execute(tournamentId: String): StartTournamentResult {
        val tournament = when (val result = tournamentStorage.getTournament(tournamentId)) {
            is GetTournamentResult.Success -> result.tournament
            GetTournamentResult.TournamentDoesNotExist -> return StartTournamentResult.TournamentDoesNotExist
        }

        try {
            if (tournament.status != TournamentStatus.NotStarted) {
                return StartTournamentResult.TournamentAlreadyStarted
            }
            val ongoingTournament = (tournament as RegisteredTournament).start()

            tournamentStorage.saveTournament(ongoingTournament)
        } catch (e: NotEnoughParticipantsException) {
            return StartTournamentResult.NotEnoughParticipants
        } catch (e: TournamentAlreadyStartedException) {
            return StartTournamentResult.TournamentAlreadyStarted
        } catch (e: TournamentHasNoPhaseException) {
            return StartTournamentResult.NoPhaseDefined
        }

        return StartTournamentResult.Success
    }
}

sealed class StartTournamentResult {
    object Success : StartTournamentResult()
    object TournamentAlreadyStarted : StartTournamentResult()
    object NotEnoughParticipants : StartTournamentResult()
    object NoPhaseDefined : StartTournamentResult()
    object TournamentDoesNotExist : StartTournamentResult()
}
