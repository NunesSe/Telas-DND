package com.example.dnd_final.activities

import PersonagemLIB
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.lifecycleScope
import com.example.dnd_final.data.Personagem
import com.example.dnd_final.data.PersonagemDB
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import bonusRacial.BonusRacialPadrao
import com.example.dnd_final.MainActivity


class CriarPersonagem : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = PersonagemDB.getDatabase(this)
        val personagemDAO = db.personagemDAO()
        super.onCreate(savedInstanceState)

        fun criarPersonagem(personagem: Personagem) {
            lifecycleScope.launch {
                personagemDAO.insert(personagem)
            }
        }

        setContent {
            Campos(
                onCriarPersonagem = { personagem -> criarPersonagem(personagem) },
                onNavigateToListar = { startActivity(Intent(this, MainActivity::class.java)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Campos(
    onCriarPersonagem: (Personagem) -> Unit,
    onNavigateToListar: () -> Unit
) {
    var pegarNome by rememberSaveable { mutableStateOf("") }
    val personagemDistribuicao = remember { PersonagemLIB(BonusRacialPadrao()) }
    var racaSelecionada by rememberSaveable { mutableStateOf<String>("") }
    var pontosDisponiveisLocal by remember { mutableStateOf(personagemDistribuicao.getPontosDisponiveis()) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val racas = listOf("Alto Elfo", "Anão", "Anão da Colina", "Anão da Montanha", "Draconato", "Drow", "Elfo", "Elfo da Floresta", "Gnomo", "Gnomo da Floresta", "Gnomo das Rochas", "Halfling", "Halfling Pés Leves", "Halfling Robusto", "Humano", "Meio-Elfo", "Meio-Orc", "Tiefling")

    fun atualizarPontos(atributo: String, novoValor: Int): Boolean {
        return if (personagemDistribuicao.alterarAtributo(atributo, novoValor)) {
            pontosDisponiveisLocal = personagemDistribuicao.getPontosDisponiveis()
            true
        } else {
            showErrorDialog = true
            errorMessage = "Pontos insuficientes para atribuir $atributo ao valor $novoValor."
            false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = pegarNome,
            onValueChange = { pegarNome = it },
            label = { Text("Nome do personagem") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Selecione uma Raça:")
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
            OutlinedTextField(
                value = racaSelecionada,
                onValueChange = { },
                readOnly = true,
                label = { Text("Raça") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                racas.forEach { raca ->
                    DropdownMenuItem(
                        text = { Text(raca) },
                        onClick = {
                            racaSelecionada = raca
                            personagemDistribuicao.alterarRaca(raca)
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }



        Text("Pontos Disponíveis: $pontosDisponiveisLocal", modifier = Modifier.padding(8.dp))

        AtributoInput("Força", personagemDistribuicao.forca) { atualizarPontos("forca", it) }
        AtributoInput("Destreza", personagemDistribuicao.destreza) { atualizarPontos("destreza", it) }
        AtributoInput("Constituição", personagemDistribuicao.constituicao) { atualizarPontos("constituicao", it) }
        AtributoInput("Inteligência", personagemDistribuicao.inteligencia) { atualizarPontos("inteligencia", it) }
        AtributoInput("Sabedoria", personagemDistribuicao.sabedoria) { atualizarPontos("sabedoria", it) }
        AtributoInput("Carisma", personagemDistribuicao.carisma) { atualizarPontos("carisma", it) }

        Button(
            onClick = {
                if (racaSelecionada == "") {
                    showErrorDialog = true
                    errorMessage = "Por favor, selecione uma raça antes de salvar o personagem."
                } else {
                    val per = Personagem().apply {
                        nome = pegarNome
                        forca = personagemDistribuicao.forca
                        destreza = personagemDistribuicao.destreza
                        constituicao = personagemDistribuicao.constituicao
                        inteligencia = personagemDistribuicao.inteligencia
                        sabedoria = personagemDistribuicao.sabedoria
                        carisma = personagemDistribuicao.carisma
                        pontosDisponiveis = personagemDistribuicao.getPontosDisponiveis()
                        raca = racaSelecionada
                        bonusForca = personagemDistribuicao.retornarBonusRacial()["forca"] as Int
                        bonusDestreza = personagemDistribuicao.retornarBonusRacial()["destreza"] as Int
                        bonusConstituicao = personagemDistribuicao.retornarBonusRacial()["constituicao"] as Int
                        bonusInteligencia = personagemDistribuicao.retornarBonusRacial()["inteligencia"] as Int
                        bonusSabedoria = personagemDistribuicao.retornarBonusRacial()["sabedoria"] as Int
                        bonusCarisma = personagemDistribuicao.retornarBonusRacial()["destreza"] as Int
                    }
                    onCriarPersonagem(per)
                    onNavigateToListar()
                }
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Salvar Personagem")
        }
    }

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

@Composable
fun AtributoInput(nomeAtributo: String, valorInicial: Int, onValorMudou: (Int) -> Boolean) {
    var valor by rememberSaveable { mutableStateOf(valorInicial) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = valor.toString(),
        onValueChange = { input ->
            valor = input.toIntOrNull() ?: valor
        },
        label = { Text(nomeAtributo) },
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    if (valor !in 8..15) {
                        showErrorDialog = true
                        errorMessage = "Valor inválido para $nomeAtributo. Insira um valor entre 8 e 15."
                        valor = 8
                    } else {
                        val sucesso = onValorMudou(valor)
                        if (!sucesso) {
                            showErrorDialog = true
                            errorMessage = "Pontos insuficientes para atribuir $nomeAtributo ao valor $valor."
                            valor = 8
                        }
                    }
                }
            },

    )

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Erro") },
        text = { Text(message) }
    )
}
