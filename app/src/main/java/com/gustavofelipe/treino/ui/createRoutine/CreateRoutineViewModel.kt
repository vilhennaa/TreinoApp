package com.gustavofelipe.treino.ui.createRoutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.Exercise
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class CreateRoutineUiState(
    val routineName: String = "",
    val exerciseName: String = "",
    val sets: String = "",
    val reps: String = "",
    val videoUrl: String = "",
    val restTime: String = "",
    val addedExercises: List<Exercise> = emptyList(),
    val errorMessage: String? = null
)

class CreateRoutineViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRoutineUiState())
    val uiState: StateFlow<CreateRoutineUiState> = _uiState.asStateFlow()

    private var currentEditingId: Int? = null

    fun updateRoutineName(name: String) = _uiState.update { it.copy(routineName = name) }
    fun updateExerciseName(name: String) = _uiState.update { it.copy(exerciseName = name) }
    fun updateSets(sets: String) = _uiState.update { it.copy(sets = sets) }
    fun updateReps(reps: String) = _uiState.update { it.copy(reps = reps) }
    fun updateVideoUrl(url: String) = _uiState.update { it.copy(videoUrl = url) }
    fun updateRestTime(time: String) = _uiState.update { it.copy(restTime = time) }

    fun loadRoutineForEditing(id: Int) {
        viewModelScope.launch {
            val routine = repository.getRoutineById(id)
            if (routine != null) {
                currentEditingId = routine.id
                _uiState.update {
                    it.copy(
                        routineName = routine.name,
                        addedExercises = routine.exercises
                    )
                }
            }
        }
    }

    fun addExercise() {
        val currentState = _uiState.value

        if (currentState.exerciseName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "O nome do exercício é obrigatório.") }
            return
        }

        val newExercise = Exercise(
            id = UUID.randomUUID().toString(),
            name = currentState.exerciseName,
            sets = currentState.sets.toIntOrNull() ?: 0,
            reps = currentState.reps,
            videoUrl = currentState.videoUrl,
            restTime = currentState.restTime
        )

        _uiState.update {
            it.copy(
                addedExercises = it.addedExercises + newExercise,
                exerciseName = "",
                sets = "",
                reps = "",
                videoUrl = "",
                restTime = "",
                errorMessage = null
            )
        }
    }

    fun saveRoutine(onSuccess: () -> Unit) {
        val currentState = _uiState.value

        if (currentState.routineName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Dê um nome para a sua ficha.") }
            return
        }

        if (currentState.addedExercises.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Adicione pelo menos um exercício na ficha.") }
            return
        }

        viewModelScope.launch {
            val newRoutine = WorkoutRoutine(
                id = currentEditingId ?: 0,
                name = currentState.routineName,
                dayOfWeek = "",
                exercises = currentState.addedExercises
            )

            repository.saveRoutine(newRoutine)

            onSuccess()
        }
    }

    fun removeExercise(exerciseToRemove: Exercise) {
        _uiState.update { currentState ->
            currentState.copy(
                addedExercises = currentState.addedExercises.filter { it.id != exerciseToRemove.id }
            )
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}