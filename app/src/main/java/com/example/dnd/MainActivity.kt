package com.example.dnd

import Personagem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bonusRacial.Anao
import com.example.dnd.ui.theme.DndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DndTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "raca") {
        composable("raca") { DropdownPersonagem(navController) }
        composable("distribuicao_atributos") {
            DistribuicaoAtributos(PersonagemManager.personagem, navController)
        }
        composable("ficha") {FichaPersonagem(PersonagemManager.personagem)}
    }
}



