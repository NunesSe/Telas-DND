package com.example.dnd

import Personagem
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun FichaPersonagem(personagem: Personagem?) {
    // Variáveis para armazenar os bônus
    var forcaBonus by remember { mutableStateOf(0) }
    var destrezaBonus by remember { mutableStateOf(0) }
    var constituicaoBonus by remember { mutableStateOf(0) }
    var inteligenciaBonus by remember { mutableStateOf(0) }
    var sabedoriaBonus by remember { mutableStateOf(0) }
    var carismaBonus by remember { mutableStateOf(0) }

    // Chama aplicaBonusRacial apenas uma vez e armazena os bônus
    if (personagem != null) {
        if (forcaBonus == 0 && destrezaBonus == 0 && constituicaoBonus == 0 &&
            inteligenciaBonus == 0 && sabedoriaBonus == 0 && carismaBonus == 0) {
            val bonus = personagem.aplicaBonusRacial()
            forcaBonus = bonus["forca"] ?: 0
            destrezaBonus = bonus["destreza"] ?: 0
            constituicaoBonus = bonus["constituicao"] ?: 0
            inteligenciaBonus = bonus["inteligencia"] ?: 0
            sabedoriaBonus = bonus["sabedoria"] ?: 0
            carismaBonus = bonus["carisma"] ?: 0
        }

        // Calcula a vida
        personagem.calcularVida()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Atributo", modifier = Modifier.weight(1f))
                Text("Valor", modifier = Modifier.weight(1f))
                Text("Bônus", modifier = Modifier.weight(1f))
            }

            Divider()

            // Exibe os atributos com bônus
            AtributoRow("Força", personagem.forca, forcaBonus)
            AtributoRow("Destreza", personagem.destreza, destrezaBonus)
            AtributoRow("Constituição", personagem.constituicao, constituicaoBonus)
            AtributoRow("Inteligência", personagem.inteligencia, inteligenciaBonus)
            AtributoRow("Sabedoria", personagem.sabedoria, sabedoriaBonus)
            AtributoRow("Carisma", personagem.carisma, carismaBonus)

            Spacer(modifier = Modifier.height(16.dp))

            // Exibe os pontos de vida
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Pontos de Vida:", modifier = Modifier.weight(1f))
                Text("${personagem.vida}", modifier = Modifier.weight(2f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Exibe os pontos disponíveis
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Pontos Disponíveis:", modifier = Modifier.weight(1f))
                Text("${personagem.pontosDisponiveis}", modifier = Modifier.weight(2f))
            }
        }
    }
}

@Composable
fun AtributoRow(label: String, valor: Int, bonus: Int?) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        Text(valor.toString(), modifier = Modifier.weight(1f))
        Text((bonus ?: 0).toString(), modifier = Modifier.weight(1f)) // Exibe o bônus ou 0 se não houver
    }
}
