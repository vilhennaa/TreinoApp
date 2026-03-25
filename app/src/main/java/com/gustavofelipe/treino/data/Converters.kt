package com.gustavofelipe.treino.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gustavofelipe.treino.domain.model.Exercise

/**
 * Classe responsável por ensinar o banco de dados a lidar com listas complexas.
 */
class Converters {
    private val gson = Gson()

    // Pega a lista de exercícios e transforma em uma String (JSON)
    @TypeConverter
    fun fromExerciseList(value: List<Exercise>?): String {
        return gson.toJson(value)
    }

    // Pega a String (JSON) do banco de dados e transforma de volta em uma lista de exercícios
    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        val listType = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}