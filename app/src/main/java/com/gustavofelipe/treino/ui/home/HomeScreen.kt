package com.gustavofelipe.treino.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.gustavofelipe.treino.domain.model.WorkoutRoutine

/**
 * Nossa Tela Principal.
 * * @param onNavigateToCreate Função chamada para ir para a tela de Montar Treino.
 * @param onNavigateToDetail Função chamada para ir para a tela de Ver Treino (passando o ID).
 * @param viewModel Injetado automaticamente pelo Koin!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val routines by viewModel.routines.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Fichas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Criar Nova Ficha")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (routines.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma ficha cadastrada ainda.\nClique no + para criar!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }

            items(routines) { routine ->
                WorkoutCard(
                    routine = routine,
                    onClick = { onNavigateToDetail(routine.id) }
                )
            }
        }
    }
}

@Composable
fun WorkoutCard(
    routine: WorkoutRoutine,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dia: ${routine.dayOfWeek}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${routine.exercises.size} exercícios cadastrados",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}