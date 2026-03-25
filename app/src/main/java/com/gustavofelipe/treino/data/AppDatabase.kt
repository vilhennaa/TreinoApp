package com.gustavofelipe.treino.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gustavofelipe.treino.domain.model.WorkoutRoutine

/**
 * Configuração principal do banco de dados Room.
 */
@Database(entities = [WorkoutRoutine::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Fornece o DAO para o resto do aplicativo usar
    abstract fun workoutDao(): WorkoutDao
}