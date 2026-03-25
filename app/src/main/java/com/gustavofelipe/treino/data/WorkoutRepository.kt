package com.gustavofelipe.treino.data

import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.Flow

/**
 * A Interface define "O QUE" o repositório faz (Contrato).
 */
interface WorkoutRepository {
    fun getAllRoutines(): Flow<List<WorkoutRoutine>>
    suspend fun saveRoutine(routine: WorkoutRoutine)
    suspend fun deleteRoutine(routine: WorkoutRoutine)
}

/**
 * A Implementação define "COMO" o repositório faz (Chamando o DAO).
 */
class WorkoutRepositoryImpl(
    private val dao: WorkoutDao
) : WorkoutRepository {

    override fun getAllRoutines(): Flow<List<WorkoutRoutine>> {
        return dao.getAllRoutines()
    }

    override suspend fun saveRoutine(routine: WorkoutRoutine) {
        dao.insertRoutine(routine)
    }

    override suspend fun deleteRoutine(routine: WorkoutRoutine) {
        dao.deleteRoutine(routine)
    }
}