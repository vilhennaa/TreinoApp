package com.gustavofelipe.treino.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _routine = MutableStateFlow<WorkoutRoutine?>(null)
    val routine: StateFlow<WorkoutRoutine?> = _routine.asStateFlow()

    fun loadRoutine(id: Int) {
        viewModelScope.launch {
            _routine.value = repository.getRoutineById(id)
        }
    }

    fun deleteRoutine(onSuccess: () -> Unit) {
        val currentRoutine = _routine.value
        if (currentRoutine != null) {
            viewModelScope.launch {
                repository.deleteRoutine(currentRoutine)
                onSuccess()
            }
        }
    }
}