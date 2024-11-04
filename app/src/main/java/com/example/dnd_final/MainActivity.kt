package com.example.dnd_final

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.dnd_final.activities.AtualizarPersonagem
import com.example.dnd_final.activities.CriarPersonagem
import com.example.dnd_final.activities.VerPersonagem
import com.example.dnd_final.data.Personagem
import com.example.dnd_final.data.PersonagemDAO
import com.example.dnd_final.data.PersonagemDB
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private lateinit var personagemDAO: PersonagemDAO
    private var personagens by mutableStateOf(emptyList<Personagem>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = PersonagemDB.getDatabase(this)
        personagemDAO = db.personagemDAO()

        setContent {
            MainContent()
        }
    }

    override fun onResume() {
        super.onResume()
        loadPersonagens() // Recarrega a lista sempre que MainActivity for retomada
    }

    private fun loadPersonagens() {
        val coroutineScope = lifecycleScope
        coroutineScope.launch {
            personagens = personagemDAO.getAllPersonagem()
        }
    }

    @Composable
    fun MainContent() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Personagens") })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val intent = Intent(this@MainActivity, CriarPersonagem::class.java)
                    startActivity(intent)
                }, modifier = Modifier.size(120.dp).padding(30.dp)) {
                    Text("+", style = MaterialTheme.typography.headlineMedium)
                }
            }
        ) { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(personagens) { personagem ->
                    PersonagemCard(personagem = personagem, onUpdate = {
                        val intent = Intent(this@MainActivity, AtualizarPersonagem::class.java)
                        intent.putExtra("personagemId", personagem.id)
                        startActivity(intent)
                    }, onDelete = {
                        val coroutineScope = lifecycleScope
                        coroutineScope.launch {
                            personagemDAO.delete(personagem)
                            loadPersonagens()
                        }
                    }, onRead = {
                        val intentRead = Intent(this@MainActivity, VerPersonagem::class.java)
                        intentRead.putExtra("personagemId", personagem.id)
                        startActivity(intentRead)
                    })
                }
            }
        }
    }
}

@Composable
fun PersonagemCard(personagem: Personagem, onDelete: () -> Unit, onUpdate: () -> Unit, onRead : () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nome: ${personagem.nome}")
            Text("ID: ${personagem.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onRead, modifier = Modifier.padding(4.dp)) {
                    Text("Ver")
                }
                Button(onClick = onUpdate, modifier = Modifier.padding(4.dp)) {
                    Text("Atualizar")
                }
                Button(onClick = onDelete, modifier = Modifier.padding(4.dp)) {
                    Text("Deletar")
                }
            }
        }
    }
}
