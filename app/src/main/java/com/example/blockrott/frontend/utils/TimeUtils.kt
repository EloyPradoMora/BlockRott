package com.example.blockrott.frontend.utils

import com.example.blockrott.frontend.components.UsageStats

fun tiempoAMinutos(timeString: String): Int {
    var total = 0
    val horasMatch = "(\\d+)\\s*h".toRegex().find(timeString)
    val minutosMatch = "(\\d+)\\s*m".toRegex().find(timeString)
    horasMatch?.let {
        val horas = it.groupValues[1].toIntOrNull() ?: 0
        total += horas * 60
    }
    minutosMatch?.let {
        val minutes = it.groupValues[1].toIntOrNull() ?: 0
        total += minutes
    }
    return total

fun calcularTiempoTotal(listaTemporal: List<UsageStats>): Int {
    val tiempoEnMinutos = listaTemporal.sumOf { usageStat ->
        tiempoAMinutos(usageStat.usageTime)
    }
    return tiempoEnMinutos
}

fun formatearMinutosAHorasMinutos(totalMinutes: Int): String {
    if (totalMinutes < 60) {
        return "${totalMinutes}m"
    }

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return if (minutes == 0) {
        "${hours}h"
    } else {
        "${hours}h ${minutes}m"
    }
}