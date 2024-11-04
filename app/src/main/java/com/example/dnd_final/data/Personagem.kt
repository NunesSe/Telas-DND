package com.example.dnd_final.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personagens")
data class Personagem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "nome")
    var nome: String = "",

    @ColumnInfo(name = "forca")
    var forca: Int = 8,

    @ColumnInfo(name = "destreza")
    var destreza: Int = 8,

    @ColumnInfo(name = "constituicao")
    var constituicao: Int = 8,

    @ColumnInfo(name = "inteligencia")
    var inteligencia: Int = 8,

    @ColumnInfo(name = "sabedoria")
    var sabedoria: Int = 8,

    @ColumnInfo(name = "carisma")
    var carisma: Int = 8,

    @ColumnInfo(name = "pontos_disponiveis")
    var pontosDisponiveis: Int = 27,

    @ColumnInfo(name = "raca")
    var raca: String = "",

    @ColumnInfo(name = "bonus_forca")
    var bonusForca: Int = 0,

    @ColumnInfo(name = "bonus_destreza")
    var bonusDestreza: Int = 0,

    @ColumnInfo(name = "bonus_constituicao")
    var bonusConstituicao: Int = 0,

    @ColumnInfo(name = "bonus_inteligencia")
    var bonusInteligencia: Int = 0,

    @ColumnInfo(name = "bonus_sabedoria")
    var bonusSabedoria: Int = 0,

    @ColumnInfo(name = "bonus_carisma")
    var bonusCarisma: Int = 0
)
