package com.example.dnd

import Personagem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bonusRacial.*

@Composable
fun DropdownPersonagem(navController: NavController) {
    val racas = listOf(
        "Alto Elfo", "Anão", "Anão da Colina", "Anão da Montanha", "Draconato",
        "Drow", "Elfo", "Elfo da Floresta", "Gnomo", "Gnomo da Floresta",
        "Gnomo das Rochas", "Halfling", "Halfling Pés Leves", "Halfling Robusto",
        "Humano", "Meio-Elfo", "Meio-Orc", "Tiefling"
    )

    var personagem by remember { mutableStateOf<Personagem?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedRaca by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecione uma raça:")

        OutlinedTextField(
            value = selectedRaca,
            onValueChange = {},
            readOnly = true,
            label = { Text("Raça") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            racas.forEach { raca ->
                DropdownMenuItem(
                    text = { Text(text = raca) },
                    onClick = {
                        selectedRaca = raca
                        expanded = false

                        // Inicializa a classe Personagem com base na raça selecionada
                        personagem = when (raca) {
                            "Alto Elfo" -> Personagem(AltoElfo())
                            "Anão" -> Personagem(Anao())
                            "Anão da Colina" -> Personagem(AnaoDaColina())
                            "Anão da Montanha" -> Personagem(AnaoMontanha())
                            "Draconato" -> Personagem(Draconato())
                            "Drow" -> Personagem(Drow())
                            "Elfo" -> Personagem(Elfo())
                            "Elfo da Floresta" -> Personagem(ElfoDaFloresta())
                            "Gnomo" -> Personagem(Gnomo())
                            "Gnomo da Floresta" -> Personagem(GnomoDaFloresta())
                            "Gnomo das Rochas" -> Personagem(GnomoDasRochas())
                            "Halfling" -> Personagem(Halfling())
                            "Halfling Pés Leves" -> Personagem(HalflingPesLeves())
                            "Halfling Robusto" -> Personagem(HalflingRobusto())
                            "Humano" -> Personagem(Humano())
                            "Meio-Elfo" -> Personagem(MeioElfo())
                            "Meio-Orc" -> Personagem(MeioOrc())
                            "Tiefling" -> Personagem(Tiefling())
                            else -> null
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para verificação e redirecionamento
        Button(
            onClick = {
                if (personagem != null) {
                    navController.navigate("distribuicao_atributos")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar para Distribuição de Atributos")
        }
    }
}
