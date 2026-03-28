package com.gustavofelipe.treino.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a Ficha de Treino completa.
 * @Entity: Esta anotação do Design Pattern diz ao Room que esta classe deve virar uma tabela no banco de dados.
 */
@Entity(tableName = "workout_routines")
data class WorkoutRoutine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dayOfWeek: String,
    val exercises: List<Exercise>, // A lista com os exercícios que pertencem a esta ficha
    val lastCompletedDate: Long = 0L
)