package com.example.dnd_final.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PersonagemDAO {
    @Query("SELECT * FROM personagens")
    suspend fun getAllPersonagem(): List<Personagem>

    @Query("SELECT * FROM personagens WHERE id=:id")
    suspend fun getPersonagemById(id : Int): Personagem

    @Insert
    suspend fun insert(personagem: Personagem)

    @Delete
    suspend fun delete(personagem: Personagem)

    @Update
    suspend fun update(personagem: Personagem)
}