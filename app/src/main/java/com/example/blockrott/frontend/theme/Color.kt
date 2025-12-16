package com.example.blockrott.frontend.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val AppBackground = Color(0xFFEBEBEB)

val PrimaryColor = Color(0xFFC7C7C7)
val SecondaryColor = Color(0xFF7829D3)

//  Colores de los componentes.
val ComponentBackground = Color(0xFFFFFFFF)
val ComponentSurface = Color(0xFFEBEBEB)
val BorderColor = Color(0xFF54575B)

//  Colores de Texto
val TextPrimary = Color(0xFF000000)
val TextSecondary = Color(0xFF4C4C4C)

//  Colores Temporizadores
val TimerBackground = Color(0xF0707070)
val TimerTextSelected = Color(0xFFFFFFFF)
val TimerTextUnselected = Color(0xFFADADAD)

//  APP Colors 
val Facebook = Color(0xFF1E2796)
val Instagram = Color(0xFFB6267C)
val TikTok = Color(0xFF000000)
val Youtube = Color(0xFFAF0B0B)
val Discord = Color(0xFF6226B6)
val Reddit = Color(0xFFFF572D)

// STATE
val Success = Color(0xFF7CE75F)
val Warning = Color(0xFFE7D95F)
val Error = Color(0xFFE73434)
val Information = Color(0xFF575757)

// Backgraund Color brush
val DarkBlueGradientStart = Color(0xFF000720)
val MediumBlueGradientEnd = Color(0xFF177AF6)
val backgroundBrush = Brush.verticalGradient(
    colors = listOf(DarkBlueGradientStart, MediumBlueGradientEnd)
)