package com.gustavofelipe.treino.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.gustavofelipe.treino.domain.model.WorkoutRoutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val routines by viewModel.routines.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Minhas Fichas", fontWeight = FontWeight.ExtraBold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreate,
                icon = { Icon(Icons.Default.Add, contentDescription = "Criar Nova Ficha") },
                text = { Text("Novo Treino") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar treino ou exercício...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar Busca")
                        }
                    }
                },
                shape = RoundedCornerShape(100),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            if (routines.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.List,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "Nenhum resultado encontrado" else "Nenhum treino montado",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "Tente buscar por outro nome." else "Toque em 'Novo Treino' para começar a criar sua primeira ficha.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(routines) { routine ->
                        WorkoutCard(
                            routine = routine,
                            onClick = { onNavigateToDetail(routine.id) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun WorkoutCard(
    routine: WorkoutRoutine,
    onClick: () -> Unit
) {
    val isCompletedToday = android.text.format.DateUtils.isToday(routine.lastCompletedDate)
    val isDark = isSystemInDarkTheme()

    val greenBackground = if (isDark) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFFE8F5E9)
    val greenIconBg = if (isDark) Color(0xFF2E7D32) else Color(0xFFC8E6C9)
    val greenText = if (isDark) Color(0xFFA5D6A7) else Color(0xFF1B5E20)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompletedToday) greenBackground else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isCompletedToday) greenIconBg else MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCompletedToday) androidx.compose.material.icons.Icons.Rounded.CheckCircle else androidx.compose.material.icons.Icons.Rounded.FitnessCenter,
                    contentDescription = null,
                    tint = if (isCompletedToday) greenText else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompletedToday) greenText else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isCompletedToday) "Treino concluído hoje! 💪" else "${routine.exercises.size} exercícios listados",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCompletedToday) greenText.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isCompletedToday) FontWeight.Bold else FontWeight.Normal
                )
            }

            Icon(
                imageVector = androidx.compose.material.icons.Icons.Rounded.ChevronRight,
                contentDescription = "Ver detalhes",
                tint = if (isCompletedToday) greenText else MaterialTheme.colorScheme.outline
            )
        }
    }
}