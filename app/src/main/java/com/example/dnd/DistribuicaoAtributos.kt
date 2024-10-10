package com.example.dnd

import Personagem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DistribuicaoAtributos(personagem: Personagem?, navController: NavController) {
    if (personagem != null) {
        var forca by remember { mutableStateOf(personagem.forca) }
        var inputForca by remember { mutableStateOf("") }
        var destreza by remember { mutableStateOf(personagem.destreza) }
        var inputDestreza by remember { mutableStateOf("") }
        var constituicao by remember { mutableStateOf(personagem.constituicao) }
        var inputConstituicao by remember { mutableStateOf("") }
        var inteligencia by remember { mutableStateOf(personagem.inteligencia) }
        var inputInteligencia by remember { mutableStateOf("") }
        var sabedoria by remember { mutableStateOf(personagem.sabedoria) }
        var inputSabedoria by remember { mutableStateOf("") }
        var carisma by remember { mutableStateOf(personagem.carisma) }
        var inputCarisma by remember { mutableStateOf("") }
        var pontosDisponiveis by remember { mutableStateOf(personagem.pontosDisponiveis) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text("Pontos Disponíveis: $pontosDisponiveis ")
            Text("DIGITE OS PONTOS QUE DESEJA USAR ")
            AtributoTextField(
                label = "Força",
                valorAtributo = forca,
                inputValor = inputForca,
                onInputChange = { inputForca = it },
                onUpdateAtributo = { novoValor ->
                    val novoForca = personagem.distribuirPontosParaAtributo("forca", forca, novoValor)
                    if (novoForca != forca) {
                        forca = novoForca
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            AtributoTextField(
                label = "Destreza",
                valorAtributo = destreza,
                inputValor = inputDestreza,
                onInputChange = { inputDestreza = it },
                onUpdateAtributo = { novoValor ->
                    val novoDestreza = personagem.distribuirPontosParaAtributo("destreza", destreza, novoValor)
                    if (novoDestreza != destreza) {
                        destreza = novoDestreza
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            AtributoTextField(
                label = "Constituição",
                valorAtributo = constituicao,
                inputValor = inputConstituicao,
                onInputChange = { inputConstituicao = it },
                onUpdateAtributo = { novoValor ->
                    val novoConstituicao = personagem.distribuirPontosParaAtributo("constituicao", constituicao, novoValor)
                    if (novoConstituicao != constituicao) {
                        constituicao = novoConstituicao
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            AtributoTextField(
                label = "Inteligência",
                valorAtributo = inteligencia,
                inputValor = inputInteligencia,
                onInputChange = { inputInteligencia = it },
                onUpdateAtributo = { novoValor ->
                    val novoInteligencia = personagem.distribuirPontosParaAtributo("inteligencia", inteligencia, novoValor)
                    if (novoInteligencia != inteligencia) {
                        inteligencia = novoInteligencia
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            AtributoTextField(
                label = "Sabedoria",
                valorAtributo = sabedoria,
                inputValor = inputSabedoria,
                onInputChange = { inputSabedoria = it },
                onUpdateAtributo = { novoValor ->
                    val novoSabedoria = personagem.distribuirPontosParaAtributo("sabedoria", sabedoria, novoValor)
                    if (novoSabedoria != sabedoria) {
                        sabedoria = novoSabedoria
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            AtributoTextField(
                label = "Carisma",
                valorAtributo = carisma,
                inputValor = inputCarisma,
                onInputChange = { inputCarisma = it },
                onUpdateAtributo = { novoValor ->
                    val novoCarisma = personagem.distribuirPontosParaAtributo("carisma", carisma, novoValor)
                    if (novoCarisma != carisma) {
                        carisma = novoCarisma
                        pontosDisponiveis = personagem.atualizarPontosRestantes()
                    }
                }
            )

            Button(

                onClick = {
                        personagem.calcularVida()
                        PersonagemManager.personagem = personagem
                        navController.navigate("ficha")  }
                ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar para Ficha do Personagem")
            }
        }
    }
}

@Composable
fun AtributoTextField(
    label: String,
    valorAtributo: Int,
    inputValor: String,
    onInputChange: (String) -> Unit,
    onUpdateAtributo: (Int) -> Unit
) {
    OutlinedTextField(
        value = inputValor,
        onValueChange = onInputChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    val parsedValue = inputValor.toIntOrNull()
                    if (parsedValue != null) {
                        onUpdateAtributo(parsedValue)
                    }
                }
            }
    )
    Text("$label: $valorAtributo")
}
