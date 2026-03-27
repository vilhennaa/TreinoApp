package com.gustavofelipe.treino.data

import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.Flow


interface WorkoutRepository {
    fun getAllRoutines(): Flow<List<WorkoutRoutine>>
    suspend fun saveRoutine(routine: WorkoutRoutine)
    suspend fun deleteRoutine(routine: WorkoutRoutine)
    suspend fun getRoutineById(id: Int): WorkoutRoutine?
}


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

    override suspend fun getRoutineById(id: Int): WorkoutRoutine? {
        return dao.getRoutineById(id)
    }
}