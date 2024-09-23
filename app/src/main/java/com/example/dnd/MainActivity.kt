package com.example.dnd

import Personagem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bonusRacial.*
import com.example.dnd.ui.theme.DndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DndTheme {
                DropdownPersonagem()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropdownPreview() {
    DndTheme {
        DropdownPersonagem()
    }
}

@Composable
fun DropdownPersonagem() {
    val racas = listOf(
        "Alto Elfo", "Anão", "Anão da Colina", "Anão da Montanha", "Draconato",
        "Drow", "Elfo", "Elfo da Floresta", "Gnomo", "Gnomo da Floresta",
        "Gnomo das Rochas", "Halfling", "Halfling Pés Leves", "Halfling Robusto",
        "Humano", "Meio-Elfo", "Meio-Orc", "Tiefling"
    )

    var personagem by remember { mutableStateOf<Personagem?>(null) }
    var expanded by remember { mutableStateOf(true) }
    var selectedRaca by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecione uma raça:")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedRaca,
                onValueChange = {},
                readOnly = true,
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth()
                    .clickable { expanded = true }
            )
        }

        DropdownMenu(
            modifier = Modifier.clickable { expanded = true },
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            racas.forEach { raca ->
                DropdownMenuItem(
                    modifier = Modifier.clickable { expanded = true },
                    text = { Text(text = raca) },
                    onClick = {
                        selectedRaca = raca
                        expanded = false

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




    }
}
