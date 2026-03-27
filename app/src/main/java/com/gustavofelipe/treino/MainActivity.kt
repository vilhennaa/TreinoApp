package com.gustavofelipe.treino

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gustavofelipe.treino.ui.createRoutine.CreateRoutineScreen
import com.gustavofelipe.treino.ui.detail.DetailScreen
import com.gustavofelipe.treino.ui.home.HomeScreen
import com.gustavofelipe.treino.ui.theme.TreinoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreinoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {

                        composable("home") {
                            HomeScreen(
                                onNavigateToCreate = {
                                    navController.navigate("create")
                                },
                                onNavigateToDetail = { routineId ->
                                    navController.navigate("detail/$routineId")
                                }
                            )
                        }

                        composable("create") {
                            CreateRoutineScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "detail/{routineId}",
                            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val routineId = backStackEntry.arguments?.getInt("routineId") ?: 0

                            DetailScreen(
                                routineId = routineId,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToEdit = { id ->
                                    navController.navigate("edit/$id")
                                }
                            )
                        }

                        composable(
                            route = "edit/{routineId}",
                            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val routineId = backStackEntry.arguments?.getInt("routineId") ?: 0

                            CreateRoutineScreen(
                                routineIdToEdit = routineId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}