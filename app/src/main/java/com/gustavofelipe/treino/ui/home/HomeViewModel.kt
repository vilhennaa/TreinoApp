package com.gustavofelipe.treino.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    val routines: StateFlow<List<WorkoutRoutine>> = combine(
        repository.getAllRoutines(),
        _searchQuery
    ) { routinesList, query ->
        if (query.isBlank()) {
            routinesList
        } else {
            routinesList.filter { routine ->
                val matchesRoutineName = routine.name.contains(query, ignoreCase = true)
                val matchesExerciseName = routine.exercises.any { exercise ->
                    exercise.name.contains(query, ignoreCase = true)
                }
                matchesRoutineName || matchesExerciseName
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}