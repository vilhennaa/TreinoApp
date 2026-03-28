package com.gustavofelipe.treino.ui.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    routineId: Int,
    onFinishWorkout: () -> Unit,
    viewModel: ActiveWorkoutViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(routineId) {
        viewModel.loadRoutine(routineId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.routineName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onFinishWorkout) {
                        Icon(Icons.Default.Close, contentDescription = "Sair do Treino")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()

                uiState.isFinished -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(64.dp))
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Treino Concluído!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text("Excelente trabalho hoje.", color = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = onFinishWorkout, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            Text("Voltar ao Início")
                        }
                    }
                }

                uiState.isResting -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Hora de Descansar", style = MaterialTheme.typography.titleLarge)

                        Text(
                            text = "00:${uiState.restTimer.toString().padStart(2, '0')}",
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedButton(onClick = { viewModel.addExtraRestTime(15) }) {
                                Text("+15s")
                            }
                            OutlinedButton(onClick = { viewModel.addExtraRestTime(30) }) {
                                Text("+30s")
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        val isMovingToNextExercise = uiState.currentSet >= uiState.exercises[uiState.currentExerciseIndex].sets
                        OutlinedButton(
                            onClick = { viewModel.skipRest(isMovingToNextExercise) },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text("Pular Descanso")
                        }
                    }
                }

                else -> {
                    val currentExercise = uiState.exercises[uiState.currentExerciseIndex]
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = "Exercício ${uiState.currentExerciseIndex + 1} de ${uiState.exercises.size}",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentExercise.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(32.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Série Atual", color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = "${uiState.currentSet} / ${currentExercise.sets}",
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Meta de Repetições", color = MaterialTheme.colorScheme.outline)
                                Text(text = currentExercise.reps, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = { viewModel.completeSet() },
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("SÉRIE CONCLUÍDA", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}