package com.gustavofelipe.treino.ui.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.Exercise
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ActiveWorkoutUiState(
    val routineId: Int = 0,
    val routineName: String = "",
    val exercises: List<Exercise> = emptyList(),
    val currentExerciseIndex: Int = 0,
    val currentSet: Int = 1,
    val isResting: Boolean = false,
    val restTimer: Int = 60,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true
)

class ActiveWorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun loadRoutine(id: Int) {
        viewModelScope.launch {
            val routine = repository.getRoutineById(id)
            if (routine != null && routine.exercises.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        routineId = routine.id,
                        routineName = routine.name,
                        exercises = routine.exercises,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, isFinished = true) }
            }
        }
    }

    fun completeSet() {
        val state = _uiState.value
        if (state.exercises.isEmpty()) return

        val currentExercise = state.exercises[state.currentExerciseIndex]
        val isLastSet = state.currentSet >= currentExercise.sets
        val isLastExercise = state.currentExerciseIndex >= state.exercises.size - 1

        if (isLastSet && isLastExercise) {
            _uiState.update { it.copy(isFinished = true, isResting = false) }
            markWorkoutAsCompleted()
        } else {
            startRestTimer(isMovingToNextExercise = isLastSet)
        }
    }


    private fun startRestTimer(isMovingToNextExercise: Boolean) {

        val currentState = _uiState.value
        val currentExercise = currentState.exercises[currentState.currentExerciseIndex]

        val restTimeSeconds = currentExercise.restTime.toIntOrNull() ?: 60

        _uiState.update { it.copy(isResting = true, restTimer = restTimeSeconds) }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.restTimer > 0) {
                delay(1000L)
                _uiState.update { it.copy(restTimer = it.restTimer - 1) }
            }
            skipRest(isMovingToNextExercise)
        }
    }

    fun addExtraRestTime(seconds: Int) {
        _uiState.update { it.copy(restTimer = it.restTimer + seconds) }
    }
    private fun markWorkoutAsCompleted() {
        val currentId = _uiState.value.routineId
        viewModelScope.launch {
            val routine = repository.getRoutineById(currentId)
            if (routine != null) {
                val updatedRoutine = routine.copy(lastCompletedDate = System.currentTimeMillis())
                repository.saveRoutine(updatedRoutine)
            }
        }
    }
    fun skipRest(isMovingToNextExercise: Boolean) {
        timerJob?.cancel()
        _uiState.update { state ->
            if (isMovingToNextExercise) {
                state.copy(
                    isResting = false,
                    currentExerciseIndex = state.currentExerciseIndex + 1,
                    currentSet = 1
                )
            } else {
                state.copy(
                    isResting = false,
                    currentSet = state.currentSet + 1
                )
            }
        }
    }
}