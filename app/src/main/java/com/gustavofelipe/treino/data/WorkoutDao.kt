package com.gustavofelipe.treino.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO: Contém as operações que faremos no banco de dados.
 */
@Dao
interface WorkoutDao {
    // Busca todas as fichas de treino.
    @Query("SELECT * FROM workout_routines ORDER BY id DESC")
    fun getAllRoutines(): Flow<List<WorkoutRoutine>>

    // Insere uma nova ficha. Se já existir uma com o mesmo ID, ele substitui (atualiza).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: WorkoutRoutine)
    @Query("SELECT * FROM workout_routines WHERE id = :id") // Verifique se o nome da tabela é esse mesmo
    suspend fun getRoutineById(id: Int): WorkoutRoutine?
    // Deleta uma ficha específica
    @Delete
    suspend fun deleteRoutine(routine: WorkoutRoutine)
}