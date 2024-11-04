package com.example.dnd_final.activities

import PersonagemLIB
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import bonusRacial.BonusRacialPadrao
import com.example.dnd_final.data.Personagem
import com.example.dnd_final.data.PersonagemDAO
import com.example.dnd_final.data.PersonagemDB
import kotlinx.coroutines.launch

class AtualizarPersonagem : ComponentActivity() {
    private lateinit var personagemDAO: PersonagemDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val personagemId = intent.getIntExtra("personagemId", -1)
        val db = PersonagemDB.getDatabase(this)
        personagemDAO = db.personagemDAO()

        fun atualizarPersonagem(personagem: Personagem) {
            lifecycleScope.launch {
                personagemDAO.update(personagem)
            }
        }

        setContent {
            var personagemState by remember { mutableStateOf<Personagem?>(null) }
            LaunchedEffect(personagemId) {
                if (personagemId != -1) {
                    val personagem = personagemDAO.getPersonagemById(personagemId)
                    personagemState = personagem
                }
            }

            if (personagemState != null) {
                AtualizarPersonagemScreen(
                    personagem = personagemState!!,
                    onAtualizarPersonagem = { personagem -> atualizarPersonagem(personagem) },
                    onPersonagemAtualizado = { finish() }
                )
            } else {
                Text("Personagem não encontrado")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtualizarPersonagemScreen(
    personagem: Personagem,
    onAtualizarPersonagem: (Personagem) -> Unit,
    onPersonagemAtualizado: () -> Unit
) {
    var nome by remember { mutableStateOf(personagem.nome) }
    var racaSelecionada by remember { mutableStateOf(personagem.raca) }

    var forca by remember { mutableStateOf(personagem.forca) }
    var destreza by remember { mutableStateOf(personagem.destreza) }
    var constituicao by remember { mutableStateOf(personagem.constituicao) }
    var inteligencia by remember { mutableStateOf(personagem.inteligencia) }
    var sabedoria by remember { mutableStateOf(personagem.sabedoria) }
    var carisma by remember { mutableStateOf(personagem.carisma) }

    val racas = listOf("Alto Elfo", "Anão", "Anão da Colina", "Anão da Montanha", "Draconato", "Drow", "Elfo", "Elfo da Floresta", "Gnomo", "Gnomo da Floresta", "Gnomo das Rochas", "Halfling", "Halfling Pés Leves", "Halfling Robusto", "Humano", "Meio-Elfo", "Meio-Orc", "Tiefling")

    var pontosDisponiveisLocal by remember { mutableStateOf(personagem.pontosDisponiveis) }
    val personagemDistribuicao = remember { PersonagemLIB(BonusRacialPadrao()) }
    personagemDistribuicao.forca = forca
    personagemDistribuicao.destreza = destreza
    personagemDistribuicao.constituicao = constituicao
    personagemDistribuicao.inteligencia = inteligencia
    personagemDistribuicao.sabedoria = sabedoria
    personagemDistribuicao.carisma = carisma
    personagemDistribuicao.setPontosDisponiveis(pontosDisponiveisLocal)

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    fun atualizarPontos(atributo: String, novoValor: Int): Boolean {
        val valorAtual = when (atributo) {
            "forca" -> forca
            "destreza" -> destreza
            "constituicao" -> constituicao
            "inteligencia" -> inteligencia
            "sabedoria" -> sabedoria
            "carisma" -> carisma
            else -> return false
        }

        val delta = novoValor - valorAtual

        if (personagemDistribuicao.alterarAtributo(atributo, novoValor)) {
            pontosDisponiveisLocal -= delta
            when (atributo) {
                "forca" -> forca = novoValor
                "destreza" -> destreza = novoValor
                "constituicao" -> constituicao = novoValor
                "inteligencia" -> inteligencia = novoValor
                "sabedoria" -> sabedoria = novoValor
                "carisma" -> carisma = novoValor
            }
            return true
        } else {
            showErrorDialog = true
            errorMessage = "Pontos insuficientes para atribuir $atributo ao valor $novoValor."
            return false
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Personagem") },
            modifier = Modifier.fillMaxWidth()
        )

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
            }}

        Text("Pontos Disponíveis: $pontosDisponiveisLocal", modifier = Modifier.padding(8.dp))

        AtributoInputUpdate("Força", forca) { atualizarPontos("forca", it) }
        AtributoInputUpdate("Destreza", destreza) { atualizarPontos("destreza", it) }
        AtributoInputUpdate("Constituição", constituicao) { atualizarPontos("constituicao", it) }
        AtributoInputUpdate("Inteligência", inteligencia) { atualizarPontos("inteligencia", it) }
        AtributoInputUpdate("Sabedoria", sabedoria) { atualizarPontos("sabedoria", it) }
        AtributoInputUpdate("Carisma", carisma) { atualizarPontos("carisma", it) }

        Button(
            onClick = {
                personagem.nome = nome
                personagem.raca = racaSelecionada
                personagem.forca = forca
                personagem.destreza = destreza
                personagem.constituicao = constituicao
                personagem.inteligencia = inteligencia
                personagem.sabedoria = sabedoria
                personagem.carisma = carisma
                personagem.pontosDisponiveis = personagemDistribuicao.getPontosDisponiveis()
                personagem.bonusForca = personagemDistribuicao.retornarBonusRacial()["forca"] as Int
                personagem.bonusDestreza = personagemDistribuicao.retornarBonusRacial()["destreza"] as Int
                personagem.bonusConstituicao = personagemDistribuicao.retornarBonusRacial()["constituicao"] as Int
                personagem.bonusInteligencia = personagemDistribuicao.retornarBonusRacial()["inteligencia"] as Int
                personagem.bonusSabedoria = personagemDistribuicao.retornarBonusRacial()["sabedoria"] as Int
                personagem.bonusCarisma = personagemDistribuicao.retornarBonusRacial()["destreza"] as Int
                onAtualizarPersonagem(personagem)
                onPersonagemAtualizado()
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text("Atualizar Personagem")
        }
    }
}

@Composable
fun AtributoInputUpdate(
    nomeAtributo: String,
    valorAtual: Int,
    onValorMudou: (Int) -> Boolean
) {
    var valor by rememberSaveable { mutableStateOf(valorAtual) }
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
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    if (valor !in 8..15) {
                        showErrorDialog = true
                        errorMessage = "Valor inválido para $nomeAtributo. Insira um valor entre 8 e 15."
                        valor = valorAtual
                    } else {
                        val sucesso = onValorMudou(valor)
                        if (!sucesso) {
                            showErrorDialog = true
                            errorMessage = "Pontos insuficientes para atribuir $nomeAtributo ao valor $valor."
                            valor = valorAtual
                        }
                    }
                }
            }
    )

    if (showErrorDialog) {
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}


