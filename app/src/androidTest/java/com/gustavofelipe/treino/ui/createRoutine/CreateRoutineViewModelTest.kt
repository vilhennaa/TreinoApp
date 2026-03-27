package com.gustavofelipe.treino.ui.createRoutine

import com.gustavofelipe.treino.data.WorkoutRepository
import com.gustavofelipe.treino.domain.model.WorkoutRoutine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeWorkoutRepository : WorkoutRepository {
    override fun getAllRoutines(): Flow<List<WorkoutRoutine>> = flowOf(emptyList())
    override suspend fun saveRoutine(routine: WorkoutRoutine) {}
    override suspend fun deleteRoutine(routine: WorkoutRoutine) {}
    override suspend fun getRoutineById(id: Int): WorkoutRoutine? = null
}

class CreateRoutineViewModelTest {

    private lateinit var viewModel: CreateRoutineViewModel
    private lateinit var fakeRepository: FakeWorkoutRepository

    @Before
    fun setup() {
        fakeRepository = FakeWorkoutRepository()
        viewModel = CreateRoutineViewModel(fakeRepository)
    }

    @Test
    fun addExercise_comNomeVazio_deveRetornarErro() {
        viewModel.updateExerciseName("")

        viewModel.addExercise()

        val currentState = viewModel.uiState.value
        assertTrue(currentState.addedExercises.isEmpty())
        assertEquals("O nome do exercício é obrigatório.", currentState.errorMessage)
    }

    @Test
    fun addExercise_comDadosValidos_deveAdicionarAListaELimparCampos() {
        viewModel.updateExerciseName("Supino Reto")
        viewModel.updateSets("4")
        viewModel.updateReps("12")

        viewModel.addExercise()

        val currentState = viewModel.uiState.value
        assertEquals(1, currentState.addedExercises.size)
        assertEquals("Supino Reto", currentState.addedExercises[0].name)
        assertEquals("", currentState.exerciseName)
    }

    @Test
    fun saveRoutine_semNomeDaFicha_deveRetornarErro() {
        viewModel.updateRoutineName("")
        viewModel.updateExerciseName("Agachamento")
        viewModel.addExercise()

        var sucessoNavegacao = false
        viewModel.saveRoutine(onSuccess = { sucessoNavegacao = true })

        val currentState = viewModel.uiState.value
        assertEquals(false, sucessoNavegacao)
        assertEquals("Dê um nome para a sua ficha.", currentState.errorMessage)
    }

    @Test
    fun saveRoutine_semExercicios_deveRetornarErro() {
        viewModel.updateRoutineName("Treino de Pernas")

        var sucessoNavegacao = false
        viewModel.saveRoutine(onSuccess = { sucessoNavegacao = true })

        val currentState = viewModel.uiState.value
        assertEquals(false, sucessoNavegacao)
        assertEquals("Adicione pelo menos um exercício na ficha.", currentState.errorMessage)
    }

    @Test
    fun clearError_deveRemoverAMensagemDeErroDoEstado() {
        viewModel.updateExerciseName("")
        viewModel.addExercise()
        assertEquals("O nome do exercício é obrigatório.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertEquals(null, viewModel.uiState.value.errorMessage)
    }
}