package com.gustavofelipe.treino.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel da Tela Principal.
 * Ele pede as fichas para o Repositório e avisa a Tela (View) sempre que houver novidades.
 */
class HomeViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    // O StateFlow é como um "canal de rádio".
    // O ViewModel transmite a lista de treinos, e a tela fica "ouvindo".
    // Se um treino novo for salvo no banco, essa lista é atualizada automaticamente!
    val routines: StateFlow<List<WorkoutRoutine>> = repository.getAllRoutines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantém ativo enquanto a tela estiver aberta
            initialValue = emptyList() // Começa com uma lista vazia
        )
}