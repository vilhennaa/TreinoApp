package com.gustavofelipe.treino.di

import androidx.room.Room
import com.gustavofelipe.treino.data.AppDatabase
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.data.WorkoutRepositoryImpl
import com.gustavofelipe.treino.ui.active.ActiveWorkoutViewModel
import com.gustavofelipe.treino.ui.createRoutine.CreateRoutineViewModel
import com.gustavofelipe.treino.ui.detail.DetailViewModel
import com.gustavofelipe.treino.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Módulo do Koin onde ensinamos como criar as dependências do nosso app.
 */
val appModule = module {

    // Cria o Banco de Dados.
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "treino_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // Extrai o DAO do Banco de Dados
    single { get<AppDatabase>().workoutDao() }

    // Cria o Repositório
    single<WorkoutRepository> { WorkoutRepositoryImpl(get()) }

    viewModel { HomeViewModel(get()) }

    viewModel { CreateRoutineViewModel(get()) }

    viewModel { DetailViewModel(get()) }

    viewModel { ActiveWorkoutViewModel(get()) }
}