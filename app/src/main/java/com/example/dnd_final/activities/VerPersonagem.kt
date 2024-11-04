package com.example.dnd_final.activities

import PersonagemLIB
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bonusRacial.BonusRacialPadrao
import com.example.dnd_final.MainActivity
import com.example.dnd_final.data.Personagem
import com.example.dnd_final.data.PersonagemDAO
import com.example.dnd_final.data.PersonagemDB

class VerPersonagem : ComponentActivity() {
    private lateinit var personagemDAO: PersonagemDAO

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val personagemId = intent.getIntExtra("personagemId", -1)
        val db = PersonagemDB.getDatabase(this)
        personagemDAO = db.personagemDAO()

        setContent {
            var personagemState by remember { mutableStateOf<Personagem?>(null) }
            LaunchedEffect(personagemId) {
                if (personagemId != -1) {
                    val personagem = personagemDAO.getPersonagemById(personagemId)
                    personagemState = personagem
                }
            }

            if (personagemState != null) {
                PersonagemRead(
                    personagem = personagemState!!,
                    onNavigateToListar = { startActivity(Intent(this, MainActivity::class.java)) }
                )
            } else {
                Text("Personagem não encontrado")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonagemRead(personagem: Personagem, onNavigateToListar: () -> Unit) {
    val personagemLib = PersonagemLIB(BonusRacialPadrao())
    val modificadorConstituicao = personagemLib.calcularModificador(personagem.constituicao + personagem.bonusConstituicao)
    val vidaTotal = 10 + modificadorConstituicao

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nome: ${personagem.nome}", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Vida: $vidaTotal", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Raça: ${personagem.raca}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Atributo", modifier = Modifier.weight(1f))
            Text("Valor", modifier = Modifier.weight(1f))
            Text("Bônus Racial", modifier = Modifier.weight(1f))
            Text("Total", modifier = Modifier.weight(1f))
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        AttributeRow("Força", personagem.forca, personagem.bonusForca)
        AttributeRow("Destreza", personagem.destreza, personagem.bonusDestreza)
        AttributeRow("Constituição", personagem.constituicao, personagem.bonusConstituicao)
        AttributeRow("Inteligência", personagem.inteligencia, personagem.bonusInteligencia)
        AttributeRow("Sabedoria", personagem.sabedoria, personagem.bonusSabedoria)
        AttributeRow("Carisma", personagem.carisma, personagem.bonusCarisma)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToListar) {
            Text("Voltar")
        }
    }
}

@Composable
fun AttributeRow(nomeAtributo: String, valorAtributo: Int, bonusRacial: Int) {
    val total = valorAtributo + bonusRacial
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(nomeAtributo, modifier = Modifier.weight(1f))
        Text(valorAtributo.toString(), modifier = Modifier.weight(1f))
        Text(bonusRacial.toString(), modifier = Modifier.weight(1f))
        Text(total.toString(), modifier = Modifier.weight(1f))
    }
}
