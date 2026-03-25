package com.gustavofelipe.treino.domain.model

import java.util.UUID

/**
 * Representa um exercício individual dentro de uma ficha de treino.
 */
data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sets: Int,
    val reps: String, // Usamos String porque pode ser repetições ("12") ou tempo ("60 seg")
    val videoUrl: String,
    val notes: String = ""
)