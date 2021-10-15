package org.hemit.domain.ports.input.commands

import org.hemit.domain.model.exceptions.NotEnoughParticipantsException
import org.hemit.domain.model.exceptions.TournamentAlreadyStartedException
import org.hemit.domain.model.exceptions.TournamentHasNoPhaseException
import org.hemit.domain.ports.output.TournamentStorage

class StartTournamentCommand(private val tournamentStorage: TournamentStorage) {

    fun execute(tournamentId: String): StartTournamentResult {
        val tournament =
            tournamentStorage.getTournament(tournamentId) ?: return StartTournamentResult.TournamentDoesNotExist

        try {
            tournament.start()

            tournamentStorage.saveTournament(tournament)
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
